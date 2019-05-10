package com.siasun.controller.app;

import android.app.Application;

import com.siasun.controller.BuildConfig;
import com.siasun.controller.Utils.Logger;

import org.xutils.x;

/**
 * Created on 2019/5/10.
 *
 * @author siasun-wangchongyang
 */
public class ControllerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
        Logger.log_level = BuildConfig.LOG_DEBUG ? Logger.DEBUG:Logger.INFO;
    }
}
