package com.ex.org.exceptionstest.com.ex.lib.data;

import com.ex.org.exceptionstest.com.ex.lib.Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Headers;
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
    private OkHttpClient client;

    public APIImpl(){
        client = new OkHttpClient();
    }


    public void postAsync(String url, String json, String id, final Callback callback) throws IOException {
//        post(url, json, id);
        Util.log("Preparing to send file where id=" + id);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader(HEADER_ID, id)
            .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onError();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = null;
                try {
                    responseBody = response.body();
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    System.out.println(responseBody.string());
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


    public String postToDelete(String url, String json, String id) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
            .url(url)
            .post(body)
            .addHeader(HEADER_ID, id)
            .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        }
        finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void sendReport(String json, String id, Callback callback) throws IOException {
        postAsync(API.URL, json, id, callback);
    }
}
