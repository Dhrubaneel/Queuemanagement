package com.das.dhrubaneel.queuemanagement.fragments;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.DatabaseHandler;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.activities.MainActivity;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.ApptTableData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.BookSlot;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.BookSlotData;
import com.das.dhrubaneel.queuemanagement.utilities.AlarmBroadcastReceiver;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dhruba on 14-Jan-17.
 */
public class FinalizeBooking extends DialogFragment {
    private static View v;
    private static FragmentActivity activity;
    private static Resources res;
    private static TextView fragmentHeading, doctorName, chamberName, visitingDate, visitingTime;
    private static Button cnfBooking,cancelBtn;
    private static Context context;
    private String apDate,apTime,apOrder,apDocId,apDocName,apDocChamMac,chambarLocation,apChId,apDocChId,alarmTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.finalize_booking, container, false);
        activity=(FragmentActivity)getActivity();
        context = activity.getApplicationContext();
        res = activity.getResources();


        //get values from bundle
        Bundle bundle = this.getArguments();
        apDate = bundle.getString("apDate");
        apTime = bundle.getString("apTime");
        apOrder = bundle.getString("apOrder");
        apDocId = bundle.getString("apDocId");
        apDocName = bundle.getString("apDocName");
        apDocChamMac = bundle.getString("apDocChamMac");
        chambarLocation = bundle.getString("chambarLocation");
        apChId = bundle.getString("apChId");
        apDocChId = bundle.getString("apDocChId");

        //set the fragment heading
        fragmentHeading=(TextView) v.findViewById(R.id.final_appt_heading);
        fragmentHeading.setText("Finalize Booking");

        //set data for booking confirmation
        doctorName=(TextView)v.findViewById(R.id.visiting_doc);
        chamberName=(TextView)v.findViewById(R.id.visiting_cham);
        visitingDate=(TextView)v.findViewById(R.id.visiting_date);
        visitingTime=(TextView)v.findViewById(R.id.visiting_time);

        doctorName.setText(apDocName);
        chamberName.setText(chambarLocation);
        visitingDate.setText(apDate);
        String[] parts=apTime.split(" ");
        String lastPart=parts[parts.length-1];
        visitingTime.setText(lastPart);

        //assign button actions
        cnfBooking=(Button)v.findViewById(R.id.cnf_booking);
        cancelBtn=(Button)v.findViewById(R.id.can_booking);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close the dialogue
                getDialog().dismiss();
            }
        });

        cnfBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //make service call to register patient
                BookSlot objBookSlot=new BookSlot();
                BookSlotData objBookSlotData=new BookSlotData();
                ServiceCall sc=new ServiceCall(activity);
                objBookSlot.setService("appointmentService");
                objBookSlot.setMethod("slotBooking");
                String[] parts=apTime.split(" ");
                String firstPart=parts[0];
                objBookSlotData.setApDate(firstPart);
                objBookSlotData.setApTime(apTime);
                objBookSlotData.setApStatus("booked");
                objBookSlotData.setApOrder(apOrder);
                objBookSlotData.setApDocId(apDocId);
                objBookSlotData.setApPatientId(ConstantVariables.user_id);
                objBookSlotData.setApChId(apChId);
                objBookSlotData.setApDocChId(apDocChId);
                objBookSlot.setData(objBookSlotData);
                Gson gson=new Gson();
                String jsonStr = gson.toJson(objBookSlot);
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    Log.e("request", json.toString());
                    sc.loadFromNetworkWithAuth("cham_bookSlot_req",
                            Request.Method.POST,
                            ConstantVariables.totalURL,
                            json,
                            new OnTaskCompleted() {
                                @Override
                                public void onTaskCompleted(JSONObject result) {
                                    onRegistrationSuccess(result);
                                }
                            },new OnTaskError() {
                                @Override
                                public void onTaskError(String result) {
                                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                                }
                            });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        return v;
    }

    public void onRegistrationSuccess(JSONObject response){
        try{
            if(response.getString("errorCode").equals("0")){
                Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
                JSONObject data=response.getJSONObject("data");
                String apptId=data.getString("ap_id");

                //store all data in local database
                DatabaseHandler objDatabaseHandler=new DatabaseHandler(activity);

                //set alarm time
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = sdf.parse(apTime);
                    alarmTime=sdf.format(new Date(date.getTime()- TimeUnit.HOURS.toMillis(1)));

                }catch (ParseException e) {
                    e.printStackTrace();
                }

                if(ConstantVariables.has_required_permission){
                    Log.e("permission","We have location permission");
                    //Insert data in database
                    ApptTableData objATD=new ApptTableData();
                    objATD.setAp_id(apptId);
                    objATD.setAp_time(apTime);
                    objATD.setAp_order(apOrder);
                    objATD.setAp_doc_id(apDocId);
                    objATD.setAp_patient_id(ConstantVariables.user_id);
                    objATD.setAp_ch_id(apChId);
                    objATD.setAp_doc_ch_id(apDocChId);
                    objATD.setAp_doc_mac(apDocChamMac);
                    objATD.setAp_alarm_time(alarmTime);
                    objDatabaseHandler.addAppointment(objATD);

                    //set the alarm
                    Intent intent=new Intent(activity, AlarmBroadcastReceiver.class);
                    intent.putExtra("apptId",apptId);
                    intent.putExtra("apDocChamMac",apDocChamMac);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            activity.getApplicationContext(),Integer.parseInt(apptId) , intent, 0);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*15, pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
                    }
                }

                //Dismiss the dialog and go to previous page
                getDialog().dismiss();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }else{
                if(response.getString("message").contains("Access validity expired")){
                    MainActivity.logout();
                }
                Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

}
