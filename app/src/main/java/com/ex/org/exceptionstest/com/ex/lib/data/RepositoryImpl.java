package com.ex.org.exceptionstest.com.ex.lib.data;

import android.content.Context;
import android.util.Log;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;
import com.ex.org.exceptionstest.com.ex.lib.Util;
import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by orenegauthier on 03/09/2017.
 */

public class RepositoryImpl implements Repository {

    private Context context;
    private DiskStore diskStore;
    private API api;

    public RepositoryImpl(Context ctx){
        this.context = ctx;
        diskStore = new DiskStore(ctx);
        api = new APIImpl();
    }
    @Override
    public void saveExceptionReport(ExceptionReport exceptionReport) {
        try {
            diskStore.saveReportDataToFile(exceptionReport);
        }
        catch (FileNotFoundException e) {
            Log.d(ExceptionReporter.LOG_TAG, "saveExceptionReport " + Log.getStackTraceString(e));
        }
        catch (UnsupportedEncodingException e) {
            Log.d(ExceptionReporter.LOG_TAG, "saveExceptionReport: " + Log.getStackTraceString(e));
        }
    }

    @Override
    public void sendStoredReports() {
        Log.d(ExceptionReporter.LOG_TAG, "UNSENT REPORTS: " + Arrays.toString(diskStore.getUnsentReport()));
        File[] files = diskStore.getUnsentReport();
        for(File file : files){

            //load file
            try {
                Util.log("Loading file named: " + file.getName());
                String jsonPayload = diskStore.loadFileAsString(file);
                Util.log("File Loaded: Payload = " + jsonPayload);
                //send to server
                api.sendReport(jsonPayload, file.getName(), new API.Callback() {
                    @Override
                    public void onSuccess() {

                        diskStore.deleteReport(file);
                    }

                    @Override
                    public void onError() {
                        //depends on the error;
                    }
                });

            }
            catch (IOException e) {
                e.printStackTrace();
            }
            // if response = good then delete file
            //do only if request are no cancelled.
        }
    }


}
