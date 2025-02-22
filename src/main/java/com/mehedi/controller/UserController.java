package com.mehedi.controller;

import com.mehedi.constatnts.AppConstants;
import com.mehedi.dto.BorrowHistoryDTO;
import com.mehedi.dto.LoginResponseDTO;
import com.mehedi.dto.UserDto;
import com.mehedi.dto.UserLoginRequestModel;
import com.mehedi.service.BorrowService;
import com.mehedi.service.UserAuthService;
import com.mehedi.utils.JWTUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserAuthService userAuthService;
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private BorrowService borrowService;

    @GetMapping("/users/{userId}/history")
    public ResponseEntity<List<BorrowHistoryDTO>> getUserBorrowHistory(@PathVariable Long userId) {
        List<BorrowHistoryDTO> borrowHistory = borrowService.getUserBorrowHistory(userId);
        return new ResponseEntity<>(borrowHistory, HttpStatus.OK);
    }

    @PostMapping("/user/register")
    public ResponseEntity<LoginResponseDTO> registerUser(@RequestBody UserDto userDto) throws Exception {
        return new ResponseEntity<>(userAuthService.createUser(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestModel userLoginReqModel, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginReqModel.getEmail(), userLoginReqModel.getPassword()));
            UserDto userDto = userAuthService.getUser(userLoginReqModel.getEmail());
            String accessToken = JWTUtils.generateToken(userDto.getEmail());
            Map<String, Object> loginResponse = new HashMap<>();
            loginResponse.put("userId", userDto.getUserId());
            loginResponse.put("email", userDto.getEmail());
            loginResponse.put(AppConstants.HEADER_STRING, AppConstants.TOKEN_PREFIX + accessToken);
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);

        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Wrong password!", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Wrong Email!", HttpStatus.UNAUTHORIZED);

        }
    }
}