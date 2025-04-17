package com.mehedi.controller;

import com.mehedi.constatnts.AppConstants;
import com.mehedi.dto.*;
import com.mehedi.entity.User;
import com.mehedi.service.BookReservationService;
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

    private final UserAuthService userAuthService;
    private final AuthenticationManager authenticationManager;
    private final BorrowService borrowService;
    private final UserService userService;
    private final BookReservationService bookReservationService;

    @Autowired
    public UserController(UserAuthService userAuthService, AuthenticationManager authenticationManager, BorrowService borrowService, UserService userService, BookReservationService bookReservationService) {
        this.userAuthService = userAuthService;
        this.authenticationManager = authenticationManager;
        this.borrowService = borrowService;
        this.userService = userService;
        this.bookReservationService = bookReservationService;
    }

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
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
            UserDto userDto = userAuthService.getUser(loginRequestDTO.getEmail());
            String accessToken = JWTUtils.generateToken(userDto.getEmail());
            Map<String, Object> loginResponse = new HashMap<>();
            loginResponse.put("userId", userDto.getUserId());
            loginResponse.put("email", userDto.getEmail());
            loginResponse.put("role", userDto.getRole());
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

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Failed to delete the user with id: " + userId, HttpStatus.INTERNAL_SERVER_ERROR);
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

    @GetMapping("/users/{userId}/reserved-books")
    public ResponseEntity<Set<?>> findCurrentReservedBooksByUser(@PathVariable Long userId){
        return new ResponseEntity<>(bookReservationService.currentlyReservedBooks(userId),HttpStatus.OK);
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