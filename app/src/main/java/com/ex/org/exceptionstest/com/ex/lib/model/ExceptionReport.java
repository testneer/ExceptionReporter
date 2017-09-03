package com.ex.org.exceptionstest.com.ex.lib.model;

import android.util.Log;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by orenegauthier on 02/09/2017.
 */

public class ExceptionReport {

    private Map<String, String> data = new HashMap<>();

    private static final String KEY_STACKTRACE = "stacktrace";
    private static final String KEY_EXCEPTION_TIME = "exceptionTime";



    //device type
    //os version
    //device manufacturer
    //screen size
    //many more


    public ExceptionReport(String stacktrace){
        data.put(KEY_STACKTRACE, stacktrace);
        data.put(KEY_EXCEPTION_TIME, Long.toString(Calendar.getInstance().getTimeInMillis()));

        //TODO: remove this debug statment
        Log.d(ExceptionReporter.LOG_TAG, "Logging new Exception " + getExceptionTime());
    }

    public String getExceptionTime() {
        return data.get(KEY_EXCEPTION_TIME);
    }

    public String getStackTrace() {
        return data.get(KEY_STACKTRACE);
    }

    public JSONObject toJson(){
        JSONObject obj = new JSONObject(data);

        try {
            Log.d(ExceptionReporter.LOG_TAG, obj.toString(2));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
