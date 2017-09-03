package com.ex.org.exceptionstest.com.ex.lib.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;
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

/**
 * Created by orenegauthier on 02/09/2017.
 */

public class DiskStore{

    private Context ctx;
    public static final int BUFFER_SIZE = 8192;
    public static final String UTF8 = "UTF-8";


    public DiskStore(Context context){
        this.ctx = context;
    }

    public void saveReportDataToFile(@NonNull ExceptionReport exceptionReport) throws FileNotFoundException, UnsupportedEncodingException {
        Log.d(ExceptionReporter.LOG_TAG, "Writing crash report to file " + exceptionReport.getExceptionTime());
        String fileName = exceptionReport.getExceptionTime();
        FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
        OutputStreamWriter writer = new OutputStreamWriter(fos, UTF8);

        try {
            writer.write(exceptionReport.toJson().toString());
            writer.flush();
        } catch (Exception e) {
            Log.e(ExceptionReporter.LOG_TAG, "An error occurred while writing the report file...", e);
        }
        finally {
            try {
                writer.close();
            } catch (IOException ignored) {
                // We made out best effort to release this resource. Nothing more we can do.
            }
        }
    }

    public File[] getUnsentReport() {
        File dir = ctx.getFilesDir();
        File[] subFiles = dir.listFiles();
        return subFiles;
    }

    public void deleteReport(@NonNull File file) {
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
}
