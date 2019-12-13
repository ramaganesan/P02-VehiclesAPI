package com.udacity.pricing.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class PriceNotFoundException extends RuntimeException{

    public PriceNotFoundException(){

    }

    public PriceNotFoundException(String message){
        super(message);
    }
}
