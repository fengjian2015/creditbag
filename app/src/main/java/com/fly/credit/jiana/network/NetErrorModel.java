package com.fly.credit.jiana.network;

import java.io.Serializable;

public class NetErrorModel implements Serializable {
    private int status;
    private String message;
    private String code;
    private String p;

    public NetErrorModel(int status, String message,String code) {
        this.status = status;
        this.message = message;
        this.code=code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
