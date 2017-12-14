package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 22-Jan-17.
 */
public class ApptTableData {
    private int _id;
    private String ap_id;
    private String ap_time;
    private String ap_order;
    private String ap_doc_id;
    private String ap_patient_id;
    private String ap_ch_id;
    private String ap_doc_ch_id;
    private String ap_doc_mac;
    private String ap_alarm_time;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getAp_id() {
        return ap_id;
    }

    public void setAp_id(String ap_id) {
        this.ap_id = ap_id;
    }

    public String getAp_time() {
        return ap_time;
    }

    public void setAp_time(String ap_time) {
        this.ap_time = ap_time;
    }

    public String getAp_order() {
        return ap_order;
    }

    public void setAp_order(String ap_order) {
        this.ap_order = ap_order;
    }

    public String getAp_doc_id() {
        return ap_doc_id;
    }

    public void setAp_doc_id(String ap_doc_id) {
        this.ap_doc_id = ap_doc_id;
    }

    public String getAp_patient_id() {
        return ap_patient_id;
    }

    public void setAp_patient_id(String ap_patient_id) {
        this.ap_patient_id = ap_patient_id;
    }

    public String getAp_ch_id() {
        return ap_ch_id;
    }

    public void setAp_ch_id(String ap_ch_id) {
        this.ap_ch_id = ap_ch_id;
    }

    public String getAp_doc_ch_id() {
        return ap_doc_ch_id;
    }

    public void setAp_doc_ch_id(String ap_doc_ch_id) {
        this.ap_doc_ch_id = ap_doc_ch_id;
    }

    public String getAp_doc_mac() {
        return ap_doc_mac;
    }

    public void setAp_doc_mac(String ap_doc_mac) {
        this.ap_doc_mac = ap_doc_mac;
    }

    public String getAp_alarm_time() {
        return ap_alarm_time;
    }

    public void setAp_alarm_time(String ap_alarm_time) {
        this.ap_alarm_time = ap_alarm_time;
    }

    @Override
    public String toString() {
        return "ApptTableData{" +
                "_id=" + _id +
                ", ap_id='" + ap_id + '\'' +
                ", ap_time='" + ap_time + '\'' +
                ", ap_order='" + ap_order + '\'' +
                ", ap_doc_id='" + ap_doc_id + '\'' +
                ", ap_patient_id='" + ap_patient_id + '\'' +
                ", ap_ch_id='" + ap_ch_id + '\'' +
                ", ap_doc_ch_id='" + ap_doc_ch_id + '\'' +
                ", ap_doc_mac='" + ap_doc_mac + '\'' +
                ", ap_alarm_time='" + ap_alarm_time + '\'' +
                '}';
    }
}
