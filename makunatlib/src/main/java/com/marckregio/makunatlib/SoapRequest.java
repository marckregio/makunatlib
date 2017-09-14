package com.marckregio.makunatlib;

import android.os.AsyncTask;

/**
 * Created by makregio on 13/09/2017.
 */

public class SoapRequest extends AsyncTask<Boolean, Void, Boolean> {

    public static String BASE = "http://172.16.85.122/zed/";
    public static String ACTION = "Authenticate";
    public static String SOAP_ACTION = BASE + ACTION;
    public static String SOAP_ADDRESS = BASE + "zed.asmx";


    public static SoapRequest getInstance(){
        SoapRequest soapHelper = new SoapRequest();
        return soapHelper;
    }

    @Override
    protected Boolean doInBackground(Boolean... params) {
        try{

        } catch(Throwable e){
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {

        }
    }
}
