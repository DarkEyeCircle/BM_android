package com.askia.coremodel.datamodel.http.entities;

public class BaseResponseData {

    /**
     * code : 900000
     * msg : 该手机号已被注册
     * list : null
     * obj : null
     */

    private int code;
    private String msg;
    private Object list;
    private Object obj;

    public boolean isError() {
        return code != ResponseCode.ResponseSuccessCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
