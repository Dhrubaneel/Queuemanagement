package com.das.dhrubaneel.queuemanagement.pojo.serviceOutput;

import java.util.ArrayList;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class Doc_details {
    private String user_gender;

    private String user_name;

    private String user_doc_specialization;

    private String user_doc_degree;

    private String user_doc_reg_no;

    private String user_id;

    private ArrayList<Slot> slot;

    private String user_contact_no;

    private String user_email;

    private String user_age;

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_doc_specialization() {
        return user_doc_specialization;
    }

    public void setUser_doc_specialization(String user_doc_specialization) {
        this.user_doc_specialization = user_doc_specialization;
    }

    public String getUser_doc_degree() {
        return user_doc_degree;
    }

    public void setUser_doc_degree(String user_doc_degree) {
        this.user_doc_degree = user_doc_degree;
    }

    public String getUser_doc_reg_no() {
        return user_doc_reg_no;
    }

    public void setUser_doc_reg_no(String user_doc_reg_no) {
        this.user_doc_reg_no = user_doc_reg_no;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Slot> getSlot() {
        return slot;
    }

    public void setSlot(ArrayList<Slot> slot) {
        this.slot = slot;
    }

    public String getUser_contact_no() {
        return user_contact_no;
    }

    public void setUser_contact_no(String user_contact_no) {
        this.user_contact_no = user_contact_no;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_age() {
        return user_age;
    }

    public void setUser_age(String user_age) {
        this.user_age = user_age;
    }

    @Override
    public String toString() {
        return "Doc_details{" +
                "user_gender='" + user_gender + '\'' +
                ", user_name='" + user_name + '\'' +
                ", user_doc_specialization='" + user_doc_specialization + '\'' +
                ", user_doc_degree='" + user_doc_degree + '\'' +
                ", user_doc_reg_no='" + user_doc_reg_no + '\'' +
                ", user_id='" + user_id + '\'' +
                ", slot=" + slot +
                ", user_contact_no='" + user_contact_no + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_age='" + user_age + '\'' +
                '}';
    }
}
