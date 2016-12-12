package com.acsia.server.support;

/**
 * Created by Acsia on 12/2/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.acsia.server.ui.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = BootReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive Starting service @ " + SystemClock.elapsedRealtime());
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equalsIgnoreCase(Constants.BROADCAST_INTENT)) {
            Log.i(TAG, "Starting service @ " + SystemClock.elapsedRealtime());
            startMainActivity();
        }
    }


    private void startMainActivity() {
        Intent intent = new Intent(ServerApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ServerApplication.getInstance().startActivity(intent);
    }

}