package com.mehedi.controller;

import com.mehedi.constatnts.AppConstants;
import com.mehedi.dto.*;
import com.mehedi.entity.User;
import com.mehedi.service.BorrowService;
import com.mehedi.service.UserAuthService;
import com.mehedi.service.UserService;
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
import java.util.Set;

@RestController
@RequestMapping("")
public class UserController {

    @Autowired
    private UserAuthService userAuthService;
    
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private UserService userService;

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

    @GetMapping("users/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.findUserDetails(userId);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/users/{userId}/books")
    public ResponseEntity<?> retriveBooks(@PathVariable Long userId) {
        try {
            List<BookBorrowDTO> allBookByUser = borrowService.getAllBookByUser(userId);
            return new ResponseEntity<>(allBookByUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/users/{userId}/borrowed-books")
    public ResponseEntity<Set<?>> findCurrentBorrowedBooksByUser(@PathVariable Long userId){
        return new ResponseEntity<>(borrowService.currentlyBorrowedBooks(userId),HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> allUsers = userService.getAllUsers();
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}