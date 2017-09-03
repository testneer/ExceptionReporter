package com.ex.org.exceptionstest.com.ex.lib.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;
import com.ex.org.exceptionstest.com.ex.lib.Util;
import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.Executors;

/**
 * Created by orenegauthier on 02/09/2017.
 */

public class DiskStore{

    private Context mContext;
    public static final String UTF8 = "UTF-8";
    private static final String FOLDER_NAME = "";


    public DiskStore(Context context){
        this.mContext = context;
    }

    public void saveReportDataToFile(@NonNull final ExceptionReport exceptionReport){
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                File dir = mContext.getDir(FOLDER_NAME, Context.MODE_PRIVATE);
                File fullPath = new File(dir, exceptionReport.getExceptionTime());
                Log.d(ExceptionReporter.LOG_TAG, "Writing crash report to file " + fullPath.getAbsolutePath());
                OutputStreamWriter writer = null;
                try {
                    FileOutputStream fos = mContext.openFileOutput(fullPath.getName(), Context.MODE_PRIVATE);
                    writer = new OutputStreamWriter(fos, UTF8);
                    writer.write(exceptionReport.toJson().toString());
                    writer.flush();
                } catch (Exception e) {
                    Log.e(ExceptionReporter.LOG_TAG, "An error occurred while writing the report file...", e);
                }
                finally {
                    if(writer != null){
                        try {
                            writer.close();
                        } catch (IOException ignored) {
                            // We made out best effort to release this resource. Nothing more we can do.
                        }
                    }
                }
            }
        });

    }

    public File[] getStoredReports() {
        File dir = new File(mContext.getFilesDir(), FOLDER_NAME);
        File[] subFiles = dir.listFiles();
//        Log.d(ExceptionReporter.LOG_TAG, "Full dir path = " + dir.getAbsolutePath() + " " + Arrays.toString(subFiles));
        return subFiles == null ? new File[0]:subFiles;
    }

    public void deleteReport(@NonNull File file) {
        Util.log("deleting file: " + file.getName());
        final boolean deleted = file.delete();
        if (!deleted) {
            Log.d(ExceptionReporter.LOG_TAG, "Failed to delete : " + file);
        }
    }

    public String loadFileAsString(@NonNull File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        String ret = sb.toString();
//        Log.d(ExceptionReporter.LOG_TAG, "loadFileAsString: " + ret);
        return ret;
    }

//    private getFullPathFileName
}
