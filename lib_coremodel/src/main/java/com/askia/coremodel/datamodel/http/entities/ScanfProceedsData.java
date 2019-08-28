package com.askia.coremodel.datamodel.http.entities;

public class ScanfProceedsData {


    /**
     * code : 100001
     * msg : 交易成功
     * list : null
     * obj : 20
     */

    private int code;
    private String msg;
    private Object list;
    private String obj;

    public boolean isError() {
        return code != ResponseCode.ResponseRradingSuccessCode;
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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }
}
