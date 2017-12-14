package com.das.dhrubaneel.queuemanagement.pojo.serviceInput;

/**
 * Created by Dhruba on 13-Jan-17.
 */
public class SlotListData {
    private String docChamId;
    private String bookingDate;

    public String getDocChamId() {
        return docChamId;
    }

    public void setDocChamId(String docChamId) {
        this.docChamId = docChamId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public String toString() {
        return "SlotListData{" +
                "docChamId='" + docChamId + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                '}';
    }
}
