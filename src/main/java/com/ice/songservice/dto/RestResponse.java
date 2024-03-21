package com.ice.songservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RestResponse {

    private Object data;

    private String errorCode;

    private String message;

    private String status;

}
