package com.springboot.backend.diego.usersapp.users_backend.auth;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJWTConfig {

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final String HEADER_AUTHORIZACION = "Authorization";
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String CONTENT_TYPE = "application/json";
}
