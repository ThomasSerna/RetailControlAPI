package com.app.retailcontrol.dto;

public class ApiResponseDTO<T> {

    private String message;
    private String status;
    private Integer statusCode;
    private T data;

    public ApiResponseDTO(){}

    public ApiResponseDTO(String message, String status, Integer statusCode, T data) {
        this.message = message;
        this.status = status;
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
