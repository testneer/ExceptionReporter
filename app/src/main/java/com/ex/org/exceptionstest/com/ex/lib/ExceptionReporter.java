package com.ex.org.exceptionstest.com.ex.lib;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ex.org.exceptionstest.com.ex.lib.data.Repository;
import com.ex.org.exceptionstest.com.ex.lib.data.RepositoryImpl;
import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Singleton and Entry point of the ExceptionReporter
 * Created by orenegauthier on 02/09/2017.
 */

public final class ExceptionReporter {

    private static ExceptionReporter instance;
    private Repository repo;


    public final static String LOG_TAG = "ExceptionReporter";

    //private constructor
    private ExceptionReporter(Context ctx){
        repo = new RepositoryImpl(ctx);
        //If there are any unsent crashes send them now.
        repo.sendStoredReports();


    };

    public static ExceptionReporter getInstance(Application app){
        if(app == null){
            throw new IllegalArgumentException("Application can't be null");
        }
        if(instance == null) {
            instance = new ExceptionReporter(app);
        }
        return instance;
    }


    public void init(){
        interceptExceptions();
    }

    private void interceptExceptions() {
        final Thread.UncaughtExceptionHandler defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.UncaughtExceptionHandler interceptor = new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                handleException(thread, throwable);
                defaultExceptionHandler.uncaughtException(thread, throwable);
            }
        };

        Thread.setDefaultUncaughtExceptionHandler(interceptor);
    }

    public static void logException(Exception caughtException){
        if(instance == null){
            throw new IllegalStateException("ExceptionReporter.getInstance.init() must be called prior to calling this");
        }
        instance.handleException(Thread.currentThread(), caughtException);
    }

    //TODO: Not thread safe
    private void handleException(Thread thread, @NonNull Throwable throwable){
        //TODO: extract data from Thread and send to the crash report.
        if(throwable != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            ExceptionReport report = new ExceptionReport(sw.toString());
            //Save the report
            repo.saveExceptionReport(report);
        }
    }

}
