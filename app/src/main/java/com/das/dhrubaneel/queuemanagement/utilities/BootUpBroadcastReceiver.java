package com.das.dhrubaneel.queuemanagement.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.das.dhrubaneel.queuemanagement.activities.NoViewActivity;

/**
 * Created by Dhruba on 28-Jan-17.
 */
public class BootUpBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("reboot","phone has been rebooted");
        Intent i =new Intent(context, NoViewActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
