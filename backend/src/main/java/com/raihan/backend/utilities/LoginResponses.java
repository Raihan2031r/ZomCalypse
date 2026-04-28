package com.raihan.backend.utilities;

import java.util.Optional;
import java.util.UUID;

public class LoginResponses {
    private String token;
    private long expires;

    public String getToken(){
        return token;
    }

    public LoginResponses setToken(String token) {
        this.token = token;
        return this;
    }

    public long getExpiresIn() {
        return expires;
    }

    public LoginResponses setExpiresIn(long expires) {
        this.expires = expires;
        return this;
    }
}
