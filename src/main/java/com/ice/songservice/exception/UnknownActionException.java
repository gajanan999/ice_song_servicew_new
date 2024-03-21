package com.ice.songservice.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnknownActionException extends Exception{

    private String errorCode;

    private String errorMessage;

    public UnknownActionException(String message) {
        super(message);
    }

    public UnknownActionException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
