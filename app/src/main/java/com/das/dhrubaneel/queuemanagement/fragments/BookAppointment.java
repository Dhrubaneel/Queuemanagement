package com.das.dhrubaneel.queuemanagement.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.activities.MainActivity;
import com.das.dhrubaneel.queuemanagement.adapters.BookApptOuterAdapter;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.DocChamberList;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.DocChamberListData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Cham_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Doc_details;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.Slot;
import com.das.dhrubaneel.queuemanagement.utilities.DatePickerFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dhruba on 08-Jan-17.
 */
public class BookAppointment extends Fragment {

    private static View v;
    private static FragmentActivity activity;
    private static TextView fragmentHeading;
    private static Context context;
    private static ListView chamberDoctorList;
    private static EditText datepicker;
    private static String selectedDate;
    private static RelativeLayout listloader;
    private static TextView noDataMsg;
    private static EditText searchByDoc;
    private static String SEARCH_TEXT = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.book_appointment, container, false);
        activity=(FragmentActivity)getActivity();
        context = activity.getApplicationContext();

        chamberDoctorList = (ListView) v.findViewById(R.id.doctor_chember_list);
        listloader = (RelativeLayout) v.findViewById(R.id.book_appt_list_loader);
        noDataMsg = (TextView) v.findViewById(R.id.book_appt_noDataMessage);
        searchByDoc =(EditText)v.findViewById(R.id.doctor_search);

        //set fragmet header
        fragmentHeading=(TextView)v.findViewById(R.id.bk_appt_heading);
        fragmentHeading.setText("Book Appointment");

        //set current date to the edittext
        datepicker=(EditText)v.findViewById(R.id.visit_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        datepicker.setText(formattedDate);

        //set the datepicker
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        try{
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            Date dt1=format1.parse(datepicker.getText().toString());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            String dayOfTheWeek = sdf.format(dt1);
            //call service to get initial chamber doctor details
            getChamberDoctorList(dayOfTheWeek);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        searchByDoc.setText("");
        searchByDoc.addTextChangedListener(watch);

    }

    private final TextWatcher watch = new TextWatcher(){
        private Timer timer=new Timer();

        @Override
        public void afterTextChanged(Editable arg0) {

            timer.cancel();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SEARCH_TEXT=searchByDoc.getText().toString();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(SEARCH_TEXT.length()>=3 || SEARCH_TEXT.length() == 0)
                            {
                                if(SEARCH_TEXT.length()>=3){
                                    //call doctor list filtered by doctor name
                                    try{
                                        SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                                        Date dt1=format1.parse(datepicker.getText().toString());
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                        String dayOfTheWeek = sdf.format(dt1);
                                        getChamberDoctorListFilterDoctor(dayOfTheWeek,SEARCH_TEXT);
                                    }catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }else if(SEARCH_TEXT.length() == 0){
                                    //call normal doctor list
                                    try{
                                        SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                                        Date dt1=format1.parse(datepicker.getText().toString());
                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                        String dayOfTheWeek = sdf.format(dt1);
                                        getChamberDoctorList(dayOfTheWeek);
                                    }catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }

                        }
                    });
                }

            }, ConstantVariables.DELAY);


            //Log.e("after change","after changing the text");

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence s, int a, int b, int c) {
        }


    };

    // date picker function
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up edittext Date Into dialog
         */
        try{
            SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
            Date dt1=format1.parse(datepicker.getText().toString());
            Calendar calender = Calendar.getInstance();
            calender.setTime(dt1);
            Bundle args = new Bundle();
            args.putInt("year", calender.get(Calendar.YEAR));
            args.putInt("month", calender.get(Calendar.MONTH));
            args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            date.setArguments(args);
        }catch (ParseException e) {
            e.printStackTrace();
        }


        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(activity.getSupportFragmentManager(), "Date Picker");
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String dayOfMonthString=String.valueOf(dayOfMonth);
            String monthOfYearString=String.valueOf(monthOfYear+1);
            String yearString=String.valueOf(year);
            if(dayOfMonth<10){
                dayOfMonthString="0"+dayOfMonthString;
            }
            if(monthOfYear<9){
                monthOfYearString="0"+monthOfYearString;
            }
            datepicker.setText(dayOfMonthString+"-"+monthOfYearString+"-"+yearString);

            try{
                SimpleDateFormat format1=new SimpleDateFormat("dd-MM-yyyy");
                Date dt1=format1.parse(dayOfMonthString+"-"+monthOfYearString+"-"+yearString);
                DateFormat format2=new SimpleDateFormat("EEEE");
                String day=format2.format(dt1);

                //call the new list of doctors for updated day
                getChamberDoctorList(day);
            }catch (ParseException e) {
                e.printStackTrace();
            }


        }
    };

    public static void getChamberDoctorList(String dayOfTheWeek){
        DocChamberList objDocChamberList =new DocChamberList();
        DocChamberListData objDocChamberListData =new DocChamberListData();
        ServiceCall sc=new ServiceCall(activity);
        objDocChamberList.setService("docClinicDetailService");
        objDocChamberList.setMethod("chamberDetailsByDay");
        objDocChamberListData.setVisitDay(dayOfTheWeek);
        objDocChamberList.setData(objDocChamberListData);
        Gson gson=new Gson();
        String jsonStr = gson.toJson(objDocChamberList);
        try {
            JSONObject json = new JSONObject(jsonStr);
            Log.e("request", json.toString());

            listloader.setVisibility(View.VISIBLE);
            noDataMsg.setVisibility(View.GONE);

            sc.loadFromNetworkWithAuth("cham_doc_req",
                    Request.Method.POST,
                    ConstantVariables.totalURL,
                    json,
                    new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(JSONObject result) {
                            onDocChamberSuccess(result);
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

    public static void getChamberDoctorListFilterDoctor(String dayOfTheWeek, String searchText){
        DocChamberList objDocChamberList =new DocChamberList();
        DocChamberListData objDocChamberListData =new DocChamberListData();
        ServiceCall sc=new ServiceCall(activity);
        objDocChamberList.setService("docClinicDetailService");
        objDocChamberList.setMethod("chamberDetailsByDay");
        objDocChamberListData.setVisitDay(dayOfTheWeek);
        objDocChamberList.setData(objDocChamberListData);
        Gson gson=new Gson();
        String jsonStr = gson.toJson(objDocChamberList);
        try {
            JSONObject json = new JSONObject(jsonStr);
            Log.e("request", json.toString());

            listloader.setVisibility(View.VISIBLE);
            noDataMsg.setVisibility(View.GONE);

            sc.loadFromNetworkWithAuth("cham_doc_req",
                    Request.Method.POST,
                    ConstantVariables.totalURL,
                    json,
                    new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted(JSONObject result) {
                            onDocChamberSuccess(result);
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

    public static void onDocChamberSuccess(JSONObject response){
        String dateInString = datepicker.getText().toString();
        selectedDate=null;
        selectedDate=dateInString;

        try{
            if(response.getString("errorCode").equals("0")){
                //get response values
                JSONArray data=response.getJSONArray("data");
                if(data.length()>0){
                    for(int a=0;a < data.length(); a++){
                        JSONObject objJsonObj=data.getJSONObject(a);
                        JSONArray chamDetailsArray=objJsonObj.getJSONArray("cham_details");
                        ArrayList<Cham_details> Cham_details_Arr = new ArrayList<Cham_details>();
                        Cham_details_Arr.clear();
                        if(chamDetailsArray.length()>0) {
                            for(int i = 0; i < chamDetailsArray.length(); i++){
                                JSONObject chamDetails = chamDetailsArray.getJSONObject(i);
                                Cham_details cd=new Cham_details();
                                cd.setCham_id(chamDetails.getString("cham_id"));
                                cd.setCham_location(chamDetails.getString("cham_location"));

                                JSONArray docDetailsArray=chamDetails.getJSONArray("doc_details");
                                ArrayList<Doc_details> Doc_details_Arr = new ArrayList<Doc_details>();
                                Doc_details_Arr.clear();

                                if(docDetailsArray.length()>0){
                                    for(int j=0;j<docDetailsArray.length();j++){
                                        JSONObject docDetails = docDetailsArray.getJSONObject(j);
                                        Doc_details dd=new Doc_details();
                                        dd.setUser_id(docDetails.getString("user_id"));
                                        dd.setUser_name(docDetails.getString("user_name"));
                                        dd.setUser_email(docDetails.getString("user_email"));
                                        dd.setUser_contact_no(docDetails.getString("user_contact_no"));
                                        dd.setUser_age(docDetails.getString("user_age"));
                                        dd.setUser_gender(docDetails.getString("user_gender"));
                                        dd.setUser_doc_specialization(docDetails.getString("user_doc_specialization"));
                                        dd.setUser_doc_degree(docDetails.getString("user_doc_degree"));
                                        dd.setUser_doc_reg_no(docDetails.getString("user_doc_reg_no"));

                                        JSONArray slotArray=docDetails.getJSONArray("slot");
                                        ArrayList<Slot> slot_Array=new ArrayList<Slot>();
                                        slot_Array.clear();
                                        if(slotArray.length()>0){
                                            for(int k=0;k<slotArray.length();k++){
                                                JSONObject slot=slotArray.getJSONObject(k);
                                                Gson gson = new GsonBuilder().create();
                                                Slot s=gson.fromJson(slot.toString(), Slot.class);
                                                slot_Array.add(s);
                                            }
                                        }
                                        dd.setSlot(slot_Array);

                                        Doc_details_Arr.add(dd);
                                    }
                                }
                                cd.setDoc_details(Doc_details_Arr);

                                Cham_details_Arr.add(cd);

                            }

                            Resources res = activity.getResources();
                            chamberDoctorList.setVisibility(View.VISIBLE);
                            listloader.setVisibility(View.GONE);
                            noDataMsg.setVisibility(View.GONE);

                            BookApptOuterAdapter adapter=new BookApptOuterAdapter(activity,Cham_details_Arr,res,selectedDate);
                            chamberDoctorList.setAdapter(adapter);
                        }else{
                            chamberDoctorList.setVisibility(View.GONE);
                            listloader.setVisibility(View.GONE);
                            noDataMsg.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    chamberDoctorList.setVisibility(View.GONE);
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
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
