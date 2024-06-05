package com.file.controller;

import com.file.model.User;
import com.file.payload.ApiResponse;
import com.file.payload.auth.JwtAuthRequest;
import com.file.payload.auth.JwtAuthResponse;
import com.file.security.JwtHelper;
import com.file.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnCreatedStatus() {
        User user = new User();
        User savedUser = new User();
        savedUser.setId("1");

        when(userService.registerUser(any(User.class))).thenReturn(savedUser);

        ResponseEntity<User> response = userController.registerUser(user);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(savedUser, response.getBody());
    }

    @Test
    void loginUser_ShouldReturnFoundStatusWhenUserFound() {
        String userId = "1";
        ApiResponse apiResponse = new ApiResponse("user login successfully");

        when(userService.loginUser(userId)).thenReturn(apiResponse);

        ResponseEntity<ApiResponse> response = userController.loginUser(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.FOUND, response.getStatusCode());
        assertEquals(apiResponse, response.getBody());
    }

    @Test
    void createToken_ShouldReturnOkStatusWithToken() {
        JwtAuthRequest request = new JwtAuthRequest();
        request.setUserName("username");
        request.setPassword("password");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        when(userDetails.getPassword()).thenReturn("encodedPassword");

        String token = "jwtToken";

        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtHelper.generateToken(any(UserDetails.class))).thenReturn(token);

        doNothing().when(authenticationManager).authenticate(any());

        ResponseEntity<JwtAuthResponse> response = userController.createToken(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User successfully logged in", response.getBody().getMessage());
        assertEquals(token, response.getBody().getToken());
    }

    @Test
    void createToken_ShouldThrowBadCredentialsExceptionWhenAuthenticationFails() {
        JwtAuthRequest request = new JwtAuthRequest();
        request.setUserName("username");
        request.setPassword("password");

        doThrow(new BadCredentialsException("Invalid Username or Password")).when(authenticationManager).authenticate(any());

        assertThrows(BadCredentialsException.class, () -> userController.createToken(request));
    }
}
