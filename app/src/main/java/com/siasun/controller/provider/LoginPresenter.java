package com.siasun.controller.provider;

import com.siasun.controller.c.C;
import com.siasun.controller.http.RequestCallBack;
import com.siasun.controller.provider.response.LoginResponseBean;

import java.util.HashMap;

import okhttp3.Request;

/**
 * Created on 2019/5/13.
 *
 * @author siasun-wangchongyang
 */
public class LoginPresenter extends BasePresenter {

    public void doLogin(String name, String password, TokenLevel tokenLevel, LoginCallback<LoginResponseBean> callBack) {
        HashMap<String, String> hashMap = getRequestHashMap(tokenLevel);
        hashMap.put(C.ATTRIBUTE_LOGIN_NAME, name);
        hashMap.put(C.ATTRIBUTE_LOGIN_PWD, password);
        String url = "";
        doPostRequest(url, hashMap, new RequestCallBack<LoginResponseBean>() {

            public void onBefore(Request request) {
                callBack.onBefore(request);
            }

            public void onAfter() {
                callBack.onAfter();
            }

            @Override
            public void onError(Request request, Exception e) {
                callBack.onLoginFailed();

            }

            @Override
            public void onResponse(LoginResponseBean response) {
                callBack.onLoginSuccess(response);
            }
        });
    }

    interface LoginCallback<T> extends BasePresenterCallBack {
        void onLoginSuccess(T response);
        void onLoginFailed();
    }
}


