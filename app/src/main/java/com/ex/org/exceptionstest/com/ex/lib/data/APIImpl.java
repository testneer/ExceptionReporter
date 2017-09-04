package com.ex.org.exceptionstest.com.ex.lib.data;

import com.ex.org.exceptionstest.com.ex.lib.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by orenegauthier on 03/09/2017.
 */

public class APIImpl implements API {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String HEADER_ID = "exceptionId";
    private OkHttpClient mClient;

    public APIImpl(){
        mClient = new OkHttpClient();
    }


    private void postAsync(String url, String json, final Callback callback) throws IOException {
        Util.log("Sending File with payload= " + json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();

        mClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(Call call, IOException e) {
                callback.onError();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = null;
                try {
                    responseBody = response.body();
                    if (!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }
                    callback.onSuccess();
                }
                finally {
                    if (responseBody != null) {
                        responseBody.close();
                    }
                }
            }
        });
    }


    String postSync(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .build();
        Response response = null;
        try {
            response = mClient.newCall(request).execute();
            return response.body().string();
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void sendReport(String json, Callback callback) throws IOException {
        postAsync(API.URL, json, callback);
    }
}
