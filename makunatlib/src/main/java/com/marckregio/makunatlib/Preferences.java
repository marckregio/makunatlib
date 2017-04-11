package com.marckregio.makunatlib;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by makregio on 23/01/2017.
 */

public abstract class Preferences {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private Broadcast broadcast;


    public Preferences(Context context){
        this.context = context;
        preferences = context.getSharedPreferences(context.getPackageName(), context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void putString(String field, String value){

    }

    public void editString(String field, String value){

    }

    public String checkString(String field){
        return preferences.getString(field, "");
    }

    public boolean checkBoolean(String field){
        return preferences.getBoolean(field, false);
    }


}
