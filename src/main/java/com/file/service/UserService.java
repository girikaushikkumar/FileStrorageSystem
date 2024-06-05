package com.file.service;

import com.file.model.User;
import com.file.payload.ApiResponse;
import com.file.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        User user1 = this.userRepo.save(user);
        return user1;
    }

    public ApiResponse loginUser(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(null);
        if(user == null)
            return new ApiResponse("User not found");
        return new ApiResponse("user login successfully");
    }
}
