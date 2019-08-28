package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class BandCardData {

    /**
     * code : 1000
     * msg : true
     * list : [{"openingBank":"工商银行","cardNo":"6215591813002418111","mobile":"18704201023","dictStatusName":"成功","dictStatus":"100001","userId":"020082f48a4c4c74bce199c59c8acc38","bankId":"","id":"2b18a36cf43a4ed4a07cf0a3e56c3185","size":10,"current":1,"operation":"operation","insertTime":"Tue Jun 12 16:28:33 CST 2018","insertTimeDate":"2018-06-12","insertTimeTime":"2018-06-12 16:28:33"}]
     * obj : null
     * current : 1
     * size : 10
     * total : 1
     * pages : 1
     */

    private int code;
    private String msg;
    private Object obj;
    private int current;
    private int size;
    private int total;
    private int pages;
    private List<ListBean> list;

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

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * openingBank : 工商银行
         * cardNo : 6215591813002418111
         * mobile : 18704201023
         * dictStatusName : 成功
         * dictStatus : 100001
         * userId : 020082f48a4c4c74bce199c59c8acc38
         * bankId :
         * id : 2b18a36cf43a4ed4a07cf0a3e56c3185
         * size : 10
         * current : 1
         * operation : operation
         * insertTime : Tue Jun 12 16:28:33 CST 2018
         * insertTimeDate : 2018-06-12
         * insertTimeTime : 2018-06-12 16:28:33
         */

        private String openingBank;
        private String cardNo;
        private String mobile;
        private String dictStatusName;
        private String dictStatus;
        private String userId;
        private String bankId;
        private String id;
        private int size;
        private int current;
        private String operation;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public String getOpeningBank() {
            return openingBank;
        }

        public void setOpeningBank(String openingBank) {
            this.openingBank = openingBank;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
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

        public String getBankId() {
            return bankId;
        }

        public void setBankId(String bankId) {
            this.bankId = bankId;
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

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
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
