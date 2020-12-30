package com.example.proiectechiparacheta;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;


public class SharedPreferencesManager {
    private SharedPreferences sPreferences;

    private SharedPreferences.Editor sEditor;


    private Context context;

    public SharedPreferencesManager(Context context){
        this.context = context;
        sPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences.Editor getEditor(){
        return sPreferences.edit();
    }

    public void storeBoolean(String tag, boolean value){
        sEditor = getEditor();
        sEditor.putBoolean(tag,value);
        sEditor.commit();
    }

    public void storeString(String tag, String str){
        sEditor = getEditor();
        sEditor.putString(tag, str);
        sEditor.commit();
    }

    public boolean retrieveBoolean(String tag, boolean defValue){
        return sPreferences.getBoolean(tag,defValue);

    }

    public String retrieveString(String tag, String defStr){
        return sPreferences.getString(tag, defStr);
    }

    public int retrieveInt(String tag, int defValue){
        return sPreferences.getInt(tag, defValue);
    }
    public void storeInt(String tag, int defValue){
        sEditor = getEditor();
        sEditor.putInt(tag, defValue);
        sEditor.commit();
    }
}



