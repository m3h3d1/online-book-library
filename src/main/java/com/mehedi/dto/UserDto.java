package com.mehedi.dto;

import com.mehedi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String address;
    private User.Role role;
}
