package com.monet.mylibrary.model.sdk;

public class SdkPojo {
    private String code;

    private Data data;

    private String error;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ClassPojo [code = " + code + ", data = " + data + ", error = " + error + ", message = " + message + "]";
    }
}
