package com.askia.coremodel.datamodel.http.entities;

import com.askia.coremodel.util.EncryptUtils;
import com.askia.coremodel.util.JsonUtil;

public class QRUrlData {

    /**
     * code : 1000
     * msg : true
     * list : null
     * obj : XAhnqaKkSCUn+VgBHFr8ccN4PW8OLNPPiknK3aVk0UcT/FrqTMbwozdfhUWirinnlJQoQJbct4o=
     */

    private static final String KEY = "4YztMHI7PsT4rLZN";
    private int code;
    private String msg;
    private Object list;
    private String obj;
    private ObjBean objBean;

    public ObjBean parsing() {
        String data = EncryptUtils.decrypt(obj, KEY);
        objBean = JsonUtil.Str2JsonBean(data, ObjBean.class);
        return objBean;
    }

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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public ObjBean getObjBean() {
        return objBean;
    }

    public void setObjBean(ObjBean objBean) {
        this.objBean = objBean;
    }

    public static class ObjBean {

        /**
         * codeId : b81bd371383f4f409b18952747a6d02a
         * type : 1
         */

        private String codeId;
        private int type;

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
    }

}
