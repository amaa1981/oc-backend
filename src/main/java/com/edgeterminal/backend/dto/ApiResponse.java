package com.edgeterminal.backend.dto;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

    public static <T> ApiResponse<T> success(String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setMsg(msg);
        return response;
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    public static <T> ApiResponse<T> unauthorized() {
        return error(401, "Unauthorized - please log in");
    }

    public static <T> ApiResponse<T> forbidden() {
        return error(403, "Forbidden - insufficient permissions");
    }
}