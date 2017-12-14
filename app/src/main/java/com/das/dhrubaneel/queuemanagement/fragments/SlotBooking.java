package com.das.dhrubaneel.queuemanagement.fragments;


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
import com.das.dhrubaneel.queuemanagement.adapters.SlotsAdapter;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.SlotList;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.SlotListData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Available_Slot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dhruba on 12-Jan-17.
 */
public class SlotBooking extends Fragment {
    private static View v;
    private static FragmentActivity activity;
    private static TextView fragmentHeading;
    private static TextView selectedDoctor;
    private static TextView selectedChaber;
    private static TextView selectedDate;
    private static ListView slotlist;
    private static Context context;
    private static RelativeLayout listloader;
    private static TextView noDataMsg;

    private static String cham_id;
    private static String cham_location;
    private static String doctor_name;
    private static String doctor_id;
    private static String d_c_id;
    private static String cham_mac_address;
    private static String booking_date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v= inflater.inflate(R.layout.slot_booking, container, false);
        activity=(FragmentActivity)getActivity();
        context = activity.getApplicationContext();

        slotlist = (ListView) v.findViewById(R.id.doctor_chember_slot);
        listloader = (RelativeLayout) v.findViewById(R.id.book_slot_list_loader);
        noDataMsg = (TextView) v.findViewById(R.id.book_slot_noDataMessage);

        //get values from bundle
        Bundle bundle = this.getArguments();
        cham_id = bundle.getString("cham_id");
        cham_location = bundle.getString("cham_location");
        doctor_name = bundle.getString("doctor_name");
        doctor_id = bundle.getString("doctor_id");
        d_c_id = bundle.getString("d_c_id");
        cham_mac_address = bundle.getString("cham_mac_address");
        booking_date = bundle.getString("booking_date");

        //set fragmet header
        fragmentHeading=(TextView)v.findViewById(R.id.slt_bk_heading);
        fragmentHeading.setText("Book Slot");

        //set doctor name, chamber name and date
        selectedDoctor=(TextView)v.findViewById(R.id.selected_doc);
        selectedChaber=(TextView)v.findViewById(R.id.selected_cham);
        selectedDate=(TextView)v.findViewById(R.id.selected_date);

        selectedDoctor.setText(doctor_name);
        selectedChaber.setText(cham_location);
        selectedDate.setText(booking_date);

        getAvailableSlots();

        return v;
    }

    public static void getAvailableSlots(){
        //format the date in yyyy-mm-dd
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-mm-yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        String formatedDate = null;

        try {
            date = inputFormat.parse(booking_date);
            formatedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SlotList objSlotList = new SlotList();
        SlotListData objSlotListData = new SlotListData();
        ServiceCall sc=new ServiceCall(activity);
        objSlotList.setService("appointmentService");
        objSlotList.setMethod("getFutureBookingHistoryToBookSlot");
        objSlotListData.setDocChamId(d_c_id);
        objSlotListData.setBookingDate(formatedDate);
        objSlotList.setData(objSlotListData);
        Gson gson=new Gson();
        String jsonStr = gson.toJson(objSlotList);
        try {
            JSONObject json = new JSONObject(jsonStr);
            Log.e("request", json.toString());

            listloader.setVisibility(View.VISIBLE);
            noDataMsg.setVisibility(View.GONE);

            sc.loadFromNetworkWithAuth("doc_slot_req",
                    Request.Method.POST,
                    ConstantVariables.totalURL,
                    json,
                    new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(JSONObject result) {
                            onGetSlotSuccess(result);
                        }
                    },new OnTaskError() {
                        @Override
                        public void onTaskError(String result) {
                            listloader.setVisibility(View.GONE);
                            noDataMsg.setVisibility(View.GONE);
                            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onGetSlotSuccess(JSONObject response){

        try {
            if(response.getString("errorCode").equals("0")){
                //get response values
                JSONArray data=response.getJSONArray("data");
                if(data.length()>0){
                    ArrayList<Available_Slot> av_slot_arr=new ArrayList<Available_Slot>();
                    av_slot_arr.clear();
                    for(int i=0;i < data.length(); i++){
                        JSONObject objJsonObj=data.getJSONObject(i);
                        Gson gson = new GsonBuilder().create();
                        Available_Slot objAvailableSlot=gson.fromJson(objJsonObj.toString(), Available_Slot.class);
                        av_slot_arr.add(objAvailableSlot);
                    }
                    Resources res = activity.getResources();
                    slotlist.setVisibility(View.VISIBLE);
                    listloader.setVisibility(View.GONE);
                    noDataMsg.setVisibility(View.GONE);
                    SlotsAdapter adapter=new SlotsAdapter(activity,av_slot_arr,res,booking_date,doctor_id,cham_id,d_c_id,cham_mac_address,doctor_name,cham_location);
                    slotlist.setAdapter(adapter);

                }else{
                    slotlist.setVisibility(View.GONE);
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
