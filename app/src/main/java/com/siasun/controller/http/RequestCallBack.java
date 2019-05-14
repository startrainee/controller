package com.siasun.controller.http;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Request;

/**
 * Created on 2019/5/10.
 *
 * @author siasun-wangchongyang
 */
public abstract class RequestCallBack<T> {
    //这是请求数据的返回类型，包含常见的（Bean，List等）
    Type mType;

    protected RequestCallBack() {
        mType = getSuperclassTypeParameter(getClass());
    }

    /**
     * 通过反射想要的返回类型
     */
    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        if (parameterized == null){
            throw new RuntimeException("Missing type parameterized.");
        }
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    /**
     * 在请求之前的方法，如加载框展示
     *
     * @param request okhttp 请求类
     */
    public void onBefore(Request request) {
    }

    /**
     * 在请求之后的方法，如加载框隐藏
     */
    public void onAfter() {
    }

    /**
     * 请求失败的时候
     *
     * @param request okhttp 请求类
     * @param e  异常
     */
    public abstract void onError(Request request, Exception e);

    /**
     *
     * @param response 正常的返回结果
     */
    public abstract void onResponse(T response);
}
