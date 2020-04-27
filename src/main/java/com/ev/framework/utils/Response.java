package com.ev.framework.utils;

import java.util.Arrays;
import java.util.List;

public class Response<T> {
    private static final String SUCCESS_CODE = "0";
    private static final String FAILURE_CODE = "500"; // 通用业务失败代码

    private boolean success;
    private String code;
    private String message;
    private Object obj;
    private T data;
    private String result;
    private List<Object> placeholder;

    public Response() {
    }

    public Response(Builder<T> builder) {
        this.success = builder.success;
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
        this.obj = builder.obj;
        this.placeholder = builder.placeholder;
    }

    public static <T> Response<T> succeed() {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        return response;
    }

    public static <T> Response<T> succeed(T data) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> succeed(T data, String message) {
        Response<T> response = new Response<>();
        response.setSuccess(true);
        response.setCode(SUCCESS_CODE);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static Response<String> fail(String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(FAILURE_CODE);
        response.setMessage(message);
        return response;
    }

    public static Response<String> fail(String code, String message) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static Response<String> fail(String code, String message, String data) {
        Response response = new Response();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Object> getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(List<Object> placeholder) {
        this.placeholder = placeholder;
    }

    public static class Builder<T> {
        private boolean success;
        private String code;
        private String message;
        private T data;
        private Object obj;
        private List<Object> placeholder;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public Builder obj(Object obj) {
            this.obj = obj;
            return this;
        }

        public Builder placeholder(Object... placeholder) {
            this.placeholder = Arrays.asList(placeholder);
            return this;
        }

        public Response buildSuccess() {
            this.code = SUCCESS_CODE;
            return new Response(this);
        }

        public Response buildFail() {
            if (StringUtils.isBlank(this.code)) {
                this.code = FAILURE_CODE;
            }
            return new Response(this);
        }
    }
}