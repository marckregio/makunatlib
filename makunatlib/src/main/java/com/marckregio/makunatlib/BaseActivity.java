package com.marckregio.makunatlib;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.marckregio.makunatlib.Permissions;

/**
 * Created by makregio on 01/02/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public BroadcastReceiver broadcastReceiver, downloadReceiver;
    public Permissions permissions;

    public Tracker tracker;

    synchronized public Tracker getDefaultTracker(){
        if (tracker == null){
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissions = new Permissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(getApplicationContext().getResources().getString(R.string.broadcast_refresh)));
        registerReceiver(downloadReceiver, new IntentFilter(getApplicationContext().getResources().getString(R.string.broadcast_download)));
    }

    @Override
    protected void onStart() {
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }

        if (downloadReceiver != null) {
            unregisterReceiver(downloadReceiver);
        }

        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onDestroy();
    }

    public void showAlert(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0] != Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] != PackageManager.PERMISSION_GRANTED){
            showAlert("Storage Permission", "The app needs your storage to work.");
        } else {
            showAlert("Storage Permission", "Download again.");
        }
    }
}
