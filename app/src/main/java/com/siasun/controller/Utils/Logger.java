package com.siasun.controller.Utils;

/**
 * Created on 2019/5/10.
 *
 * @author siasun-wangchongyang
 */
import android.util.Log;

public class Logger {

    public static final String LOG_HEAD = "controller-";

    public static final int VERBOSE = 2;
    public static final int DEBUG   = 3;
    public static final int INFO    = 4;
    public static final int WARN    = 5;
    public static final int ERROR   = 6;
    private static final int ASSERT = 7;
    public static int log_level = INFO;

    public static void v(String tag, String msg) {
        if (log_level <= VERBOSE)
            Log.v(tag, msg);
    }
    public static void d(String tag, String msg) {
        if (log_level <= DEBUG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (log_level <= INFO)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (log_level <= WARN)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (log_level <= ERROR)
            Log.e(tag, msg);
    }
}

