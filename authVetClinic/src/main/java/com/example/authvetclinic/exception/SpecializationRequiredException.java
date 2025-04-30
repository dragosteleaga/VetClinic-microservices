package com.example.authvetclinic.exception;

public class SpecializationRequiredException extends RuntimeException {
    public SpecializationRequiredException(String message){
        super(message);
    }
}