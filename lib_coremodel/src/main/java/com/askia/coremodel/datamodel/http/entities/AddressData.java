package com.askia.coremodel.datamodel.http.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddressData {


    /**
     * code : 1000
     * msg : 获取地址列表成功
     * list : null
     * obj : [{"address":"mbhkhhkjhkj","name":"Askia","mobile":"187 0420 1023","dictStatusName":"启用","dictStatus":"100000","userId":"020082f48a4c4c74bce199c59c8acc38","area":"浙江省杭州市滨江区","isDefault":0,"id":"220be9dbdaf844bfa24603f392df4daa","size":10,"current":1,"operation":"operation","insertTime":"Tue Jun 12 18:07:05 CST 2018","insertTimeDate":"2018-06-12","insertTimeTime":"2018-06-12 18:07:05"}]
     */
    private static final int IsDefaultAddr = 1;
    private static final int NotDefaultAddr = 0;

    private int code;
    private String msg;
    private Object list;
    private List<ObjBean> obj;

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

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Parcelable {
        /**
         * address : mbhkhhkjhkj
         * name : Askia
         * mobile : 187 0420 1023
         * dictStatusName : 启用
         * dictStatus : 100000
         * userId : 020082f48a4c4c74bce199c59c8acc38
         * area : 浙江省杭州市滨江区
         * isDefault : 0
         * id : 220be9dbdaf844bfa24603f392df4daa
         * size : 10
         * current : 1
         * operation : operation
         * insertTime : Tue Jun 12 18:07:05 CST 2018
         * insertTimeDate : 2018-06-12
         * insertTimeTime : 2018-06-12 18:07:05
         */

        private String address;
        private String name;
        private String mobile;
        private String dictStatusName;
        private String dictStatus;
        private String userId;
        private String area;
        private int isDefault;
        private String id;
        private int size;
        private int current;
        private String operation;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public boolean isDefault() {
            return isDefault == IsDefaultAddr ? true : false;
        }

        public void setDefault(boolean isDefault) {
            if (isDefault) {
                this.isDefault = IsDefaultAddr;
            } else {
                this.isDefault = NotDefaultAddr;
            }
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(int isDefault) {
            this.isDefault = isDefault;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.address);
            dest.writeString(this.name);
            dest.writeString(this.mobile);
            dest.writeString(this.dictStatusName);
            dest.writeString(this.dictStatus);
            dest.writeString(this.userId);
            dest.writeString(this.area);
            dest.writeInt(this.isDefault);
            dest.writeString(this.id);
            dest.writeInt(this.size);
            dest.writeInt(this.current);
            dest.writeString(this.operation);
            dest.writeString(this.insertTime);
            dest.writeString(this.insertTimeDate);
            dest.writeString(this.insertTimeTime);
        }

        public ObjBean() {
        }

        protected ObjBean(Parcel in) {
            this.address = in.readString();
            this.name = in.readString();
            this.mobile = in.readString();
            this.dictStatusName = in.readString();
            this.dictStatus = in.readString();
            this.userId = in.readString();
            this.area = in.readString();
            this.isDefault = in.readInt();
            this.id = in.readString();
            this.size = in.readInt();
            this.current = in.readInt();
            this.operation = in.readString();
            this.insertTime = in.readString();
            this.insertTimeDate = in.readString();
            this.insertTimeTime = in.readString();
        }

        public static final Parcelable.Creator<ObjBean> CREATOR = new Parcelable.Creator<ObjBean>() {
            @Override
            public ObjBean createFromParcel(Parcel source) {
                return new ObjBean(source);
            }

            @Override
            public ObjBean[] newArray(int size) {
                return new ObjBean[size];
            }
        };
    }
}
