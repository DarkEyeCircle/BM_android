package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

//卡片(卡包)
public class CardsData {


    /**
     * code : 1000
     * msg : true
     * list : [{"number":10,"name":"积分兑换卡","comment":"用来兑换商城积分","startTime":"Thu Jun 14 12:00:00 CST 2018","startTimeDate":"2018-06-14","startTimeTime":"2018-06-14 12:00:00","endTime":"Tue Jul 17 12:00:00 CST 2018","endTimeDate":"2018-07-17","endTimeTime":"2018-07-17 12:00:00","dictStatusName":"启用","dictStatus":"100000","dictTypeName":"积分兑换卡","dictType":"100400","faceValue":10,"multiple":10,"dictTermTypeName":"有效期","dictTermType":"100452","coverUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png","id":"c5a38d80545e407dba353bc3d95d64cc","size":10,"operation":"operation","current":1,"insertTime":"Thu Jun 14 20:44:12 CST 2018","insertTimeDate":"2018-06-14","insertTimeTime":"2018-06-14 20:44:12"},{"number":100,"name":"积分兑换卡","comment":"用来兑换商城积分","startTime":"Sun Jun 03 12:00:00 CST 2018","startTimeDate":"2018-06-03","startTimeTime":"2018-06-03 12:00:00","endTime":"Tue Jun 12 00:00:00 CST 2018","endTimeDate":"2018-06-12","endTimeTime":"2018-06-12 00:00:00","dictStatusName":"启用","dictStatus":"100000","dictTypeName":"积分兑换卡","dictType":"100400","faceValue":400,"multiple":10,"dictTermTypeName":"有效期","dictTermType":"100452","coverUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png","id":"b182e88975554fffac1e9ac16d784e9c","size":10,"operation":"operation","current":1,"insertTime":"Mon Jun 11 17:58:22 CST 2018","insertTimeDate":"2018-06-11","insertTimeTime":"2018-06-11 17:58:22"},{"number":996,"name":"积分兑换卡","comment":"用来兑换商城积分","startTime":"Sun Jun 03 12:00:00 CST 2018","startTimeDate":"2018-06-03","startTimeTime":"2018-06-03 12:00:00","endTime":"Wed Jun 13 00:00:00 CST 2018","endTimeDate":"2018-06-13","endTimeTime":"2018-06-13 00:00:00","dictStatusName":"启用","dictStatus":"100000","dictTypeName":"积分兑换卡","dictType":"100400","faceValue":1000,"multiple":10,"dictTermTypeName":"有效期","dictTermType":"100452","coverUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png","id":"3d047d115e644536b593e662e583940d","size":10,"operation":"operation","current":1,"insertTime":"Mon Jun 11 17:35:50 CST 2018","insertTimeDate":"2018-06-11","insertTimeTime":"2018-06-11 17:35:50"}]
     * obj : null
     * current : 1
     * size : 10
     * total : 3
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
         * number : 10
         * name : 积分兑换卡
         * comment : 用来兑换商城积分
         * startTime : Thu Jun 14 12:00:00 CST 2018
         * startTimeDate : 2018-06-14
         * startTimeTime : 2018-06-14 12:00:00
         * endTime : Tue Jul 17 12:00:00 CST 2018
         * endTimeDate : 2018-07-17
         * endTimeTime : 2018-07-17 12:00:00
         * dictStatusName : 启用
         * dictStatus : 100000
         * dictTypeName : 积分兑换卡
         * dictType : 100400
         * faceValue : 10
         * multiple : 10
         * dictTermTypeName : 有效期
         * dictTermType : 100452
         * coverUrl : http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png
         * id : c5a38d80545e407dba353bc3d95d64cc
         * size : 10
         * operation : operation
         * current : 1
         * insertTime : Thu Jun 14 20:44:12 CST 2018
         * insertTimeDate : 2018-06-14
         * insertTimeTime : 2018-06-14 20:44:12
         */

        private int number;
        private String name;
        private String comment;
        private String startTime;
        private String startTimeDate;
        private String startTimeTime;
        private String endTime;
        private String endTimeDate;
        private String endTimeTime;
        private String dictStatusName;
        private String dictStatus;
        private String dictTypeName;
        private String dictType;
        private String faceValue;
        private String multiple;
        private String dictTermTypeName;
        private String dictTermType;
        private String coverUrl;
        private String id;
        private int size;
        private String operation;
        private int current;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartTimeDate() {
            return startTimeDate;
        }

        public void setStartTimeDate(String startTimeDate) {
            this.startTimeDate = startTimeDate;
        }

        public String getStartTimeTime() {
            return startTimeTime;
        }

        public void setStartTimeTime(String startTimeTime) {
            this.startTimeTime = startTimeTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEndTimeDate() {
            return endTimeDate;
        }

        public void setEndTimeDate(String endTimeDate) {
            this.endTimeDate = endTimeDate;
        }

        public String getEndTimeTime() {
            return endTimeTime;
        }

        public void setEndTimeTime(String endTimeTime) {
            this.endTimeTime = endTimeTime;
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

        public String getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(String faceValue) {
            this.faceValue = faceValue;
        }

        public String getMultiple() {
            return multiple;
        }

        public void setMultiple(String multiple) {
            this.multiple = multiple;
        }

        public String getDictTermTypeName() {
            return dictTermTypeName;
        }

        public void setDictTermTypeName(String dictTermTypeName) {
            this.dictTermTypeName = dictTermTypeName;
        }

        public String getDictTermType() {
            return dictTermType;
        }

        public void setDictTermType(String dictTermType) {
            this.dictTermType = dictTermType;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
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

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
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
