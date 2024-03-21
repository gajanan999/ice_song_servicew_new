package com.ice.songservice.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NoRecordFound extends Exception{

    private String errorCode;

    private String errorMessage;

    public NoRecordFound(String message) {
        super(message);
    }

    public NoRecordFound(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
