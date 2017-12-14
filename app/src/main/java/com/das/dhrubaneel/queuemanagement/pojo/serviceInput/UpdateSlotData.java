package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 15-Jan-17.
 */
public class UpdateSlotData {
    private String apId;
    private String apStatus;
    private String apReportingTime;

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getApStatus() {
        return apStatus;
    }

    public void setApStatus(String apStatus) {
        this.apStatus = apStatus;
    }

    public String getApReportingTime() {
        return apReportingTime;
    }

    public void setApReportingTime(String apReportingTime) {
        this.apReportingTime = apReportingTime;
    }

    @Override
    public String toString() {
        return "UpdateSlotData{" +
                "apId='" + apId + '\'' +
                ", apStatus='" + apStatus + '\'' +
                ", apReportingTime='" + apReportingTime + '\'' +
                '}';
    }
}
