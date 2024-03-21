package com.ice.songservice.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordAlreadyExistsException extends Exception{


    private String errorCode;

    private String errorMessage;

    public RecordAlreadyExistsException(String message) {
        super(message);
    }

    public RecordAlreadyExistsException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
