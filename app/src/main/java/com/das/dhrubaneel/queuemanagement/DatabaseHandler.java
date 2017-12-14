package com.das.dhrubaneel.queuemanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.ApptTableData;

import java.util.ArrayList;

/**
 * Created by Dhruba on 21-Jan-17.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DatabaseHandler(Context context) {
        super(context, ConstantVariables.DATABASE_NAME, null, ConstantVariables.DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + ConstantVariables.TABLE_APPOINTMENT + "("
                + ConstantVariables.KEY_SLNO + " INTEGER PRIMARY KEY,"
                + ConstantVariables.KEY_APID + " TEXT,"
                + ConstantVariables.KEY_APTIME + " TEXT,"
                + ConstantVariables.KEY_APORDER + " TEXT,"
                + ConstantVariables.KEY_APDOCID + " TEXT,"
                + ConstantVariables.KEY_APPATIENTID + " TEXT,"
                + ConstantVariables.KEY_APCHAMID + " TEXT,"
                + ConstantVariables.KEY_APDOCCHAMID + " TEXT,"
                + ConstantVariables.KEY_APDOCMAC + " TEXT,"
                + ConstantVariables.KEY_ALARMTIME + " TEXT"
                + ")";
        Log.e("create_table",CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ConstantVariables.TABLE_APPOINTMENT);

        // Create tables again
        onCreate(db);
    }

    // Adding new appointment
    public void addAppointment(ApptTableData apptTableData){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ConstantVariables.KEY_APID, apptTableData.getAp_id()); // Appointment ID
        values.put(ConstantVariables.KEY_APTIME, apptTableData.getAp_time()); // Appointment Time
        values.put(ConstantVariables.KEY_APORDER, apptTableData.getAp_order()); // Appointment Order
        values.put(ConstantVariables.KEY_APDOCID, apptTableData.getAp_doc_id()); // Appointment Doctor ID
        values.put(ConstantVariables.KEY_APPATIENTID, apptTableData.getAp_patient_id()); // Appointment Patient ID
        values.put(ConstantVariables.KEY_APCHAMID, apptTableData.getAp_ch_id()); // Appointment Chamber ID
        values.put(ConstantVariables.KEY_APDOCCHAMID, apptTableData.getAp_doc_ch_id()); // Appointment Doc Cham ID
        values.put(ConstantVariables.KEY_APDOCMAC, apptTableData.getAp_doc_mac()); // Appointment Doctor Mac
        values.put(ConstantVariables.KEY_ALARMTIME, apptTableData.getAp_alarm_time()); // Appointment Alarm Time
        // Inserting Row
        db.insert(ConstantVariables.TABLE_APPOINTMENT, null, values);
        db.close(); // Closing database connection
    }

    // Getting single appointment
    public ApptTableData getAppt(String apptID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + ConstantVariables.TABLE_APPOINTMENT + " WHERE "
                + ConstantVariables.KEY_APID + " = " + apptID;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null)
            cursor.moveToFirst();

        ApptTableData objApptTableData = new ApptTableData();
        objApptTableData.set_id(Integer.parseInt(cursor.getString(0)));
        objApptTableData.setAp_id(cursor.getString(1));
        objApptTableData.setAp_time(cursor.getString(2));
        objApptTableData.setAp_order(cursor.getString(3));
        objApptTableData.setAp_doc_id(cursor.getString(4));
        objApptTableData.setAp_patient_id(cursor.getString(5));
        objApptTableData.setAp_ch_id(cursor.getString(6));
        objApptTableData.setAp_doc_ch_id(cursor.getString(7));
        objApptTableData.setAp_doc_mac(cursor.getString(8));
        objApptTableData.setAp_alarm_time(cursor.getString(9));

        return  objApptTableData;
    }

    //Getting all appointments
    public ArrayList<ApptTableData> getAllAppointments(){
        ArrayList<ApptTableData> apptList =new ArrayList<ApptTableData>();
        String selectQuery = "SELECT  * FROM " + ConstantVariables.TABLE_APPOINTMENT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                ApptTableData objApptTableData = new ApptTableData();
                objApptTableData.set_id(Integer.parseInt(cursor.getString(0)));
                objApptTableData.setAp_id(cursor.getString(1));
                objApptTableData.setAp_time(cursor.getString(2));
                objApptTableData.setAp_order(cursor.getString(3));
                objApptTableData.setAp_doc_id(cursor.getString(4));
                objApptTableData.setAp_patient_id(cursor.getString(5));
                objApptTableData.setAp_ch_id(cursor.getString(6));
                objApptTableData.setAp_doc_ch_id(cursor.getString(7));
                objApptTableData.setAp_doc_mac(cursor.getString(8));
                objApptTableData.setAp_alarm_time(cursor.getString(9));

                apptList.add(objApptTableData);
            }while (cursor.moveToNext());
        }

        return apptList;
    }

    //Delete an appointment
    public void deleteAppointment(String apptID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ConstantVariables.TABLE_APPOINTMENT, ConstantVariables.KEY_APID + " = ?",
                new String[] {apptID });
        db.close();
    }
}
