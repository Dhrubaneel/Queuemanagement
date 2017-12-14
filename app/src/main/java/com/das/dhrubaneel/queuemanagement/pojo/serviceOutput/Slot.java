package com.das.dhrubaneel.queuemanagement.pojo.serviceOutput;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class Slot {
    private String d_c_id;

    private String d_c_end_time;

    private String d_c_slot_length;

    private String d_c_start_time;

    private String d_c_day;

    private String d_c_mac;

    public String getD_c_id() {
        return d_c_id;
    }

    public void setD_c_id(String d_c_id) {
        this.d_c_id = d_c_id;
    }

    public String getD_c_end_time() {
        return d_c_end_time;
    }

    public void setD_c_end_time(String d_c_end_time) {
        this.d_c_end_time = d_c_end_time;
    }

    public String getD_c_slot_length() {
        return d_c_slot_length;
    }

    public void setD_c_slot_length(String d_c_slot_length) {
        this.d_c_slot_length = d_c_slot_length;
    }

    public String getD_c_start_time() {
        return d_c_start_time;
    }

    public void setD_c_start_time(String d_c_start_time) {
        this.d_c_start_time = d_c_start_time;
    }

    public String getD_c_day() {
        return d_c_day;
    }

    public void setD_c_day(String d_c_day) {
        this.d_c_day = d_c_day;
    }

    public String getD_c_mac() {
        return d_c_mac;
    }

    public void setD_c_mac(String d_c_mac) {
        this.d_c_mac = d_c_mac;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "d_c_id='" + d_c_id + '\'' +
                ", d_c_end_time='" + d_c_end_time + '\'' +
                ", d_c_slot_length='" + d_c_slot_length + '\'' +
                ", d_c_start_time='" + d_c_start_time + '\'' +
                ", d_c_day='" + d_c_day + '\'' +
                ", d_c_mac='" + d_c_mac + '\'' +
                '}';
    }
}
