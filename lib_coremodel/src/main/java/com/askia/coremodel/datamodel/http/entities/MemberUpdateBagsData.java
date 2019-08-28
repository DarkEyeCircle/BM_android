package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class MemberUpdateBagsData {
    /**
     * code : 1000
     * msg : 获取会员升级包成功
     * list : [{"comment":"<p>提现单笔上限 ¥5 <\/p> <p >提现总额上限  ¥5 <\/p>  <p >享受被推荐会员奖励  5%<\/p>","title":"二级会员包","dictLevelName":"二级会员","dictLevel":"100502","dictStatusName":"启用","dictStatus":"100000","imgUrl":"http://baimeng.oss-cn-beijing.aliyuncs.com/1529748650925.png","price":0.02,"name":"二级会员包","current":1,"insertTime":"Sat Jun 30 14:48:06 CST 2018","insertTimeDate":"2018-06-30","insertTimeTime":"2018-06-30 14:48:06","operation":"operation","id":"ba704bbb385547b8b2c750309496b442","size":10}]
     * obj : 100501
     */
    private int code;
    private String msg;
    private int obj;
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

    public int getObj() {
        return obj;
    }

    public void setObj(int obj) {
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
         * comment : <p>提现单笔上限 ¥5 </p> <p >提现总额上限  ¥5 </p>  <p >享受被推荐会员奖励  5%</p>
         * title : 二级会员包
         * dictLevelName : 二级会员
         * dictLevel : 100502
         * dictStatusName : 启用
         * dictStatus : 100000
         * imgUrl : http://baimeng.oss-cn-beijing.aliyuncs.com/1529748650925.png
         * price : 0.02
         * name : 二级会员包
         * current : 1
         * insertTime : Sat Jun 30 14:48:06 CST 2018
         * insertTimeDate : 2018-06-30
         * insertTimeTime : 2018-06-30 14:48:06
         * operation : operation
         * id : ba704bbb385547b8b2c750309496b442
         * size : 10
         */

        private String comment;
        private String title;
        private String dictLevelName;
        private String dictLevel;
        private String dictStatusName;
        private String dictStatus;
        private String imgUrl;
        private String price;
        private String name;
        private int current;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;
        private String operation;
        private String id;
        private int size;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDictLevelName() {
            return dictLevelName;
        }

        public void setDictLevelName(String dictLevelName) {
            this.dictLevelName = dictLevelName;
        }

        public String getDictLevel() {
            return dictLevel;
        }

        public void setDictLevel(String dictLevel) {
            this.dictLevel = dictLevel;
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

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
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
    }
}
