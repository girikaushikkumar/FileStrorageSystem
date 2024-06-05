package com.file.service;

import com.file.model.User;
import com.file.payload.ApiResponse;
import com.file.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldEncodePasswordAndSaveUser() {
        User user = new User();
        user.setPassword("password");

        User savedUser = new User();
        savedUser.setId("1");
        savedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void loginUser_ShouldReturnApiResponseWhenUserNotFound() {
        String userId = "1";
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        ApiResponse response = userService.loginUser(userId);

        assertNotNull(response);
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void loginUser_ShouldReturnApiResponseWhenUserFound() {
        String userId = "1";
        User user = new User();
        user.setId(userId);

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        ApiResponse response = userService.loginUser(userId);

        assertNotNull(response);
        assertEquals("user login successfully", response.getMessage());
    }
}
