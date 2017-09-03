package com.ex.org.exceptionstest.com.ex.lib.model;

import org.junit.Test;

/**
 * Created by orenegauthier on 02/09/2017.
 */
public class CrashReportTest {

   private String stacktrace = "java.lang.NullPointerException: NullPointerException test\n" +
       "at com.ex.org.exceptionstest.MainActivity$2.onClick(MainActivity.java:34)\n" +
       "at android.view.View.performClick(View.java:5610)\n" +
       "at android.view.View$PerformClick.run(View.java:22265)\n" +
       "at android.os.Handler.handleCallback(Handler.java:751)\n" +
       "at android.os.Handler.dispatchMessage(Handler.java:95)\n" +
       "at android.os.Looper.loop(Looper.java:154)\n" +
       "at android.app.ActivityThread.main(ActivityThread.java:6077)\n" +
       "at java.lang.reflect.Method.invoke(Native Method)\n" +
       "at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:866)\n" +
       "at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:756)";


    @Test
    public void testToJson() throws Exception {
        ExceptionReport report = new ExceptionReport("oren");
        System.out.println(report.toJson());
    }

}