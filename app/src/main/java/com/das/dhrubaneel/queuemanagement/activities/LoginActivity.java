package com.das.dhrubaneel.queuemanagement.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.das.dhrubaneel.queuemanagement.DatabaseHandler;
import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskCompleted;
import com.das.dhrubaneel.queuemanagement.listners.OnTaskError;
import com.das.dhrubaneel.queuemanagement.networkRequest.ServiceCall;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.ApptTableData;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.Login;
import com.das.dhrubaneel.queuemanagement.pojo.serviceInput.LoginData;
import com.das.dhrubaneel.queuemanagement.utilities.AlarmBroadcastReceiver;
import com.das.dhrubaneel.queuemanagement.utilities.FormValidation;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static FragmentActivity activity;
    private static Context context;
    private static EditText log_email;
    private static EditText password;
    private Button submit;
    private TextView registration;
    private TextView forgotPass;
    private final int REQUEST_LOCATION=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginActivity.context = getApplicationContext();
        activity=this;
        setContentView(R.layout.activity_login);

        //Check how many appointments are there
        setPendingAlarms();

        //Check the dangerous permissions are granted or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasLocationPermission= ContextCompat.checkSelfPermission(activity,Manifest.permission.ACCESS_COARSE_LOCATION);
            if(hasLocationPermission != PackageManager.PERMISSION_GRANTED){
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                   ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION);

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_LOCATION);
                }
            }else {
                // got permission use it
                ConstantVariables.has_required_permission=null;
                ConstantVariables.has_required_permission=true;
            }
        } else {
           ConstantVariables.has_required_permission=null;
           ConstantVariables.has_required_permission=true;
        }

        log_email=(EditText)findViewById(R.id.log_email);
        password=(EditText)findViewById(R.id.log_pass);
        submit=(Button)findViewById(R.id.log_submit);
        registration=(TextView)findViewById(R.id.user_reg);
        forgotPass=(TextView)findViewById(R.id.fgt_pass);

        forgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i =new Intent(context, ForgotPasswordActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validLoginDetails()){
                    String logEmail=log_email.getText().toString();
                    String logpass=password.getText().toString();

                    Login objLogin=new Login();
                    LoginData objLoginData =new LoginData();
                    ServiceCall sc=new ServiceCall(activity);
                    objLogin.setService("userService");
                    objLogin.setMethod("userLogin");
                    objLoginData.setUserEmail(logEmail);
                    objLoginData.setUserPassword(logpass);
                    objLogin.setData(objLoginData);

                    Gson gson=new Gson();
                    String jsonStr = gson.toJson(objLogin);
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        Log.e("request", json.toString());
                        sc.loadFromNetwork("login_req",
                                Request.Method.POST,
                                ConstantVariables.totalURL,
                                json,
                                new OnTaskCompleted() {
                                    @Override
                                    public void onTaskCompleted(JSONObject result) {
                                        onLoginSuccess(result);
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
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ConstantVariables.has_required_permission=null;
                    ConstantVariables.has_required_permission=true;

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    ConstantVariables.has_required_permission=null;
                    ConstantVariables.has_required_permission=false;
                    activity.finish();
                    Toast.makeText(context,"Location permission required to run the application",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    public static boolean validLoginDetails(){
        boolean boolResult;

        boolean loginEmail= FormValidation.hasText(log_email);
        boolean loginPass= FormValidation.hasText(password);

        boolResult=loginEmail&&loginPass;

        return boolResult;
    }

    public static void onLoginSuccess(JSONObject response){

        try{
            if(response.getString("errorCode").equals("0")){

                ConstantVariables.login_token=response.getString("token");

                JSONObject data=response.getJSONObject("data");

                ConstantVariables.user_id=data.getString("user_id");
                ConstantVariables.user_email=data.getString("user_email");
                ConstantVariables.user_type=data.getString("user_type");
                ConstantVariables.user_name=data.getString("user_name");

                Intent i =new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }else{
                Toast.makeText(context,response.getString("message"),Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //hiding soft keyboard
    public boolean dispatchTouchEvent(MotionEvent event){

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }

        return ret;

    }

    //Function to set pending alarms if any
    public void setPendingAlarms(){
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

    }



}
