package com.mehedi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mehedi.dto.LoginResponseDTO;
import com.mehedi.dto.UserDto;
import com.mehedi.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
public class UserAuthServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserAuthService userAuthService;

    @Test
    public void registerUserPositive() throws Exception {
        // Arrange
        UserDto userDTO = new UserDto(99L, "Mehedi", "Hasan", "mehedi@test.com", "1234", "dhaka", User.Role.CUSTOMER);

        when(userAuthService.createUser(any(UserDto.class)))
                .thenReturn(LoginResponseDTO.builder()
                        .userId(99L)
                        .email("mehedi@test.com")
                        .AccessToken("accessToken")
                        .build());

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .content(asJsonString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("accessToken")))
                .andReturn();

        // Assert
        Assertions.assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
