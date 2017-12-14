package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class MyAppointmentsData {
    private String patientId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "MyAppointmentsData{" +
                "patientId='" + patientId + '\'' +
                '}';
    }
}
