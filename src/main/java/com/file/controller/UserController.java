package com.file.controller;

import com.file.model.User;
import com.file.payload.ApiResponse;
import com.file.payload.auth.JwtAuthRequest;
import com.file.payload.auth.JwtAuthResponse;
import com.file.security.JwtHelper;
import com.file.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User user1 = this.userService.registerUser(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);
    }

    @PostMapping("login/{userId}")
    public ResponseEntity<ApiResponse> loginUser(@PathVariable String userId) {
        ApiResponse apiResponse = this.userService.loginUser(userId);
        return new ResponseEntity<>(apiResponse,HttpStatus.FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request){

        this.doAuthenticate(request.getUserName(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
        System.out.println(userDetails.getUsername()+" "+userDetails.getPassword());
        String token = this.jwtHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
//        System.out.println(token);
        response.setMessage("User successfully logged in");

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }
}
