package com.example.jawahra;

import android.content.Context;

public class SharedPreferences {
    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor editor;
    Context context;
    private static final String PREF_NAME = "appFirstTime";
    public static final String KEY_SET_APP_RUN_FIRST_TIME = "KEY_SET_APP_RUN_FIRST_TIME";


    public SharedPreferences(Context context)
    {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void setApp_runFirst(String App_runFirst)
    {
        editor.remove(KEY_SET_APP_RUN_FIRST_TIME);
        editor.putString(KEY_SET_APP_RUN_FIRST_TIME, App_runFirst);
        editor.apply();
    }

    public String getApp_runFirst()
    {
        String  App_runFirst= pref.getString(KEY_SET_APP_RUN_FIRST_TIME, "FIRST");
        return  App_runFirst;
    }

}
