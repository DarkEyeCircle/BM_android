package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class StatementBillDetailsData {

    /**
     * code : 1000
     * msg : true
     * list : [{"comment":"买卡","amount":100,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","dictTypeName":"日常用品","dictType":"100300","dictStatusName":"成功","dictStatus":"100001","commodityName":"买卡","id":"ddff4f16d75e488987ff909389bdedf9","size":10,"current":1,"operation":"operation","insertTime":"Fri May 25 11:47:31 CST 2018","insertTimeDate":"2018-05-25","insertTimeTime":"2018-05-25 11:47:31"},{"comment":"买卡","amount":100,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","dictTypeName":"日常用品","dictType":"100300","dictStatusName":"成功","dictStatus":"100001","commodityName":"买卡","id":"697d8941c80e4ddc96c50cf9568a643f","size":10,"current":1,"operation":"operation","insertTime":"Fri May 25 11:38:35 CST 2018","insertTimeDate":"2018-05-25","insertTimeTime":"2018-05-25 11:38:35"},{"comment":"买卡","amount":200,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","dictTypeName":"日常用品","dictType":"100300","dictStatusName":"成功","dictStatus":"100001","commodityName":"买卡","id":"8d7bd3fcd2d2438f81307a4fdccf8748","size":10,"current":1,"operation":"operation","insertTime":"Fri May 25 11:38:02 CST 2018","insertTimeDate":"2018-05-25","insertTimeTime":"2018-05-25 11:38:02"},{"comment":"买卡","amount":200,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","dictTypeName":"日常用品","dictType":"100300","dictStatusName":"成功","dictStatus":"100001","commodityName":"买卡","id":"457c1dd726194bf5ab3e85df9c28ecac","size":10,"current":1,"operation":"operation","insertTime":"Fri May 25 10:53:05 CST 2018","insertTimeDate":"2018-05-25","insertTimeTime":"2018-05-25 10:53:05"},{"comment":"买卡","amount":100,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","dictTypeName":"日常用品","dictType":"100300","dictStatusName":"成功","dictStatus":"100001","commodityName":"买卡","id":"a518e350cc9f49deb93b96a5a22176b6","size":10,"current":1,"operation":"operation","insertTime":"Fri May 25 10:52:47 CST 2018","insertTimeDate":"2018-05-25","insertTimeTime":"2018-05-25 10:52:47"}]
     * obj : null
     * current : 3
     * size : 10
     * total : 25
     * pages : 3
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
         * comment : 买卡
         * amount : 100
         * userId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * dictTypeName : 日常用品
         * dictType : 100300
         * dictStatusName : 成功
         * dictStatus : 100001
         * commodityName : 买卡
         * id : ddff4f16d75e488987ff909389bdedf9
         * size : 10
         * current : 1
         * operation : operation
         * insertTime : Fri May 25 11:47:31 CST 2018
         * insertTimeDate : 2018-05-25
         * insertTimeTime : 2018-05-25 11:47:31
         */

        private String comment;
        private String amount;
        private String userId;
        private String dictTypeName;
        private String dictType;
        private String dictStatusName;
        private String dictStatus;
        private String commodityName;
        private String id;
        private int size;
        private int current;
        private String operation;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
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
