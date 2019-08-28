package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class TodayRecommendedData {

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

    public static class ListBean extends HomeFragmentData {

        private int imageUrl;

        private String desc;

        public int getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(int imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


}
