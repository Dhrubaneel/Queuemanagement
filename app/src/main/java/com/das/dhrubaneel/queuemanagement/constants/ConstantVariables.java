package com.das.dhrubaneel.queuemanagement.constants;

/**
 * Created by Dhruba on 04-Jan-17.
 */
public class ConstantVariables {
    public static String serverIP="http://192.168.42.238:8282/";
    public static String serviceURL="eclipse_project/mediApp/index.php";
    //public static String totalURL= serverIP+serviceURL;
    public static String totalURL= "http://35.160.191.32/index.php";
    public static String login_token;
    public static String user_id;
    public static String user_email;
    public static String user_type;
    public static String user_name;
    public static Boolean has_required_permission;
    public static long DELAY=1000;

    //Database Details
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="mediApp";
    public static final String TABLE_APPOINTMENT="tableAppointment";

    //tableAppointment table structure
    public static final String KEY_SLNO="sl_no";
    public static final String KEY_APID="ap_id";
    public static final String KEY_APTIME="ap_time";
    public static final String KEY_APORDER="ap_order";
    public static final String KEY_APDOCID="ap_doc_id";
    public static final String KEY_APPATIENTID="ap_patient_id";
    public static final String KEY_APCHAMID="ap_ch_id";
    public static final String KEY_APDOCCHAMID="ap_doc_ch_id";
    public static final String KEY_APDOCMAC="ap_doc_mac";
    public static final String KEY_ALARMTIME="ap_alarm_time";

    //clear all global constants
    public void makeDefaultGlobalConstants(){
        login_token=null;
        user_id=null;
        user_email=null;
        user_type=null;
        user_name=null;
        has_required_permission=null;
    }
}
