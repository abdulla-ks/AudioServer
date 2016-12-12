package com.acsia.server.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.acsia.server.R;
import com.acsia.server.aidl.AudioService;
import com.acsia.server.support.ServerApplication;
import com.acsia.server.thrift.ThriftService;

public class MainActivity extends RuntimePermissionsActivity {

    private static final int REQUEST_PERMISSIONS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.super.requestAppPermissions(new
                        String[]{Manifest.permission.INTERNET,
                        Manifest.permission.RECEIVE_BOOT_COMPLETED,
                        Manifest.permission.WAKE_LOCK}, R.string
                        .runtime_permissions_txt
                , REQUEST_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(final int requestCode) {
//        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
        ServerApplication.getInstance().startServer();

        finish();
    }


}