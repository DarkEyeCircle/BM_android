package com.askia.coremodel.datamodel.http.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class NearSotreData {


    /**
     * code : 1000
     * msg : true
     * list : [{"distance":"915m","radius":3000,"name":"华盛达广场","comment":"这里就是个卖吃的广场。。。","endTime":"20:30","identifier":"764706092900000006","startTime":"9:00","master":"苏永鸿","area":"浙江省杭州市滨江区滨兴路1451","dictStatusName":"启用","dictStatus":"100000","latitude":"30.19466","longitude":"120.189125","logo":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKlPWz6sE2IicibCmPJft9wKUVuTxop1dbAwF6Q6dN2k2wPTgYb7ysO7XXjt2c82j9IQ4wqkZMBJhLA/132","businessTime":"一,二,三","businessScope":"卖吃的！！！","id":"iku639ed1bcdikj89b9a6bb298eloi89","size":10,"operation":"operation","current":1,"insertTime":"Fri Jun 22 14:56:16 CST 2018","insertTimeDate":"2018-06-22","insertTimeTime":"2018-06-22 14:56:16"}]
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

    public static class ListBean extends HomeFragmentData implements Parcelable {
        /**
         * distance : 915m
         * radius : 3000
         * name : 华盛达广场
         * comment : 这里就是个卖吃的广场。。。
         * endTime : 20:30
         * identifier : 764706092900000006
         * startTime : 9:00
         * master : 苏永鸿
         * area : 浙江省杭州市滨江区滨兴路1451
         * dictStatusName : 启用
         * dictStatus : 100000
         * latitude : 30.19466
         * longitude : 120.189125
         * logo : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKlPWz6sE2IicibCmPJft9wKUVuTxop1dbAwF6Q6dN2k2wPTgYb7ysO7XXjt2c82j9IQ4wqkZMBJhLA/132
         * businessTime : 一,二,三
         * businessScope : 卖吃的！！！
         * id : iku639ed1bcdikj89b9a6bb298eloi89
         * size : 10
         * operation : operation
         * current : 1
         * insertTime : Fri Jun 22 14:56:16 CST 2018
         * insertTimeDate : 2018-06-22
         * insertTimeTime : 2018-06-22 14:56:16
         */


        private String distance;
        private int radius;
        private String name;
        private String comment;
        private String endTime;
        private String identifier;
        private String startTime;
        private String master;
        private String area;
        private String dictStatusName;
        private String dictStatus;
        private String latitude;
        private String longitude;
        private String logo;
        private String businessTime;
        private String businessScope;
        private String id;
        private int size;
        private String operation;
        private int current;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getMaster() {
            return master;
        }

        public void setMaster(String master) {
            this.master = master;
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

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getBusinessTime() {
            return businessTime;
        }

        public void setBusinessTime(String businessTime) {
            this.businessTime = businessTime;
        }

        public String getBusinessScope() {
            return businessScope;
        }

        public void setBusinessScope(String businessScope) {
            this.businessScope = businessScope;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.distance);
            dest.writeInt(this.radius);
            dest.writeString(this.name);
            dest.writeString(this.comment);
            dest.writeString(this.endTime);
            dest.writeString(this.identifier);
            dest.writeString(this.startTime);
            dest.writeString(this.master);
            dest.writeString(this.area);
            dest.writeString(this.dictStatusName);
            dest.writeString(this.dictStatus);
            dest.writeString(this.latitude);
            dest.writeString(this.longitude);
            dest.writeString(this.logo);
            dest.writeString(this.businessTime);
            dest.writeString(this.businessScope);
            dest.writeString(this.id);
            dest.writeInt(this.size);
            dest.writeString(this.operation);
            dest.writeInt(this.current);
            dest.writeString(this.insertTime);
            dest.writeString(this.insertTimeDate);
            dest.writeString(this.insertTimeTime);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.distance = in.readString();
            this.radius = in.readInt();
            this.name = in.readString();
            this.comment = in.readString();
            this.endTime = in.readString();
            this.identifier = in.readString();
            this.startTime = in.readString();
            this.master = in.readString();
            this.area = in.readString();
            this.dictStatusName = in.readString();
            this.dictStatus = in.readString();
            this.latitude = in.readString();
            this.longitude = in.readString();
            this.logo = in.readString();
            this.businessTime = in.readString();
            this.businessScope = in.readString();
            this.id = in.readString();
            this.size = in.readInt();
            this.operation = in.readString();
            this.current = in.readInt();
            this.insertTime = in.readString();
            this.insertTimeDate = in.readString();
            this.insertTimeTime = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }
}
