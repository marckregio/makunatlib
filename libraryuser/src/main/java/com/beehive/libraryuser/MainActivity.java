package com.beehive.libraryuser;

import android.os.Bundle;

import com.marckregio.makunatlib.BaseActivity;
import com.marckregio.makunatlib.SoapRequest;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SoapRequest.getInstance().execute();
    }
}
