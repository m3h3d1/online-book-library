package com.mehedi.service;

import com.mehedi.entity.BookBorrow;
import com.mehedi.entity.User;
import com.mehedi.exception.ErrorMessage;
import com.mehedi.exception.UnauthorizedUserException;
import com.mehedi.exception.UserNotFoundException;
import com.mehedi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired

    private UserRepository userRepository;

    public User findUserDetails(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userNow = userRepository.findByEmail(authentication.getName()).get();
        if(userNow.getRole() != User.Role.ADMIN) {
            throw new UnauthorizedUserException("You are not allowed to access this resource.");
        }

        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception ex) {
            throw new ErrorMessage("Failed to delete the user with ID: " + userId + ". Error message:" + ex.toString());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
