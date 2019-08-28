package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

//用户买卡记录
public class UserCardsData {

    /**
     * code : 1000
     * msg : true
     * list : [{"comment":"购买了一张卡","integralProportion":"10","integralCashback":"是","dictCheckStatusName":"待处理","dictCheckStatus":"100650","dictStatusName":"成功","dictStatus":"100001","userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","integral":100,"faceValue":10,"coverUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png","seriaNumber":"NO20180618130154603994000292","id":"7f80d4c0d8dc4e2ebb3bacdfd413d09e","size":10,"insertTime":"Mon Jun 18 13:01:54 CST 2018","insertTimeDate":"2018-06-18","insertTimeTime":"2018-06-18 13:01:54","operation":"operation","current":1}]
     * obj : null
     */

    private int code;
    private String msg;
    private Object obj;
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * comment : 购买了一张卡
         * integralProportion : 10
         * integralCashback : 是
         * dictCheckStatusName : 待处理
         * dictCheckStatus : 100650
         * dictStatusName : 成功
         * dictStatus : 100001
         * userId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * integral : 100
         * faceValue : 10
         * coverUrl : http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png
         * seriaNumber : NO20180618130154603994000292
         * id : 7f80d4c0d8dc4e2ebb3bacdfd413d09e
         * size : 10
         * insertTime : Mon Jun 18 13:01:54 CST 2018
         * insertTimeDate : 2018-06-18
         * insertTimeTime : 2018-06-18 13:01:54
         * operation : operation
         * current : 1
         */

        private String comment;
        private String integralProportion;
        private String integralCashback;
        private String dictCheckStatusName;
        private String dictCheckStatus;
        private String dictStatusName;
        private String dictStatus;
        private String userId;
        private String integral;
        private String faceValue;
        private String coverUrl;
        private String seriaNumber;
        private String id;
        private int size;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;
        private String operation;
        private int current;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getIntegralProportion() {
            return integralProportion;
        }

        public void setIntegralProportion(String integralProportion) {
            this.integralProportion = integralProportion;
        }

        public String getIntegralCashback() {
            return integralCashback;
        }

        public void setIntegralCashback(String integralCashback) {
            this.integralCashback = integralCashback;
        }

        public String getDictCheckStatusName() {
            return dictCheckStatusName;
        }

        public void setDictCheckStatusName(String dictCheckStatusName) {
            this.dictCheckStatusName = dictCheckStatusName;
        }

        public String getDictCheckStatus() {
            return dictCheckStatus;
        }

        public void setDictCheckStatus(String dictCheckStatus) {
            this.dictCheckStatus = dictCheckStatus;
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

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(String faceValue) {
            this.faceValue = faceValue;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getSeriaNumber() {
            return seriaNumber;
        }

        public void setSeriaNumber(String seriaNumber) {
            this.seriaNumber = seriaNumber;
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
    }
}
