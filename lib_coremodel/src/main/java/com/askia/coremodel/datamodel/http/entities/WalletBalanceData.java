package com.askia.coremodel.datamodel.http.entities;

public class WalletBalanceData {


    /**
     * code : 1000
     * msg : true
     * list : null
     * obj : {"comment":"商户钱包","dictStatusName":"启用","dictStatus":"100000","userId":"020082f48a4c4c74bce199c59c8acc38","balance":0,"dictTypeName":"商户钱包","dictType":"100205","id":"5ac14fdc6b96416bbed8cd11697b34d9","size":10,"current":1,"operation":"operation","insertTime":"Tue Jun 12 16:26:47 CST 2018","insertTimeDate":"2018-06-12","insertTimeTime":"2018-06-12 16:26:47"}
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
         * proportion:0.08
         * comment : 商户钱包
         * dictStatusName : 启用
         * dictStatus : 100000
         * userId : 020082f48a4c4c74bce199c59c8acc38
         * balance : 0
         * dictTypeName : 商户钱包
         * dictType : 100205
         * id : 5ac14fdc6b96416bbed8cd11697b34d9
         * size : 10
         * current : 1
         * operation : operation
         * insertTime : Tue Jun 12 16:26:47 CST 2018
         * insertTimeDate : 2018-06-12
         * insertTimeTime : 2018-06-12 16:26:47
         */
        private float proportion;
        private String comment;
        private String dictStatusName;
        private String dictStatus;
        private String userId;
        private String balance;
        private String dictTypeName;
        private String dictType;
        private String id;
        private int size;
        private String current;
        private String operation;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public float getProportion() {
            return proportion;
        }

        public void setProportion(float proportion) {
            this.proportion = proportion;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDictStatusName() {
            return dictStatusName;
        }

        public void setDictStatusName(String dictStatusName) {
            this.dictStatusName = dictStatusName;
        }

        public String getDictStatus() {
            return dictStatus;
        }

        public void setDictStatus(String dictStatus) {
            this.dictStatus = dictStatus;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getDictTypeName() {
            return dictTypeName;
        }

        public void setDictTypeName(String dictTypeName) {
            this.dictTypeName = dictTypeName;
        }

        public String getDictType() {
            return dictType;
        }

        public void setDictType(String dictType) {
            this.dictType = dictType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getCurrent() {
            return current;
        }

        public void setCurrent(String current) {
            this.current = current;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getInsertTime() {
            return insertTime;
        }

        public void setInsertTime(String insertTime) {
            this.insertTime = insertTime;
        }

        public String getInsertTimeDate() {
            return insertTimeDate;
        }

        public void setInsertTimeDate(String insertTimeDate) {
            this.insertTimeDate = insertTimeDate;
        }

        public String getInsertTimeTime() {
            return insertTimeTime;
        }

        public void setInsertTimeTime(String insertTimeTime) {
            this.insertTimeTime = insertTimeTime;
        }
    }
}
