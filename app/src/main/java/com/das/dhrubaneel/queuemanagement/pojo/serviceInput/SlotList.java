package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 13-Jan-17.
 */
public class SlotList {
    private String service;
    private String method;
    private SlotListData data;

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

    public SlotListData getData() {
        return data;
    }

    public void setData(SlotListData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SlotList{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
