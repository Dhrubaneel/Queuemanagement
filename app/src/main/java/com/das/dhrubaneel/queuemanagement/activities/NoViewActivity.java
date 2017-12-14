package com.das.dhrubaneel.queuemanagement.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.das.dhrubaneel.queuemanagement.DatabaseHandler;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.ApptTableData;
import com.das.dhrubaneel.queuemanagement.utilities.AlarmBroadcastReceiver;

import java.util.ArrayList;

/**
 * Created by Dhruba on 28-Jan-17.
 */
public class NoViewActivity extends AppCompatActivity {
    private static FragmentActivity activity;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NoViewActivity.context = getApplicationContext();
        activity=this;

        Log.e("Activity","No View Activity");

        //get all appointments
        DatabaseHandler odjDatabaseHandler = new DatabaseHandler(activity);
        ArrayList<ApptTableData> allAppts= odjDatabaseHandler.getAllAppointments();
        if(allAppts.size()>0){
            for(ApptTableData atd : allAppts){
                Log.e("ApptNo",atd.getAp_id());
                Intent intent=new Intent(activity, AlarmBroadcastReceiver.class);
                boolean alarmUp=(PendingIntent.getBroadcast(
                        activity.getApplicationContext(),Integer.parseInt(atd.getAp_id()) , intent, PendingIntent.FLAG_NO_CREATE) != null);
                if(alarmUp){
                    Log.e("alarm","already set for "+atd.getAp_id());
                }else{
                    Log.e("alarm","set alarm for "+ atd.getAp_id());
                    //set the alarm
                    intent.putExtra("apptId",atd.getAp_id());
                    intent.putExtra("apDocChamMac",atd.getAp_doc_mac());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            activity.getApplicationContext(),Integer.parseInt(atd.getAp_id()) , intent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*15, pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*15, pendingIntent);
                    }
                }
            }
        }else{
            Log.e("Appt","No pending appt");
        }

        //close the activity
        finish();
    }
}
