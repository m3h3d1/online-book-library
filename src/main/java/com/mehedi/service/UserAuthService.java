package com.mehedi.service;

import com.mehedi.dto.LoginResponseDTO;
import com.mehedi.dto.UserDto;
import com.mehedi.entity.User;
import com.mehedi.repository.UserRepository;
import com.mehedi.utils.JWTUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UserAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserAuthService(BCryptPasswordEncoder bCryptPasswordEncoder) {
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
}