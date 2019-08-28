package com.askia.coremodel.datamodel.http.entities;

public class WithDrawalBalanceAndScaleData {


    /**
     * code : 1000
     * msg : true
     * list : null
     * obj : {"bmBalance":1000000,"merchantBalance":999964,"memberBalance":1000000,"bmTransferBm":"6%","bmTransferThird":"8%","bmWithdrawThird":"8%","shopWithdrawThird":"0.78%","memberWithdrawBm":"6%","memberWithdrawThird":"13%"}
     */

    private int code;
    private String msg;
    private Object list;
    private ObjBean obj;

    /**
     * {
     * "code": "1000",
     * "msg": "true",
     * "list": null,
     * "obj": {
     * "bmBalance": 1000000,
     * "merchantBalance": 999964,
     * "memberBalance": 1000000,
     * "bmTransferBm":6%,
     * "bmTransferThird":8%,
     * "bmWithdrawThird":8%,
     * "shopWithdrawThird":0.78%,
     * "memberWithdrawBm":6%,
     * "memberWithdrawThird":13%
     * }
     * }
     */

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
         * bmBalance : 1000000
         * merchantBalance : 999964
         * memberBalance : 1000000
         * bmTransferBm : 6%
         * bmTransferThird : 8%
         * bmWithdrawThird : 8%
         * shopWithdrawThird : 0.78%
         * memberWithdrawBm : 6%
         * memberWithdrawThird : 13%
         */

        private String bmBalance;
        private String merchantBalance;
        private String memberBalance;
        private String bmTransferBm;
        private String bmTransferThird;
        private String bmWithdrawThird;
        private String shopWithdrawThird;
        private String memberWithdrawBm;
        private String memberWithdrawThird;

        public String getBmBalance() {
            return bmBalance;
        }

        public void setBmBalance(String bmBalance) {
            this.bmBalance = bmBalance;
        }

        public String getMerchantBalance() {
            return merchantBalance;
        }

        public void setMerchantBalance(String merchantBalance) {
            this.merchantBalance = merchantBalance;
        }

        public String getMemberBalance() {
            return memberBalance;
        }

        public void setMemberBalance(String memberBalance) {
            this.memberBalance = memberBalance;
        }

        public String getBmTransferBm() {
            return bmTransferBm;
        }

        public void setBmTransferBm(String bmTransferBm) {
            this.bmTransferBm = bmTransferBm;
        }

        public String getBmTransferThird() {
            return bmTransferThird;
        }

        public void setBmTransferThird(String bmTransferThird) {
            this.bmTransferThird = bmTransferThird;
        }

        public String getBmWithdrawThird() {
            return bmWithdrawThird;
        }

        public void setBmWithdrawThird(String bmWithdrawThird) {
            this.bmWithdrawThird = bmWithdrawThird;
        }

        public String getShopWithdrawThird() {
            return shopWithdrawThird;
        }

        public void setShopWithdrawThird(String shopWithdrawThird) {
            this.shopWithdrawThird = shopWithdrawThird;
        }

        public String getMemberWithdrawBm() {
            return memberWithdrawBm;
        }

        public void setMemberWithdrawBm(String memberWithdrawBm) {
            this.memberWithdrawBm = memberWithdrawBm;
        }

        public String getMemberWithdrawThird() {
            return memberWithdrawThird;
        }

        public void setMemberWithdrawThird(String memberWithdrawThird) {
            this.memberWithdrawThird = memberWithdrawThird;
        }
    }
}
