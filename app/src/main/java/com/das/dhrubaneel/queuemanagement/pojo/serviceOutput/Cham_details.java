package com.das.dhrubaneel.queuemanagement.pojo.serviceOutput;

import java.util.ArrayList;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class Cham_details {

    private ArrayList<Doc_details> doc_details;

    private String cham_location;

    private String cham_id;

    public ArrayList<Doc_details> getDoc_details() {
        return doc_details;
    }

    public void setDoc_details(ArrayList<Doc_details> doc_details) {
        this.doc_details = doc_details;
    }

    public String getCham_location() {
        return cham_location;
    }

    public void setCham_location(String cham_location) {
        this.cham_location = cham_location;
    }

    public String getCham_id() {
        return cham_id;
    }

    public void setCham_id(String cham_id) {
        this.cham_id = cham_id;
    }

    @Override
    public String toString() {
        return "Cham_details{" +
                "doc_details=" + doc_details +
                ", cham_location='" + cham_location + '\'' +
                ", cham_id='" + cham_id + '\'' +
                '}';
    }
}
