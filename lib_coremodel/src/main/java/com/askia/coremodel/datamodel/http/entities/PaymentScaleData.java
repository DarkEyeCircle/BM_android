package com.askia.coremodel.datamodel.http.entities;

public class PaymentScaleData {


    /**
     * code : 1000
     * msg : 获取付款质保金比例成功
     * list : null
     * obj : 0.08
     */

    private int code;
    private String msg;
    private Object list;
    private float obj;

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

    public float getObj() {
        return obj;
    }

    public void setObj(float obj) {
        this.obj = obj;
    }
}
