package com.mehedi.controller;

import com.mehedi.dto.BorrowHistoryDTO;
import com.mehedi.dto.LoginResponseDTO;
import com.mehedi.dto.UserDto;
import com.mehedi.service.BorrowService;
import com.mehedi.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}