package com.das.dhrubaneel.queuemanagement.adapters;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.DatabaseHandler;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.activities.MainActivity;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.fragments.PatientMyBookings;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.ApptTableData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.UpdateSlot;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.UpdateSlotData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceOutput.All_Appointments;
import com.das.dhrubaneel.queuemanagement.utilities.AlarmBroadcastReceiver;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dhruba on 15-Jan-17.
 */
public class ApptHistoryAdapter extends BaseAdapter implements View.OnClickListener  {
    /*********** Declare Used Variables *********/
    private FragmentActivity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    public All_Appointments objAllAppointments=null;
    public String apptToCancel=null;

    /*************  CustomAdapter Constructor *****************/
    public ApptHistoryAdapter(Activity a, ArrayList d, Resources resLocal ) {


        /********** Take passed values **********/
        activity = (FragmentActivity)a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView apptDoc,apptCham,apptTime,apptStatus;
        public Button cancelBooking;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.bookings_adapter, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.apptDoc=(TextView) vi.findViewById(R.id.ah_docname);
            holder.apptCham=(TextView) vi.findViewById(R.id.ah_chamname);
            holder.apptTime=(TextView) vi.findViewById(R.id.ah_date);
            holder.apptStatus=(TextView) vi.findViewById(R.id.ah_status);
            holder.cancelBooking=(Button) vi.findViewById(R.id.cancel_appt);


            /************  Set holder with LayoutInflater ************/
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {
            //hide the list

        } else {

            /***** Get each Model object from ArrayList ********/
            objAllAppointments= null;
            objAllAppointments = (All_Appointments) data.get(position);

            /************  Set Model values in Holder elements ***********/
            holder.apptDoc.setText(objAllAppointments.getUser_name());
            holder.apptCham.setText(objAllAppointments.getCham_location());
            String[] parts=objAllAppointments.getAp_time().split(" ");
            String firstPart=parts[0];
            String lastPart=parts[parts.length-1];
            //format the date in yyyy-mm-dd
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-mm-yyyy");
            Date date = null;
            String formatedDate = null;

            try {
                date = inputFormat.parse(firstPart);
                formatedDate = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.apptTime.setText(formatedDate+" "+lastPart);
            holder.apptStatus.setText(objAllAppointments.getAp_status());
            if(!objAllAppointments.getAp_status().equals("booked")){
                holder.cancelBooking.setVisibility(View.GONE);
            }else{
                holder.cancelBooking.setVisibility(View.VISIBLE);
            }

        }

        holder.cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setTitle("Appointment Cancellation")
                        .setMessage("Do you want to cancel this appointment?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //service call to cancel appointment
                                All_Appointments objAllAppts= null;
                                objAllAppts = (All_Appointments) data.get(position);

                                apptToCancel=objAllAppts.getAp_id();

                                UpdateSlot objUpdateSlot=new UpdateSlot();
                                UpdateSlotData objUpdateSlotData=new UpdateSlotData();
                                ServiceCall sc=new ServiceCall(activity);

                                objUpdateSlot.setService("appointmentService");
                                objUpdateSlot.setMethod("slotUpdate");

                                objUpdateSlotData.setApId(objAllAppts.getAp_id());
                                objUpdateSlotData.setApStatus("cancelled");
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String formattedDate = df.format(c.getTime());
                                objUpdateSlotData.setApReportingTime(formattedDate);

                                objUpdateSlot.setData(objUpdateSlotData);

                                Gson gson=new Gson();
                                String jsonStr = gson.toJson(objUpdateSlot);
                                try {
                                    JSONObject json = new JSONObject(jsonStr);
                                    Log.e("request", json.toString());
                                    sc.loadFromNetworkWithAuth("update_booking_req",
                                            Request.Method.POST,
                                            ConstantVariables.totalURL,
                                            json,
                                            new OnTaskCompleted() {
                                                @Override
                                                public void onTaskCompleted(JSONObject result) {
                                                    onUpdateBookingSuccess(result);
                                                }
                                            },new OnTaskError() {
                                                @Override
                                                public void onTaskError(String result) {
                                                    Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                                                }
                                            });

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });




        return vi;
    }

    public void onUpdateBookingSuccess(JSONObject response){
        try{
            if(response.getString("errorCode").equals("0")){
                //Remove the appointment from local database
                DatabaseHandler db=new DatabaseHandler(activity);
                db.deleteAppointment(apptToCancel);
                //Cancel the alarm
                Intent objIntent=new Intent(activity.getApplicationContext(), AlarmBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        activity.getApplicationContext().getApplicationContext(),Integer.parseInt(apptToCancel) , objIntent, 0);
                AlarmManager alarmManager = (AlarmManager) activity.getApplicationContext().getSystemService(activity.getApplicationContext().ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                Toast.makeText(activity,response.getString("message"),Toast.LENGTH_LONG).show();

            }else{
                if(response.getString("message").contains("Access validity expired")){
                    MainActivity.logout();
                }
                Toast.makeText(activity,response.getString("message"),Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.v("Appt_history_Adapter", "=====Row button clicked=====");
    }





}
