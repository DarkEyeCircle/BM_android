package com.askia.coremodel.datamodel.http.entities;

import java.util.List;

public class IntegralMallDetailsData {

    /**
     * code : 1000
     * msg : true
     * list : [{"completeTime":"Fri Jul 06 11:20:36 CST 2018","completeTimeDate":"2018-07-06","completeTimeTime":"2018-07-06 11:20:36","walletBalance":97000,"tradeNo":"","finalAmount":0,"comment":"待激活积分'被激活'成商城积分","dictChannelTypeName":"系统","dictChannelType":"100280","realityAmount":1000,"symbol":"-","dictChangeTypeName":"积分被激活","dictChangeType":"100242","dictWalletTypeName":null,"dictWalletType":"100220","tradinProportion":"0.08","account":"","tradingObject":"","rewardPoints":0,"userId":"1c9657ffcab3419ca771680e577382bf","seriaNumber":"NO34089918070611200000000966","amount":1000,"dictStatusName":"成功","dictStatus":"100001","dictCheckStatusName":"待处理","dictCheckStatus":"100650","integralCashback":"否","current":1,"insertTime":"Fri Jul 06 11:20:35 CST 2018","insertTimeDate":"2018-07-06","insertTimeTime":"2018-07-06 11:20:35","operation":"operation","id":"6571ec9b19b245cb9635d4fb4a7cccbb","size":10}]
     * obj : null
     * current : 1
     * size : 10
     * total : 8
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

    public static class ListBean {
        /**
         * completeTime : Fri Jul 06 11:20:36 CST 2018
         * completeTimeDate : 2018-07-06
         * completeTimeTime : 2018-07-06 11:20:36
         * walletBalance : 97000
         * tradeNo :
         * finalAmount : 0
         * comment : 待激活积分'被激活'成商城积分
         * dictChannelTypeName : 系统
         * dictChannelType : 100280
         * realityAmount : 1000
         * symbol : -
         * dictChangeTypeName : 积分被激活
         * dictChangeType : 100242
         * dictWalletTypeName : null
         * dictWalletType : 100220
         * tradinProportion : 0.08
         * account :
         * tradingObject :
         * rewardPoints : 0
         * userId : 1c9657ffcab3419ca771680e577382bf
         * seriaNumber : NO34089918070611200000000966
         * amount : 1000
         * dictStatusName : 成功
         * dictStatus : 100001
         * dictCheckStatusName : 待处理
         * dictCheckStatus : 100650
         * integralCashback : 否
         * current : 1
         * insertTime : Fri Jul 06 11:20:35 CST 2018
         * insertTimeDate : 2018-07-06
         * insertTimeTime : 2018-07-06 11:20:35
         * operation : operation
         * id : 6571ec9b19b245cb9635d4fb4a7cccbb
         * size : 10
         */

        private String completeTime;
        private String completeTimeDate;
        private String completeTimeTime;
        private String walletBalance;
        private String tradeNo;
        private String finalAmount;
        private String comment;
        private String dictChannelTypeName;
        private String dictChannelType;
        private String realityAmount;
        private String symbol;
        private String dictChangeTypeName;
        private String dictChangeType;
        private String dictWalletTypeName;
        private String dictWalletType;
        private String tradinProportion;
        private String account;
        private String tradingObject;
        private String rewardPoints;
        private String userId;
        private String seriaNumber;
        private String amount;
        private String dictStatusName;
        private String dictStatus;
        private String dictCheckStatusName;
        private String dictCheckStatus;
        private String integralCashback;
        private int current;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;
        private String operation;
        private String id;
        private int size;

        public String getCompleteTime() {
            return completeTime;
        }

        public void setCompleteTime(String completeTime) {
            this.completeTime = completeTime;
        }

        public String getCompleteTimeDate() {
            return completeTimeDate;
        }

        public void setCompleteTimeDate(String completeTimeDate) {
            this.completeTimeDate = completeTimeDate;
        }

        public String getCompleteTimeTime() {
            return completeTimeTime;
        }

        public void setCompleteTimeTime(String completeTimeTime) {
            this.completeTimeTime = completeTimeTime;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getFinalAmount() {
            return finalAmount;
        }

        public void setFinalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
        }

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

        public String getRealityAmount() {
            return realityAmount;
        }

        public void setRealityAmount(String realityAmount) {
            this.realityAmount = realityAmount;
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

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getTradingObject() {
            return tradingObject;
        }

        public void setTradingObject(String tradingObject) {
            this.tradingObject = tradingObject;
        }

        public String getRewardPoints() {
            return rewardPoints;
        }

        public void setRewardPoints(String rewardPoints) {
            this.rewardPoints = rewardPoints;
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

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
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

        public String getIntegralCashback() {
            return integralCashback;
        }

        public void setIntegralCashback(String integralCashback) {
            this.integralCashback = integralCashback;
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

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
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
    }
}
