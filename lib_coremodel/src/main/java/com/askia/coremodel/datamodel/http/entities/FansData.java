package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class FansData {

    /**
     * code : 1000
     * msg : true
     * list : [{"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","realName":"呵呵呵呵","mobile":"13656634309","avatar":"https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3468481793,3455309356&fm=27&gp=0.jpg","dictSex":"111111","contribution":8816}]
     * obj : 8816
     * current : 1
     * size : 10
     * total : 1
     * pages : 1
     */

    private int code;
    private String msg;
    private String obj;
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

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
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

    public static class ListBean {
        /**
         * userId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * realName : 呵呵呵呵
         * mobile : 13656634309
         * avatar : https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3468481793,3455309356&fm=27&gp=0.jpg
         * dictSex : 111111
         * contribution : 8816
         */

        private String userId;
        private String realName;
        private String mobile;
        private String avatar;
        private String dictSex;
        private String contribution;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDictSex() {
            return dictSex;
        }

        public void setDictSex(String dictSex) {
            this.dictSex = dictSex;
        }

        public String getContribution() {
            return contribution;
        }

        public void setContribution(String contribution) {
            this.contribution = contribution;
        }
    }
}
