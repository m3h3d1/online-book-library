//package com.mehedi.service;
//
//import com.mehedi.entity.User;
//import com.mehedi.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserService {
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    public User registerUser(User user) {
//        // Check if the user with the provided email already exists
//        if (userRepository.findByEmail(user.getEmail()) != null) {
//            throw new RuntimeException("User with this email already exists");
//        }
//
//        // Hash the user's password before saving it
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        return userRepository.save(user);
//    }
//}