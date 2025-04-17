package com.mehedi.service;

import com.mehedi.dto.LoginResponseDTO;
import com.mehedi.dto.UserDto;
import com.mehedi.entity.User;
import com.mehedi.exception.ErrorMessage;
import com.mehedi.repository.UserRepository;
import com.mehedi.utils.JWTUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAuthService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserAuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
    public LoginResponseDTO createUser(UserDto userDto) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        if(userRepository.findByEmail(userDto.getEmail()).isPresent())
            throw new Exception("Record already exists");
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setAddress(userDto.getAddress());
        user.setRole(userDto.getRole());
        String publicUserId = JWTUtils.generateUserID(10);
        // user.setUserId(publicUserId);
        User storedUserDetails =userRepository.save(user);
        String accessToken = JWTUtils.generateToken(publicUserId); // Adjust this based on your token generation logic

        // Create a response object containing the token
        LoginResponseDTO responseDto = modelMapper.map(storedUserDetails, LoginResponseDTO.class);
        responseDto.setAccessToken(accessToken);
        return responseDto;
    }

    public UserDto getUser(String email) {
        User user = userRepository.findByEmail(email).get();
        if(user == null) throw new UsernameNotFoundException("No record found");
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(user,returnValue);
        return returnValue;
    }

    public UserDto getUserByUserId(Long userId) throws Exception {
        UserDto returnValue = new UserDto();
        User user = userRepository.findByUserId(userId).orElseThrow(Exception::new);
        BeanUtils.copyProperties(user,returnValue);
        return returnValue;
    }


    public Long getUserId() {
        Optional<User> userOptional = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userOptional.isPresent()) {
            return userOptional.get().getUserId();
        }
        throw new ErrorMessage("There is no user in the database with email");
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority grantedAuthority= new SimpleGrantedAuthority("ROLE_"+user.getRole().name());
        authorities.add(grantedAuthority);
        if(user==null) throw new UsernameNotFoundException(email);
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),
                true,true,true,true,authorities);
    }
}