package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class BillDetailsData {

    /**
     * code : 1000
     * msg : true
     * list : [{"comment":"积分钱包收款交易","dictChannelTypeName":"默认状态","dictChannelType":"200000","symbol":"+","dictChangeTypeName":"积分收款","dictChangeType":"100251","dictWalletTypeName":"商城积分","dictWalletType":"100215","tradinProportion":"","integralCashback":"否","dictCheckStatusName":"待处理","dictCheckStatus":"100650","amount":100,"userId":"accab9ed1bcd4fa59b9a6bb298eb53d6","seriaNumber":"NO20180614131527661634000190","tradeNo":"67500.00","realityAmount":100,"account":"","dictStatusName":"成功","dictStatus":"100001","rewardPoints":0,"tradingObject":"18a63dadf49747a4893d07110935b4a3","walletBalance":67500,"id":"fc33034a2a7049cb96d40294fc11b0ea","size":10,"current":1,"operation":"operation","insertTime":"Thu Jun 14 13:17:38 CST 2018","insertTimeDate":"2018-06-14","insertTimeTime":"2018-06-14 13:17:38"}]
     * obj : null
     * current : 1
     * size : 10
     * total : 46
     * pages : 5
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

    public static class ListBean {
        /**
         * comment : 积分钱包收款交易
         * dictChannelTypeName : 默认状态
         * dictChannelType : 200000
         * symbol : +
         * dictChangeTypeName : 积分收款
         * dictChangeType : 100251
         * dictWalletTypeName : 商城积分
         * dictWalletType : 100215
         * tradinProportion :
         * integralCashback : 否
         * dictCheckStatusName : 待处理
         * dictCheckStatus : 100650
         * amount : 100
         * userId : accab9ed1bcd4fa59b9a6bb298eb53d6
         * seriaNumber : NO20180614131527661634000190
         * tradeNo : 67500.00
         * realityAmount : 100
         * account :
         * dictStatusName : 成功
         * dictStatus : 100001
         * rewardPoints : 0
         * tradingObject : 18a63dadf49747a4893d07110935b4a3
         * walletBalance : 67500
         * id : fc33034a2a7049cb96d40294fc11b0ea
         * size : 10
         * current : 1
         * operation : operation
         * insertTime : Thu Jun 14 13:17:38 CST 2018
         * insertTimeDate : 2018-06-14
         * insertTimeTime : 2018-06-14 13:17:38
         */

        private String comment;
        private String dictChannelTypeName;
        private String dictChannelType;
        private String symbol;
        private String dictChangeTypeName;
        private String dictChangeType;
        private String dictWalletTypeName;
        private String dictWalletType;
        private String tradinProportion;
        private String integralCashback;
        private String dictCheckStatusName;
        private String dictCheckStatus;
        private String amount;
        private String userId;
        private String seriaNumber;
        private String tradeNo;
        private String realityAmount;
        private String account;
        private String dictStatusName;
        private String dictStatus;
        private String rewardPoints;
        private String tradingObject;
        private String walletBalance;
        private String id;
        private int size;
        private int current;
        private String operation;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getDictChannelTypeName() {
            return dictChannelTypeName;
        }

        public void setDictChannelTypeName(String dictChannelTypeName) {
            this.dictChannelTypeName = dictChannelTypeName;
        }

        public String getDictChannelType() {
            return dictChannelType;
        }

        public void setDictChannelType(String dictChannelType) {
            this.dictChannelType = dictChannelType;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getDictChangeTypeName() {
            return dictChangeTypeName;
        }

        public void setDictChangeTypeName(String dictChangeTypeName) {
            this.dictChangeTypeName = dictChangeTypeName;
        }

        public String getDictChangeType() {
            return dictChangeType;
        }

        public void setDictChangeType(String dictChangeType) {
            this.dictChangeType = dictChangeType;
        }

        public String getDictWalletTypeName() {
            return dictWalletTypeName;
        }

        public void setDictWalletTypeName(String dictWalletTypeName) {
            this.dictWalletTypeName = dictWalletTypeName;
        }

        public String getDictWalletType() {
            return dictWalletType;
        }

        public void setDictWalletType(String dictWalletType) {
            this.dictWalletType = dictWalletType;
        }

        public String getTradinProportion() {
            return tradinProportion;
        }

        public void setTradinProportion(String tradinProportion) {
            this.tradinProportion = tradinProportion;
        }

        public String getIntegralCashback() {
            return integralCashback;
        }

        public void setIntegralCashback(String integralCashback) {
            this.integralCashback = integralCashback;
        }

        public String getDictCheckStatusName() {
            return dictCheckStatusName;
        }

        public void setDictCheckStatusName(String dictCheckStatusName) {
            this.dictCheckStatusName = dictCheckStatusName;
        }

        public String getDictCheckStatus() {
            return dictCheckStatus;
        }

        public void setDictCheckStatus(String dictCheckStatus) {
            this.dictCheckStatus = dictCheckStatus;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getSeriaNumber() {
            return seriaNumber;
        }

        public void setSeriaNumber(String seriaNumber) {
            this.seriaNumber = seriaNumber;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getRealityAmount() {
            return realityAmount;
        }

        public void setRealityAmount(String realityAmount) {
            this.realityAmount = realityAmount;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
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

        public String getRewardPoints() {
            return rewardPoints;
        }

        public void setRewardPoints(String rewardPoints) {
            this.rewardPoints = rewardPoints;
        }

        public String getTradingObject() {
            return tradingObject;
        }

        public void setTradingObject(String tradingObject) {
            this.tradingObject = tradingObject;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
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
    }
}
