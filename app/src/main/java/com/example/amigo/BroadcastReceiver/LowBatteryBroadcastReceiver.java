package com.example.amigo.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author Yohai Mazuz
 * This is a broadcast receiver for notifying the user about low battery event.
 */
public class LowBatteryBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_BATTERY_LOW.equals(intent.getAction()))
            Toast.makeText(context, "Battery is low, save changes", Toast.LENGTH_SHORT).show();
    }
}
