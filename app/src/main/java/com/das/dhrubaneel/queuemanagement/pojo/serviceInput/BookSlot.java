package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class BookSlot {
    private String service;
    private String method;
    private BookSlotData data;

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

    public BookSlotData getData() {
        return data;
    }

    public void setData(BookSlotData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BookSlot{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
