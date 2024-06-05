package com.file.payload.auth;

import lombok.Data;

@Data
public class JwtAuthRequest {
    private String userName;
    private String password;
}
