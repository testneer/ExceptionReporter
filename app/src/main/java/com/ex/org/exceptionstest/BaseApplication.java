package com.ex.org.exceptionstest;

import android.app.Application;
import android.content.Context;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;

/**
 * Created by orenegauthier on 01/09/2017.
 */

public class BaseApplication extends Application {



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ExceptionReporter.getInstance(base).init();
    }
}
