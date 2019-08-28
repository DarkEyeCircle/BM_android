package com.askia.coremodel.datamodel.http.entities;

public class UserInfoByQRCode {


    /**
     * code : 1000
     * msg : 获取信息成功
     * list : null
     * obj : {"receiveUserId":"accab9ed1bcd4fa59b9a6bb298eb53d6","money":null,"type":0,"avatar":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3468481793,3455309356&fm=27&gp=0.jpg","nickName":"666","codeId":"db93d57de4a54279af5bc4410f992bb2"}
     */

    private int code;
    private String msg;
    private Object list;
    private ObjBean obj;

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * receiveUserId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * money : null
         * type : 0
         * avatar : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3468481793,3455309356&fm=27&gp=0.jpg
         * nickName : 666
         * codeId : db93d57de4a54279af5bc4410f992bb2
         */

        private String receiveUserId;
        private String money;
        private int type;
        private String avatar;
        private String nickName;
        private String codeId;

        public String getReceiveUserId() {
            return receiveUserId;
        }

        public void setReceiveUserId(String receiveUserId) {
            this.receiveUserId = receiveUserId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCodeId() {
            return codeId;
        }

        public void setCodeId(String codeId) {
            this.codeId = codeId;
        }
    }
}
