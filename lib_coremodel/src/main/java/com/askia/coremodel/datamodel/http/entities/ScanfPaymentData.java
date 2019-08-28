package com.askia.coremodel.datamodel.http.entities;

public class ScanfPaymentData {

    /**
     * code : 100001
     * msg : 交易成功
     * list : null
     * obj : {"money":0.92,"name":"222","avatar":"http://baimeng.oss-cn-beijing.aliyuncs.com/1530275892239saveImageview.jpg"}
     */

    private int code;
    private String msg;
    private Object list;
    private ObjBean obj;

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * money : 0.92
         * name : 222
         * avatar : http://baimeng.oss-cn-beijing.aliyuncs.com/1530275892239saveImageview.jpg
         */

        private String money;
        private String name;
        private String avatar;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }


}
