package com.aditya.classquiz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ResetStepsOnDateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_TIME_CHANGED.equals(intent.getAction())) {
            Log.d("DateChangeReceiver", "Date has changed");
            resetStepCount(context);
        }
    }

    private void resetStepCount(Context context){

        FitBitDatabaseHelper dbHelper = new FitBitDatabaseHelper(context);
        dbHelper.resetStepCount();
    }
}
