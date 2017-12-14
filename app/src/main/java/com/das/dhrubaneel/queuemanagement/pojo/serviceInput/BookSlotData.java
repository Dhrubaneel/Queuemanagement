package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class BookSlotData {
    private String apDate;
    private String apTime;
    private String apStatus;
    private String apOrder;
    private String apDocId;
    private String apPatientId;
    private String apChId;
    private String apDocChId;

    public String getApDate() {
        return apDate;
    }

    public void setApDate(String apDate) {
        this.apDate = apDate;
    }

    public String getApTime() {
        return apTime;
    }

    public void setApTime(String apTime) {
        this.apTime = apTime;
    }

    public String getApStatus() {
        return apStatus;
    }

    public void setApStatus(String apStatus) {
        this.apStatus = apStatus;
    }

    public String getApOrder() {
        return apOrder;
    }

    public void setApOrder(String apOrder) {
        this.apOrder = apOrder;
    }

    public String getApDocId() {
        return apDocId;
    }

    public void setApDocId(String apDocId) {
        this.apDocId = apDocId;
    }

    public String getApPatientId() {
        return apPatientId;
    }

    public void setApPatientId(String apPatientId) {
        this.apPatientId = apPatientId;
    }

    public String getApChId() {
        return apChId;
    }

    public void setApChId(String apChId) {
        this.apChId = apChId;
    }

    public String getApDocChId() {
        return apDocChId;
    }

    public void setApDocChId(String apDocChId) {
        this.apDocChId = apDocChId;
    }

    @Override
    public String toString() {
        return "BookSlotData{" +
                "apDate='" + apDate + '\'' +
                ", apTime='" + apTime + '\'' +
                ", apStatus='" + apStatus + '\'' +
                ", apOrder='" + apOrder + '\'' +
                ", apDocId='" + apDocId + '\'' +
                ", apPatientId='" + apPatientId + '\'' +
                ", apChId='" + apChId + '\'' +
                ", apDocChId='" + apDocChId + '\'' +
                '}';
    }
}
