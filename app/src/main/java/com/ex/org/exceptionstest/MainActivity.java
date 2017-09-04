package com.ex.org.exceptionstest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ex.org.exceptionstest.com.ex.lib.ExceptionReporter;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String tag = "TEST_APP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button caughtBtn = (Button) findViewById(R.id.btn_caught);
        Button uncaughtBtn = (Button) findViewById(R.id.btn_uncaught);

        caughtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Assume this exception was caught in a try catch block
                Exception caughtException = new IOException("You caught me there");
                ExceptionReporter.logException(caughtException);
            }
        });

        uncaughtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(tag, "uncaughtBtn");
                throw new NullPointerException("NullPointerException test");
            }
        });
    }
}
