package com.code.hotel_management.dto.response;

public class ResponseError extends ResponseData {

    public ResponseError(int status, String message) {
        super(status, message);
    }
}