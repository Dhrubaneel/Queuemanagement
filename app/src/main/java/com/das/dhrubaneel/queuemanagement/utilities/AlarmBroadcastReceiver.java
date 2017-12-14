package com.das.dhrubaneel.queuemanagement.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.DatabaseHandler;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.UpdateSlot;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.UpdateSlotData;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Dhruba on 22-Jan-17.
 */
public class AlarmBroadcastReceiver extends BroadcastReceiver {
    private String apptId;
    private String apDocChamMac;
    private Boolean bluetoothStatus;
    Boolean deviceFound=false;
    private Context objcontext;
    private BroadcastReceiver mReceiver;
    @Override
    public void onReceive(Context context, Intent intent) {

        objcontext=context;

        Bundle extras = intent.getExtras();
        apptId = extras.getString("apptId");
        apDocChamMac = extras.getString("apDocChamMac");

        Log.e("alarm for",apptId);


        //Check bluetooth status and enable it if not enabled
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothStatus=mBluetoothAdapter.isEnabled();

        if(!bluetoothStatus){
            mBluetoothAdapter.enable();
        }

        //as mac address is known we are trying to connect directly
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(apDocChamMac.toUpperCase());
        Log.e("device_mac",device.toString());

        //Register bluetooth broadcast receiver
        mReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, Intent intent) {
                Log.e("Bluetooth","received");
                String action = intent.getAction();

                if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                    final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                    if (state == BluetoothAdapter.STATE_ON) {
                        Log.e("ACTION_STATE_CHANGED","STATE_ON");
                    }
                //If BT is just enabled then we need to search discovery again
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                   // mBluetoothAdapter.cancelDiscovery();
                    mBluetoothAdapter.startDiscovery();

                }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                    Log.e("BT","ACTION_DISCOVERY_STARTED");
                }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    Log.e("BT","ACTION_DISCOVERY_FINISHED");

                    //Unregister bluetooth broadcast receiver
                    context.getApplicationContext().unregisterReceiver(mReceiver);
                    //Disable bluetooth if it was in disable mode when the task started
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if(!bluetoothStatus){
                            mBluetoothAdapter.cancelDiscovery();
                            mBluetoothAdapter.disable();
                        }
                    //If device not found then run the process again
                    if(!deviceFound){
                        Log.e("Device","not found");
                        // set the alarm
                        Intent objIntent=new Intent(context, AlarmBroadcastReceiver.class);
                        objIntent.putExtra("apptId",apptId);
                        objIntent.putExtra("apDocChamMac",apDocChamMac);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                context.getApplicationContext(),Integer.parseInt(apptId) , objIntent, 0);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000*15, pendingIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000, pendingIntent);
                        }

                    }

                }

                else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    Log.e("Dhruba","found_device");
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    String deviceHardwareAddress = device.getAddress(); // MAC address
                    Log.e("Bluetooth mac",deviceHardwareAddress);
                    if(deviceHardwareAddress.equalsIgnoreCase(apDocChamMac)){
                        //call the service to update status
                        Log.e("DocMac","Matched");
                        UpdateSlot objUpdateSlot=new UpdateSlot();
                        UpdateSlotData objUpdateSlotData=new UpdateSlotData();
                        ServiceCall sc=new ServiceCall(context);

                        objUpdateSlot.setService("appointmentService");
                        objUpdateSlot.setMethod("slotUpdate");
                        objUpdateSlotData.setApId(apptId);
                        objUpdateSlotData.setApStatus("checked-in");
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
                            sc.loadFromNetwork("update_booking_req",
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
                                            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                                        }
                                    });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }else{
                        Log.e("DocMac","Not Matched");
                        deviceFound=false;
                    }

                }

            }
        };

        Log.e("Bluetooth","Intent started");
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.getApplicationContext().registerReceiver(mReceiver, filter);

        mBluetoothAdapter.cancelDiscovery();
        mBluetoothAdapter.startDiscovery();
    }


    public void onUpdateBookingSuccess(JSONObject response){
        try{
            if(response.getString("errorCode").equals("0")){
                deviceFound=true;
                //Remove the appointment from local database
                try{
                    DatabaseHandler db=new DatabaseHandler(objcontext.getApplicationContext());
                    db.deleteAppointment(apptId);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //Cancel all alarm for this appointment
                Log.e("Cancel Alarm for",apptId);
                Intent objIntent=new Intent(objcontext.getApplicationContext(), AlarmBroadcastReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        objcontext.getApplicationContext().getApplicationContext(),Integer.parseInt(apptId) , objIntent, 0);
                AlarmManager alarmManager = (AlarmManager) objcontext.getApplicationContext().getSystemService(objcontext.getApplicationContext().ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);

                //Make the notification that the attendance has been captured
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(objcontext);
                mBuilder.setSmallIcon(R.drawable.logo);
                NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
                bigTextStyle.setBigContentTitle("Appointment Update");
                bigTextStyle.bigText("You have successfully checked-in for today's appointment.");
                mBuilder.setStyle(bigTextStyle);

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);
                NotificationManager mNotificationManager = (NotificationManager) objcontext.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(Integer.parseInt(apptId), mBuilder.build());

            }else{
                Toast.makeText(objcontext,response.getString("message"),Toast.LENGTH_LONG).show();
                deviceFound=false;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
