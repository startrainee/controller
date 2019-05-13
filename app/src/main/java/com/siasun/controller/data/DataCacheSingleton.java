package com.siasun.controller.data;

import android.text.TextUtils;

import com.siasun.controller.provider.C;

/**
 * Created on 2019/5/13.
 *
 * @author siasun-wangchongyang
 */
public class DataCacheSingleton {

    private static DataCacheSingleton ourInstance = new DataCacheSingleton();

    public static DataCacheSingleton getInstance() {
        return ourInstance;
    }

    private DataCacheSingleton() {
    }

    private boolean isLogin;

    public boolean isLogin() {
        judgeLogin();
        return isLogin;
    }

    private void judgeLogin() {
        isLogin = !TextUtils.isEmpty(C.Tk);
    }

    public void readLoginConfig(){
        C.Tk = SharedPreferencesHelper.getInstance().readConfig(ConfigKey.Token,"").toString();
        judgeLogin();
    }

    public void saveLoginConfig(){
        SharedPreferencesHelper.getInstance().saveConfig(ConfigKey.Token,C.Tk);
    }

    interface ConfigKey {
        String Token = "fecb";
    }
}
