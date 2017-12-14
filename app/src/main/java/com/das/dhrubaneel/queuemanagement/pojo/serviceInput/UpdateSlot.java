package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 15-Jan-17.
 */
public class UpdateSlot {
    private String service;
    private String method;
    private UpdateSlotData data;

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

    public UpdateSlotData getData() {
        return data;
    }

    public void setData(UpdateSlotData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UpdateSlot{" +
                "service='" + service + '\'' +
                ", method='" + method + '\'' +
                ", data=" + data +
                '}';
    }
}
