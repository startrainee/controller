package com.siasun.controller.provider;

import com.siasun.controller.c.C;
import com.siasun.controller.data.DataCacheSingleton;
import com.siasun.controller.http.OkHttpClientManager;
import com.siasun.controller.http.RequestCallBack;

import java.util.HashMap;

import okhttp3.Request;

/**
 * Created on 2019/5/13.
 *
 * @author siasun-wangchongyang
 */

abstract class BasePresenter{

    public enum TokenLevel {
        NO_NEED_TOKEN_0, CAN_TOKEN_1, MUST_TOKEN_2
    }

    HashMap<String, String> getRequestHashMap(TokenLevel tokenLevel) {
        HashMap<String, String> hashMap = new HashMap<>();
        switch (tokenLevel) {
            case CAN_TOKEN_1:
                hashMap.put(C.ATTRIBUTE_TOKEN, C.Tk);
                break;
            case NO_NEED_TOKEN_0:
                if (DataCacheSingleton.getInstance().isLogin()) {
                    hashMap.put(C.ATTRIBUTE_TOKEN, C.Tk);
                }
                break;
            case MUST_TOKEN_2:
            default:
                break;
        }
        return hashMap;
    }

    void doPostRequest(String url, HashMap<String, String> hashMap, RequestCallBack callBack) {
        OkHttpClientManager.getInstance().postAysn(url, hashMap, callBack);
    }

    interface BasePresenterCallBack {
        default void onBefore(Request request){}
        default void onAfter(){}
    }
}

