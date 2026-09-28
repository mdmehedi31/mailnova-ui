package com.mailnova.service;

import org.springframework.stereotype.Service;

/** Default placeholder: never pretends an account was created. Delete or replace when real registration exists. */
@Service
public class NotConfiguredSignupService implements SignupService {
    @Override
    public Result register(String fullName, String email, String rawPassword) {
        return new Result(false, "Registration is not connected yet.");
    }
}
