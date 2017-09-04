package com.ex.org.exceptionstest.com.ex.lib;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ex.org.exceptionstest.com.ex.lib.data.Repository;
import com.ex.org.exceptionstest.com.ex.lib.data.RepositoryImpl;
import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Singleton and Entry point of the ExceptionReporter
 * Created by orenegauthier on 02/09/2017.
 */

public final class ExceptionReporter {

    private static ExceptionReporter sInstance;
    private Repository mRepo;
    private boolean mInitiated;
    private Context mContext;


    public final static String LOG_TAG = "ExceptionReporter";

    //private constructor
    private ExceptionReporter(Context ctx){
        mRepo = new RepositoryImpl(ctx);
        mContext = ctx;
        //If there are any unsent crashes send them now.
        mRepo.sendStoredReports();
    };


    /**
      * The main integration point to your app.
      * Call this from Application.getBaseContext(base Context) with "ExceptionReporter.getInstance(base).init()"
      *
      * @param context - Must be the application context
      * @return the instance of this Singleton
      */
    public static ExceptionReporter getInstance(Context context){
        if(context == null){
            throw new IllegalArgumentException("context can't be null");
        }
        if(sInstance == null) {
            sInstance = new ExceptionReporter(context);
        }
        return sInstance;
    }



    public void init(){
        if(!mInitiated){
            mInitiated = true;
            interceptExceptions();
            setSendReportTimer();
        }
    }

    private void setSendReportTimer() {
        ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
            (new Runnable() {
                public void run() {
                    mRepo.sendStoredReports();
                }
            }, 10, 10, TimeUnit.MINUTES);
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
        if(sInstance == null){
            throw new IllegalStateException("ExceptionReporter.getInstance.init() must be called prior to calling this");
        }
        sInstance.handleException(Thread.currentThread(), caughtException);
    }

    //TODO: Not thread safe - I should handle the case when this method is called from multiple worker threads.
    //TODO: (prob just enqueue the logging requests)
    private void handleException(Thread thread, @NonNull Throwable throwable){
        //TODO: extract data from Thread and send to the crash report.
        try {
            if (throwable != null) {
                //The Log.getStackTraceString(throwable) call looks for nested exceptions.
                ExceptionReport report = new ExceptionReport(Log.getStackTraceString(throwable));
                //add extra info to better understand the Exception cause
                report.addExtraInfo(new InfoCollector(mContext).getExtraInfo());
                //Save the report
                mRepo.saveExceptionReport(report);
            }
        }catch (Throwable t){
            // do nothing.. let the exception propagate to the default handler
        }
    }
}
