package com.das.dhrubaneel.queuemanagement.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.activities.MainActivity;
import com.das.dhrubaneel.queuemanagement.adapters.ApptHistoryAdapter;
import com.das.dhrubaneel.queuemanagement.adapters.SlotsAdapter;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.MyAppointments;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.MyAppointmentsData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.All_Appointments;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Available_Slot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class PatientMyBookings extends Fragment {

    private static View v;
    private static FragmentActivity activity;
    private static TextView fragmentHeading;
    private static Context context;
    private static ListView allApointments;
    private static RelativeLayout listloader;
    private static TextView noDataMsg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.patient_my_booking, container, false);
        activity=(FragmentActivity)getActivity();
        activity=(FragmentActivity)getActivity();
        context = activity.getApplicationContext();

        allApointments=(ListView)v.findViewById(R.id.patient_appt_list);
        listloader = (RelativeLayout) v.findViewById(R.id.patient_appt_list_loader);
        noDataMsg = (TextView) v.findViewById(R.id.patient_appt_noDataMessage);


        //set fragmet header
        fragmentHeading=(TextView)v.findViewById(R.id.bk_history_heading);
        fragmentHeading.setText("Appointment History");

        MyAppointments objMyAppointments = new MyAppointments();
        MyAppointmentsData objMyAppointmentsData = new MyAppointmentsData();
        ServiceCall sc = new ServiceCall(activity);

        objMyAppointments.setService("appointmentService");
        objMyAppointments.setMethod("getMyBookingDetails");

        objMyAppointmentsData.setPatientId(ConstantVariables.user_id);

        objMyAppointments.setData(objMyAppointmentsData);

        Gson gson=new Gson();
        String jsonStr = gson.toJson(objMyAppointments);
        try {
            JSONObject json = new JSONObject(jsonStr);
            Log.e("request", json.toString());

            listloader.setVisibility(View.VISIBLE);
            noDataMsg.setVisibility(View.GONE);

            sc.loadFromNetworkWithAuth("appt_history_req",
                    Request.Method.POST,
                    ConstantVariables.totalURL,
                    json,
                    new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(JSONObject result) {
                            ongetApptHistorySuccess(result);
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

        return v;
    }

    public static void ongetApptHistorySuccess(JSONObject response){

        try{
            if(response.getString("errorCode").equals("0")){
                //get response values
                JSONArray data=response.getJSONArray("data");
                if(data.length()>0){
                    ArrayList<All_Appointments> all_appt_arr=new ArrayList<All_Appointments>();
                    all_appt_arr.clear();
                    for(int i=0;i < data.length(); i++){
                        JSONObject objJsonObj=data.getJSONObject(i);
                        Gson gson = new GsonBuilder().create();
                        All_Appointments objAll_Appointments=gson.fromJson(objJsonObj.toString(), All_Appointments.class);
                        all_appt_arr.add(objAll_Appointments);
                    }
                    Resources res = activity.getResources();
                    allApointments.setVisibility(View.VISIBLE);
                    listloader.setVisibility(View.GONE);
                    noDataMsg.setVisibility(View.GONE);
                    ApptHistoryAdapter adapter=new ApptHistoryAdapter(activity,all_appt_arr,res);
                    allApointments.setAdapter(adapter);

                }else{
                    allApointments.setVisibility(View.GONE);
                    listloader.setVisibility(View.GONE);
                    noDataMsg.setVisibility(View.VISIBLE);
                }
            }else{
                listloader.setVisibility(View.GONE);
                noDataMsg.setVisibility(View.GONE);
                if(response.getString("message").contains("Access validity expired")){
                    MainActivity.logout();
                }
                Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
