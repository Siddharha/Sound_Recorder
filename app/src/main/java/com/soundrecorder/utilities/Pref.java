package com.soundrecorder.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by BLUEHORSE 123 on 12/14/2015.
 */
public class Pref {
    private SharedPreferences spref;
    //private Activity _activity;
    private static final String PREF_FILE = "com.soundrecorder";
    private SharedPreferences.Editor _editorSpref;

    public SharedPreferences getSharedPreferencesInstance() {
        return spref;
    }

    public SharedPreferences.Editor getSharedPreferencesEditorInstance() {
        return _editorSpref;
    }

    @SuppressLint("CommitPrefEdits")
    public Pref(Context _thisContext) {
        // TODO Auto-generated constructor stub
        //this._activity = (Activity)_thisContext;
        spref = _thisContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        _editorSpref = spref.edit();
    }

    public String getSession(String key) {
        String value = spref.getString(key, "");
        return value;
    }

    public int getIntegerSession(String key) {
        int value = spref.getInt(key, Integer.MIN_VALUE);
        return value;
    }



    public void setSession(String key, String value) {
        if (key != null && value != null) {
            _editorSpref.putString(key, value);
            _editorSpref.commit();
        }
    }

    // @author : DTS
    public void setSession(String key, int value) {
        if (key != null) {
            _editorSpref.putInt(key, value);
            _editorSpref.commit();
        }
    }

    public void setSession(String key, boolean value) {
        if (key != null) {
            _editorSpref.putBoolean(key, value);
            _editorSpref.commit();
        }
    }

    public boolean getBooleanSession(String key){
        boolean value=spref.getBoolean(key, false);
        return value;
    }

    public void saveTracks(String name) {
        _editorSpref.putString("name", name);
        _editorSpref.commit();
    }
    public String getTracks() {
        return spref.getString("name", "");
    }


}
