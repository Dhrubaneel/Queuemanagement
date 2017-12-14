package com.das.dhrubaneel.queuemanagement.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import com.das.dhrubaneel.queuemanagement.R;

/**
 * Created by Dhruba on 25-Feb-17.
 */
public class ForgotPasswordActivity extends Activity {
    private static Activity activity;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ForgotPasswordActivity.context = getApplicationContext();
        activity=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot_password);

    }
}
