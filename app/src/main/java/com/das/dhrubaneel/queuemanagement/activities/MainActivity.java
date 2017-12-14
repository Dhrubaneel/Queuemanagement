package com.das.dhrubaneel.queuemanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.das.dhrubaneel.queuemanagement.R;
import com.das.dhrubaneel.queuemanagement.constants.ConstantVariables;
import com.das.dhrubaneel.queuemanagement.fragments.PatientLandingFragment;

public class MainActivity extends AppCompatActivity {

    private TextView userLoginName;
    private ImageView userProfile;
    private ImageView logout;
    private static FragmentActivity activity;
    private static Context context;
    private boolean doubleBackToExitPressedOnce = false;
    private Toast mExitToast=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = getApplicationContext();
        activity=this;
        setContentView(R.layout.activity_main);

        mExitToast=Toast.makeText(getApplicationContext(), "Double tap back button to logout.", Toast.LENGTH_SHORT);

        //Code to add custom actionbar
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_action_bar_layout, null);
        actionBar.setCustomView(mCustomView);

        //set user name
        userLoginName=(TextView)findViewById(R.id.user_name);
        userLoginName.setText(ConstantVariables.user_name);

        //user profile onclick listner
        userProfile=(ImageView)findViewById(R.id.user_profile);
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the fragment for user profile
            }
        });

        //logout button onclick listner
        logout=(ImageView)findViewById(R.id.app_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the logout function
                logout();
            }
        });

        //Load the fragment based on role
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            if(ConstantVariables.user_type.equals("Patient")){
                PatientLandingFragment objPatientLandingFragment=new PatientLandingFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, objPatientLandingFragment).commit();

            }else if(ConstantVariables.user_type.equals("Doctor")){

            }
        }


    }

    //Executes once the activity resumes
    @Override
    protected void onResume() {
        super.onResume();
        // handle the reset state when the activity is resumed
        this.doubleBackToExitPressedOnce = false;
    }

    //if back button clicked twice then app should logout
    @Override
    public void onBackPressed() {

        int count = activity.getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            if (doubleBackToExitPressedOnce) {

                mExitToast.cancel();
                logout();
            }
            this.doubleBackToExitPressedOnce = true;
            mExitToast.show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            activity.getSupportFragmentManager().popBackStack();
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

    //logout user
    public static void logout(){

        //Make all values default

        ConstantVariables objConstantVariables=new ConstantVariables();
        objConstantVariables.makeDefaultGlobalConstants();

        Intent i =new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        Toast.makeText(context,"You are Logged Out",Toast.LENGTH_LONG).show();
    }
}
