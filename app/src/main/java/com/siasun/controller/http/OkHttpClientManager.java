package com.siasun.controller.http;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.siasun.controller.Utils.Logger;
import com.siasun.controller.Utils.SystemUtils;
import com.siasun.controller.c.C;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpClientManager {

    private final static String TAG = Logger.LOG_HEAD + OkHttpClientManager.class.getSimpleName();

    private static final int NET_REQUEST_GET = 0;
    private static final int NET_REQUEST_POST = 1;
    //连接超时时间20s
    private static final int NET_REQUEST_READ_TIMEOUT = 30;
    //读数据超时时间20s
    private static final int NET_REQUEST_CONNECT_TIMEOUT = 30;

    //默认的请求回调类
    private final RequestCallBack<String> DEFAULT_RESULT_CALLBACK = new RequestCallBack<String>() {
        @Override
        public void onError(Request request, Exception e) {
        }

        @Override
        public void onResponse(String response) {
        }
    };

    private static OkHttpClientManager mInstance;
    private Handler mMainThreadHandler;
    private OkHttpClient mOkHttpClient;

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(NET_REQUEST_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NET_REQUEST_READ_TIMEOUT, TimeUnit.SECONDS)
                .build();
        mMainThreadHandler = new Handler(Looper.getMainLooper());
    }

    private Request getRequest(String url, HashMap<String, String> values, int request_type) {
        if (request_type == NET_REQUEST_POST) {
            return getRequestByPost(url, values,"");
        } else {
            return getRequestByGet(url, values);
        }
    }

    private Request getRequestByPost(String url, HashMap<String, String> values, Object tag) {

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        for (String attribute : values.keySet()) {
            if (C.ATTRIBUTE_TOKEN.equals(attribute)) {
                continue;
            }
            String value = values.get(attribute);
            formBodyBuilder.add(attribute, value == null ? "" : value);
        }

        String token = values.containsKey(C.ATTRIBUTE_TOKEN) ? values.get(C.ATTRIBUTE_TOKEN) : "";
        String phoneInfo = SystemUtils.getSystemModel();

        RequestBody requestBody = formBodyBuilder.build();
        return new Request.Builder()
                .url(url)
                .header(C.ATTRIBUTE_PHONE_MODEL, phoneInfo)
                .header(C.ATTRIBUTE_TOKEN, token == null ? "" : token)
                .post(requestBody)
                .tag(tag)
                .build();
    }

    private Request getRequestByGet(String url, HashMap<String, String> values) {
        StringBuilder urlExt = new StringBuilder("?");
        for (String attribute : values.keySet()) {
            String value = values.get(attribute);
            urlExt.append(attribute).append("=").append(value);
        }
        String ext = urlExt.toString();
        if (values.size() <= 0) {
            ext = "";
        }
        return new Request.Builder()
                .get()
                .url(url + ext)
                .build();
    }

    private Request getRequestForUpFile(String url, HashMap<String, Object> objectValues, String fileType) {

        String fileName = "";
        String token = "";

        if (objectValues.containsKey(C.ATTRIBUTE_FILE_NAME)) {
            Object value = objectValues.get(C.ATTRIBUTE_FILE_NAME);
            fileName = value == null ? "" : value.toString();
        }

        if (objectValues.containsKey(C.ATTRIBUTE_TOKEN)) {
            Object value = objectValues.get(C.ATTRIBUTE_TOKEN);
            token = value == null ? "" : value.toString();
        }

        MultipartBody.Builder builder = new MultipartBody.Builder();
        String parseKey = getMediaType(fileType);
        builder.setType(MultipartBody.FORM);

        for (String key : objectValues.keySet()) {
            if (key.equals(C.ATTRIBUTE_FILE_NAME) || key.equals(C.ATTRIBUTE_TOKEN)) {
                continue;
            }
            Object object = objectValues.get(key);
            if (object instanceof File) {
                Logger.d(TAG, "getRequestForUpFile() File.length: " + ((File) object).length());
                builder.addFormDataPart(key, fileName,
                        RequestBody.create(MediaType.parse(parseKey), (File) object));
            } else {
                builder.addFormDataPart(key, object != null ? object.toString() : "");
            }
        }

        String phoneInfo = SystemUtils.getSystemModel();

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .header(C.ATTRIBUTE_PHONE_MODEL, phoneInfo)
                .header(C.ATTRIBUTE_TOKEN, token)
                .post(requestBody)
                .build();


    }

    //上传的文件格式
    private String getMediaType(String fileName) {
        if (fileName.equals(C.ATTRIBUTE_FIlE_TYPE_IMAGE)) {
            return "image/png";
        }
        return "";
    }


    private void upFileAysn(String url, HashMap<String, Object> objectValues,String fileType,RequestCallBack callBack) {
        Request request = getRequestForUpFile(url,objectValues,fileType);
        deliveryResult(callBack,request);
    }

    public void getAysn(String url, HashMap<String, String> values,RequestCallBack callBack) {
        Request request = getRequest(url, values,NET_REQUEST_GET);
        deliveryResult(callBack, request);
    }


    public void postAysn(String url, HashMap<String, String> values,RequestCallBack callBack) {
        Request request = getRequest(url, values,NET_REQUEST_POST);
        deliveryResult(callBack, request);
    }

    private void deliveryResult(RequestCallBack callback, final Request request) {
        if (callback == null)
            callback = DEFAULT_RESULT_CALLBACK;
        final RequestCallBack resCallBack = callback;
        callback.onBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendFailedStringCallback(request, e, resCallBack);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final String responseMessage = response.message();
                    ResponseBody requestBody = response.body();
                    String responseBody = requestBody != null ? requestBody.string() : "";
                    if (response.code() == 200) {
                        if (resCallBack.mType == String.class) {
                            sendSuccessResultCallback(responseBody, resCallBack);
                        } else {
                            Object o = JSON.parseObject(responseBody, resCallBack.mType);
                            sendSuccessResultCallback(o, resCallBack);
                        }
                    } else {
                        Exception exception = new Exception(response.code() + ":" + responseMessage);
                        sendFailedStringCallback(response.request(), exception, resCallBack);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resCallBack);
                } catch (JSONException e) {//Json解析的错误
                    sendFailedStringCallback(response.request(), e, resCallBack);
                }
            }
        });
    }

    private void sendFailedStringCallback(Request request, Exception exception, RequestCallBack callBack) {
        mMainThreadHandler.post(() -> {
            callBack.onError(request, exception);
            callBack.onAfter();
        });
    }

    /**
     * 处理请求成功的回调信息方法
     *
     * @param object   服务器响应信息
     * @param callback 回调类
     */
    private void sendSuccessResultCallback(final Object object, RequestCallBack callback) {
        mMainThreadHandler.post(() -> {
            callback.onResponse(object);
            callback.onAfter();
        });
    }
}
