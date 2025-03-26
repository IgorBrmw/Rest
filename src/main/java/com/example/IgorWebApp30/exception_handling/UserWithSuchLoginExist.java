package com.example.IgorWebApp30.exception_handling;

import org.springframework.dao.DataIntegrityViolationException;

public class UserWithSuchLoginExist extends DataIntegrityViolationException {
    public UserWithSuchLoginExist(String message) {
        super(message);
    }
}
