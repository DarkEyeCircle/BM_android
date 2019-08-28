package com.askia.coremodel.datamodel.http.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.askia.coremodel.util.EncryptUtils;
import com.askia.coremodel.util.JsonUtil;

public class ScanfQRCodeResult implements Parcelable {

    private static final String KEY = "4YztMHI7PsT4rLZN";

    public static ScanfQRCodeResult parsing(String obj) {
        String data = EncryptUtils.decrypt(obj, KEY);
        return JsonUtil.Str2JsonBean(data, ScanfQRCodeResult.class);
    }

    /**
     * codeId : b81bd371383f4f409b18952747a6d02a
     * type : 1
     */

    private String codeId;
    private int type;
    private String introduceCode;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIntroduceCode() {
        return introduceCode;
    }

    public void setIntroduceCode(String introduceCode) {
        this.introduceCode = introduceCode;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.codeId);
        dest.writeInt(this.type);
        dest.writeString(this.introduceCode);
    }

    public ScanfQRCodeResult() {
    }

    protected ScanfQRCodeResult(Parcel in) {
        this.codeId = in.readString();
        this.type = in.readInt();
        this.introduceCode = in.readString();
    }

    public static final Parcelable.Creator<ScanfQRCodeResult> CREATOR = new Parcelable.Creator<ScanfQRCodeResult>() {
        @Override
        public ScanfQRCodeResult createFromParcel(Parcel source) {
            return new ScanfQRCodeResult(source);
        }

        @Override
        public ScanfQRCodeResult[] newArray(int size) {
            return new ScanfQRCodeResult[size];
        }
    };
}
