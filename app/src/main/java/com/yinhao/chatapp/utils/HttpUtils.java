package com.yinhao.chatapp.utils;

import android.util.Log;

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

    private static final String TAG = "HttpUtils";

    public static void handleImageOnServer(File file, String id, String address, Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // MediaType.parse() 里面是上传的文件类型。
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        // 参数分别为， 请求key ，文件名称 ， RequestBody
        builder.addFormDataPart("txImg", file.getName(), body);
        Log.i(TAG, "上传的参数txImg:" + file.getName());
        builder.addFormDataPart("id", id);
        MultipartBody multipartBody = builder.build();

        Request request = new Request.Builder()
                .post(multipartBody)
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

    /**
     * 完善资料的上传
     *
     * @param file     头像
     * @param address  请求地址
     * @param nikeName 昵称
     */
    public static void handleCompleteInfoOnServer(File file, String address, String nikeName, String id, Callback callback) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("txImg", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        builder.addFormDataPart("nikeName", nikeName);
        builder.addFormDataPart("id", id);
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(ConstantValue.URL + address)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
