package com.askia.coremodel.datamodel.http.entities;

public class MoneyStatistics {

    /**
     * code : 1000
     * msg : true
     * list : null
     * obj : {"dateStr":null,"totalWithdrawAmount":0,"totalRechargeAmount":0,"totalTransferAmount":0,"totalConsumeAmount":0}
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

    public static class ObjBean {
        /**
         * dateStr : null
         * totalWithdrawAmount : 0
         * totalRechargeAmount : 0
         * totalTransferAmount : 0
         * totalConsumeAmount : 0
         */

        private String dateStr;
        private double totalWithdrawAmount; //提现总额
        private double totalRechargeAmount; //充值总额
        private double totalTransferAmount; //转账总额
        private double totalConsumeAmount; //消费总额

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public double getTotalWithdrawAmount() {
            return totalWithdrawAmount;
        }

        public void setTotalWithdrawAmount(double totalWithdrawAmount) {
            this.totalWithdrawAmount = totalWithdrawAmount;
        }

        public double getTotalRechargeAmount() {
            return totalRechargeAmount;
        }

        public void setTotalRechargeAmount(double totalRechargeAmount) {
            this.totalRechargeAmount = totalRechargeAmount;
        }

        public double getTotalTransferAmount() {
            return totalTransferAmount;
        }

        public void setTotalTransferAmount(double totalTransferAmount) {
            this.totalTransferAmount = totalTransferAmount;
        }

        public double getTotalConsumeAmount() {
            return totalConsumeAmount;
        }

        public void setTotalConsumeAmount(double totalConsumeAmount) {
            this.totalConsumeAmount = totalConsumeAmount;
        }
    }
}
