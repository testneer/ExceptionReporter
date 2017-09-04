package com.ex.org.exceptionstest.com.ex.lib.data;

import java.io.IOException;

/**
 *
 * Created by orenegauthier on 03/09/2017.
 */

public interface API {
    //IP address for local host in Android Emulator
     final String URL = "http://10.0.2.2:9000/api/exceptions";

    interface Callback{
        void onSuccess();
        void onError();
    }
    void sendReport(String json, Callback callback) throws IOException;
}
