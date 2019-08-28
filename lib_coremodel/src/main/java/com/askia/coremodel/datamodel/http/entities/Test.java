package com.askia.coremodel.datamodel.http.entities;

public class Test {


    /**
     * code : 1000
     * msg : 获取用户代理信息成功
     * list : null
     * obj : {"realName":"Askia","mobile":"18704201023","idCard":"340827199508238546","integral":0,"areaTotalConsumption":0,"tradingProportion":"0.05","userArea":"浙江省","comment":"","area":"自己","dictStatusName":"审核已通过","dictStatus":"100040","dictGradeName":"经纪人","dictGrade":"100065","userId":"020082f48a4c4c74bce199c59c8acc38","id":"ff81cc85ed2b400b8846c20f7449359e","size":10,"current":1,"operation":"operation"}
     */

    private String code;
    private String msg;
    private Object list;
    private ObjBean obj;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
         * realName : Askia
         * mobile : 18704201023
         * idCard : 340827199508238546
         * integral : 0
         * areaTotalConsumption : 0
         * tradingProportion : 0.05
         * userArea : 浙江省
         * comment :
         * area : 自己
         * dictStatusName : 审核已通过
         * dictStatus : 100040
         * dictGradeName : 经纪人
         * dictGrade : 100065
         * userId : 020082f48a4c4c74bce199c59c8acc38
         * id : ff81cc85ed2b400b8846c20f7449359e
         * size : 10
         * current : 1
         * operation : operation
         */

        private String realName;
        private String mobile;
        private String idCard;
        private int integral;
        private int areaTotalConsumption;
        private String tradingProportion;
        private String userArea;
        private String comment;
        private String area;
        private String dictStatusName;
        private String dictStatus;
        private String dictGradeName;
        private String dictGrade;
        private String userId;
        private String id;
        private int size;
        private int current;
        private String operation;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public int getAreaTotalConsumption() {
            return areaTotalConsumption;
        }

        public void setAreaTotalConsumption(int areaTotalConsumption) {
            this.areaTotalConsumption = areaTotalConsumption;
        }

        public String getTradingProportion() {
            return tradingProportion;
        }

        public void setTradingProportion(String tradingProportion) {
            this.tradingProportion = tradingProportion;
        }

        public String getUserArea() {
            return userArea;
        }

        public void setUserArea(String userArea) {
            this.userArea = userArea;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
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

        public String getDictGradeName() {
            return dictGradeName;
        }

        public void setDictGradeName(String dictGradeName) {
            this.dictGradeName = dictGradeName;
        }

        public String getDictGrade() {
            return dictGrade;
        }

        public void setDictGrade(String dictGrade) {
            this.dictGrade = dictGrade;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
    }
}
