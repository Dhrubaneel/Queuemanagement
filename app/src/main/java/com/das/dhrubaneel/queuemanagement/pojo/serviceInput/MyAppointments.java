package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class MyAppointments {
    private String service;
    private String method;
    private MyAppointmentsData data;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public MyAppointmentsData getData() {
        return data;
    }

    public void setData(MyAppointmentsData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MyAppointments{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
