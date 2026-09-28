package com.mailnova.service;

/** Boundary between the signup UI and persistence. Provide your own @Service to replace the default. */
public interface SignupService {
    record Result(boolean success, String message) {}
    Result register(String fullName, String email, String rawPassword);
}
