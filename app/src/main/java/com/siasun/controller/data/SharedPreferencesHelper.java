package com.siasun.controller.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.siasun.rtd.tools.MD5Utils;

import java.util.Map;

/**
 * Created on 2019/1/21.
 *
 * @author siasun-wangchongyang
 */
public class SharedPreferencesHelper {

    private static final SharedPreferencesHelper ourInstance = new SharedPreferencesHelper();
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static SharedPreferencesHelper getInstance() {
        return ourInstance;
    }

    private SharedPreferencesHelper() {
    }

    @SuppressLint("CommitPrefEdits")
    public void createSP(Context context, String fileName) {
        this.sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public SharedPreferences getPreferences(){
        return sharedPreferences;
    }

    /**
     * 存储数据
     */
    void saveConfig(String key, Object object) {
        object = object == null ? "":object;
        if (object instanceof String) {
            editor.putString(key, object.toString());
            editor.putString(key + key, MD5Utils.MD5(object.toString()));
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 获取保存的数据
     */
    Object readConfig(String key, Object defaultObject) {
        if (defaultObject instanceof String) {
            String value = sharedPreferences.getString(key, defaultObject.toString());
            String checkValue = sharedPreferences.getString(key + key, defaultObject.toString());
            return check(value,checkValue) ? value:  "";
        } else if (defaultObject instanceof Integer) {
            return sharedPreferences.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sharedPreferences.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sharedPreferences.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sharedPreferences.getLong(key, (Long) defaultObject);
        } else {
            return sharedPreferences.getString(key, null);
        }
    }

    public String getString(String key, String defaultValue){
        return sharedPreferences.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue){
        return sharedPreferences.getInt(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue){
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue){
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public void remove(String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     */
    public void clear() {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否存在
     */
    public Boolean contain(String key) {
        return sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    private boolean check(String value, String md5) {
        try {
            String vMD5 = MD5Utils.MD5(value);
            return md5.equalsIgnoreCase(vMD5);
        } catch (Exception var4) {
            return false;
        }
    }
}
