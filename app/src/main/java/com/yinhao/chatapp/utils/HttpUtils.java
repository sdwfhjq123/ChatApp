package com.yinhao.chatapp.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hp on 2017/12/20.
 */

public class HttpUtils {

    public static void handleImageOnServer(File file, String address, Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("txImg", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ConstantValue.URL + address)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    public static void handleInfoOnServer(String address, Map<String, String> map, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> keyset : map.entrySet()) {
            builder.add(keyset.getKey(), keyset.getValue());
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .url(ConstantValue.URL + address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
