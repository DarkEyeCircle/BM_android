package com.askia.coremodel.datamodel.http.entities;

public class BuyCardData {


    /**
     * code : 1000
     * msg : 购买成功
     * list : null
     * obj : {"comment":"购买了一张卡","integral":2500,"faceValue":500,"coverUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png","userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","integralProportion":"5","id":"a16022623f5c4e9a963a5bd7fa8e06d6","size":10,"operation":"operation","current":1}
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
         * comment : 购买了一张卡
         * integral : 2500
         * faceValue : 500
         * coverUrl : http://baimeng.oss-cn-beijing.aliyuncs.com/1528705383678.png
         * userId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * integralProportion : 5
         * id : a16022623f5c4e9a963a5bd7fa8e06d6
         * size : 10
         * operation : operation
         * current : 1
         */

        private String comment;
        private String integral;
        private String faceValue;
        private String coverUrl;
        private String userId;
        private String integralProportion;
        private String id;
        private int size;
        private String operation;
        private int current;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getIntegralProportion() {
            return integralProportion;
        }

        public void setIntegralProportion(String integralProportion) {
            this.integralProportion = integralProportion;
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
    }
}
