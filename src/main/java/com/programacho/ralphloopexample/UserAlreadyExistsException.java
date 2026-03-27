package com.programacho.ralphloopexample;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}
