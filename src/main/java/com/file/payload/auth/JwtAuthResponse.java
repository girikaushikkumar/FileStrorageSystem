package com.file.payload.auth;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private  String token;
    private String message;
}
