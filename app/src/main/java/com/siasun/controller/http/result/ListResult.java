package com.siasun.controller.http.result;

import java.util.List;

/**
 * Created on 2019/5/13.
 *
 * @author siasun-wangchongyang
 */
public class ListResult<T> {

    private int code;
    private List<T> data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
