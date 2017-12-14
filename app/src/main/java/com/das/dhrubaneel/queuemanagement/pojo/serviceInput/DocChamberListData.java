package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class DocChamberListData {
    private String visitDay;

    public String getVisitDay() {
        return visitDay;
    }

    public void setVisitDay(String visitDay) {
        this.visitDay = visitDay;
    }

    @Override
    public String toString() {
        return "DocChamberListData{" +
                "visitDay='" + visitDay + '\'' +
                '}';
    }
}
