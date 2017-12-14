package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 05-Jan-17.
 */
public class Login {
    private String service;
    private String method;
    private LoginData data;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public LoginData getData() {
        return data;
    }

    public void setData(LoginData data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Login{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
