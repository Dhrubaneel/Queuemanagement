package com.das.dhrubaneel.queuemanagement.pojo.serviceOutput;

/**
 * Created by Dhruba on 13-Jan-17.
 */
public class Available_Slot {
    private String ap_order;
    private String ap_time;

    public String getAp_order() {
        return ap_order;
    }

    public void setAp_order(String ap_order) {
        this.ap_order = ap_order;
    }

    public String getAp_time() {
        return ap_time;
    }

    public void setAp_time(String ap_time) {
        this.ap_time = ap_time;
    }

    @Override
    public String toString() {
        return "Available_Slot{" +
                "ap_order='" + ap_order + '\'' +
                ", ap_time='" + ap_time + '\'' +
                '}';
    }
}
