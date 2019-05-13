package com.siasun.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created on 2019/5/13.
 *
 * @author siasun-wangchongyang
 */

public class Test {

    @org.junit.Test
    public void run() {

        CallBackResult<ListResult<User>> d = new CallBackResult<ListResult<User>>(){};
        CallBackResult<Result<User>> d1 = new CallBackResult<Result<User>>(){};
        String json = "{\n" +
                " \"code\": 200,\n" +
                "  \"data\":[{\"user_id\":1,\"user_name\":\"hehe\"},{\"user_id\":2,\"user_name\":\"hehe2\"}],\n" +
                " \"msg\": \"登录成功\"\n" +
                "}";

        System.out.println(json);
        Object obj = new Gson().fromJson(json,d.mType);
        System.out.println(((ListResult<User>)obj).msg);
        System.out.println(((ListResult<User>)obj).data.get(0).getUser_id());
        System.out.println(((ListResult<User>)obj).data.get(0).getUser_name());
        String json1 = "{\n" +
                " \"code\": 200,\n" +
                "  \"data\":{\"user_id\":1,\"user_name\":\"hehe\"},\n" +
                " \"msg\": \"登录成功\"\n" +
                "}";

        System.out.println(json1);
        Object obj1 = JSON.parseObject(json1,d1.mType);
        System.out.println(((Result<User>)obj1).msg);
        System.out.println(((Result<User>)obj1).data.getUser_id());
        System.out.println(((Result<User>)obj1).data.getUser_name());
    }
}

class CallBackResult<T>{
    Type mType;

    CallBackResult() {
        mType = getSuperclassTypeParameter(getClass());
    }

    private static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        System.out.println("type: " + superclass);
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        if (parameterized == null){
            throw new RuntimeException("Missing type parameterized.");
        }
        Type type = parameterized.getActualTypeArguments()[0];
        return type;
    }
}

class ListResult<T> {
    public String code;
    public List<T> data;
    public String msg;
}

class Result<T> {
    public String code;
    public T data;
    public String msg;
}



class User {

    private Long user_id;

    private String user_name;

    public User() {

    }

    public User(Long userId, String name) {
        this.user_id = userId;
        this.user_name = name;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

}