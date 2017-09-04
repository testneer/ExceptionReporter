package com.lib.exceptionreporter.data;

import android.content.Context;
import android.util.Log;

import com.lib.exceptionreporter.ExceptionReporter;
import com.lib.exceptionreporter.Util;
import com.lib.exceptionreporter.model.ExceptionReport;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Created by orenegauthier on 03/09/2017.
 */

public class RepositoryImpl implements Repository {

    private DiskStore mDiskStore;
    private API mApi; //

    public RepositoryImpl(Context ctx){
        mDiskStore = new DiskStore(ctx);
        mApi = new APIImpl();
    }
    @Override
    public void saveExceptionReport(ExceptionReport exceptionReport) {
        mDiskStore.saveReportDataToFile(exceptionReport);
    }

    @Override
    public void sendStoredReports() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                Log.d(ExceptionReporter.LOG_TAG, "UNSENT REPORTS: " + Arrays.toString(mDiskStore.getStoredReports()));
                File[] files = mDiskStore.getStoredReports();
                for(final File file : files){

                    try {
                        Util.log("Loading file named: " + file.getName());
                        //load file
                        String jsonPayload = mDiskStore.loadFileAsString(file);
                        Util.log("File Loaded: Payload = " + jsonPayload);
                        //send to server
                        mApi.sendReport(jsonPayload, new API.Callback() {
                            @Override
                            public void onSuccess() {
                                mDiskStore.deleteReport(file);
                            }

                            @Override
                            public void onError() {
                                //Handle it depends on the error - For the Purpose of this assignment I will not handle errors as the
                                //reports will be sent on the next startup or every 10 minutes (and then the report will be deleted
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
        });
    }
}
