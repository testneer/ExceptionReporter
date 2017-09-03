package com.ex.org.exceptionstest.com.ex.lib.data;

import com.ex.org.exceptionstest.com.ex.lib.model.ExceptionReport;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by orenegauthier on 03/09/2017.
 */

public interface API {
    public static final String URL = "http://localhost:9000/api/exceptions";

    public interface Callback{
        void onSuccess();
        void onError();
    }


    void sendReport(String json, String id, Callback callback) throws IOException;
}
