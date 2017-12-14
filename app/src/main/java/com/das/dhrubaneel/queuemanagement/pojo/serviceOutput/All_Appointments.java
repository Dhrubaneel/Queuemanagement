package com.das.dhrubaneel.queuemanagement.pojo.serviceOutput;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class All_Appointments {
    private String ap_id;
    private String ap_device_mac_id;
    private String ap_date;
    private String ap_time;
    private String ap_status;
    private String user_name;
    private String cham_location;

    public String getAp_id() {
        return ap_id;
    }

    public void setAp_id(String ap_id) {
        this.ap_id = ap_id;
    }

    public String getAp_device_mac_id() {
        return ap_device_mac_id;
    }

    public void setAp_device_mac_id(String ap_device_mac_id) {
        this.ap_device_mac_id = ap_device_mac_id;
    }

    public String getAp_date() {
        return ap_date;
    }

    public void setAp_date(String ap_date) {
        this.ap_date = ap_date;
    }

    public String getAp_time() {
        return ap_time;
    }

    public void setAp_time(String ap_time) {
        this.ap_time = ap_time;
    }

    public String getAp_status() {
        return ap_status;
    }

    public void setAp_status(String ap_status) {
        this.ap_status = ap_status;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCham_location() {
        return cham_location;
    }

    public void setCham_location(String cham_location) {
        this.cham_location = cham_location;
    }

    @Override
    public String toString() {
        return "All_Appointments{" +
                "ap_id='" + ap_id + '\'' +
                ", ap_device_mac_id='" + ap_device_mac_id + '\'' +
                ", ap_date='" + ap_date + '\'' +
                ", ap_time='" + ap_time + '\'' +
                ", ap_status='" + ap_status + '\'' +
                ", user_name='" + user_name + '\'' +
                ", cham_location='" + cham_location + '\'' +
                '}';
    }
}
