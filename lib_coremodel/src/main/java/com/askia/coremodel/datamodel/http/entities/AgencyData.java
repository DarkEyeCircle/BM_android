package com.askia.coremodel.datamodel.http.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class AgencyData {


    /**
     * code : 1000
     * msg : 正在代理申请中
     * list : null
     * obj : {"realName":"Askia","mobile":"18704201023","idCard":"340827199508238546","userArea":"浙江省","comment":"","area":"自己","dictStatusName":"正在审核中","dictStatus":"100030","dictGradeName":"经纪人","dictGrade":"100065","userId":"020082f48a4c4c74bce199c59c8acc38","id":"ff81cc85ed2b400b8846c20f7449359e","size":10,"current":1,"operation":"operation"}
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

    public static class ObjBean implements Parcelable {
        /**
         * realName : Askia
         * mobile : 18704201023
         * idCard : 340827199508238546
         * userArea : 浙江省
         * comment :
         * area : 自己
         * dictStatusName : 正在审核中
         * dictStatus : 100030
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
        private String integral;
        private String areaTotalConsumption;
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

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getAreaTotalConsumption() {
            return areaTotalConsumption;
        }

        public void setAreaTotalConsumption(String areaTotalConsumption) {
            this.areaTotalConsumption = areaTotalConsumption;
        }

        public String getTradingProportion() {
            return tradingProportion;
        }

        public void setTradingProportion(String tradingProportion) {
            this.tradingProportion = tradingProportion;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.realName);
            dest.writeString(this.mobile);
            dest.writeString(this.idCard);
            dest.writeString(this.integral);
            dest.writeString(this.areaTotalConsumption);
            dest.writeString(this.tradingProportion);
            dest.writeString(this.userArea);
            dest.writeString(this.comment);
            dest.writeString(this.area);
            dest.writeString(this.dictStatusName);
            dest.writeString(this.dictStatus);
            dest.writeString(this.dictGradeName);
            dest.writeString(this.dictGrade);
            dest.writeString(this.userId);
            dest.writeString(this.id);
            dest.writeInt(this.size);
            dest.writeInt(this.current);
            dest.writeString(this.operation);
        }

        public ObjBean() {
        }

        protected ObjBean(Parcel in) {
            this.realName = in.readString();
            this.mobile = in.readString();
            this.idCard = in.readString();
            this.integral = in.readString();
            this.areaTotalConsumption = in.readString();
            this.tradingProportion = in.readString();
            this.userArea = in.readString();
            this.comment = in.readString();
            this.area = in.readString();
            this.dictStatusName = in.readString();
            this.dictStatus = in.readString();
            this.dictGradeName = in.readString();
            this.dictGrade = in.readString();
            this.userId = in.readString();
            this.id = in.readString();
            this.size = in.readInt();
            this.current = in.readInt();
            this.operation = in.readString();
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
