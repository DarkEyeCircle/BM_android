package com.askia.coremodel.datamodel.http.entities;

public class User {

    /**
     * code : 1000
     * msg : 登录成功
     * list : null
     * obj : {"walletBalanceInBaiMeng":0,"merchantAccountBalance":0,"totalChargeAmount":0,"totalWithdrawAmount":0,"totalWithdrawAmountInBaiMeng":0,"transferExpenses":0,"transferIncome":0,"totalPoints":0,"toActivatePoints":0,"redeemedPointsAmount":0,"redeemedPoints":0,"introducerName":"无","authCode":"PWtIVTBsWFdsVkVjb1JVV3pGR2UxTkViMVpuVExsbWNxOWlabGhrTnlaVlc=","dictUserTypeName":"用户","dictUserType":"100600","agentName":"","settlementProportion":"","birthday":"","dictStatusName":"默认状态","dictStatus":"200000","comment":"","identifier":"378507043400000004","authentication":1,"dictAccountStatusName":"启用","dictAccountStatus":"100000","age":0,"introduceCode":"As614k","introducer":"","nickName":"老何","avatar":"http://baimeng.oss-cn-beijing.aliyuncs.com/1529745897626.png","intro":"","dictSexName":"男","dictSex":"111110","area":"","idCard":"","securityPassword":"","lastLoginTime":"Thu Jul 05 10:16:07 CST 2018","lastLoginTimeDate":"2018-07-05","lastLoginTimeTime":"2018-07-05 10:16:07","alipayAccount":"18704201023","idCardFrontUrl":"","idCardBackUrl":"","bindingBankCard":0,"mobile":"18704201023","dictLevelName":"普通会员","dictLevel":"100500","realName":"老何","insertTime":"Wed Jul 04 14:34:38 CST 2018","insertTimeDate":"2018-07-04","insertTimeTime":"2018-07-04 14:34:38","current":1,"operation":"operation","id":"9726be85decb4539ac6d869b8b16877a","size":10}
     * token : eyJhbGciOiJIUzUxMiJ9.eyJ1aWQiOiI5NzI2YmU4NWRlY2I0NTM5YWM2ZDg2OWI4YjE2ODc3YSIsImNyZWF0ZWQiOjE1MzA3NTY5Njc0NDIsImV4cCI6MTUzMDg0MzM2N30.y84jhFqmqfkwUu5VTi_G9N2zZJ332KvvIdwJKFP88sy_t4qE3d4trlkEvBhanOsO2uCS5iAVQqrNUEruQH3K6w
     */

    private int code;
    private String msg;
    private Object list;
    private ObjBean obj;
    private String token;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class ObjBean {
        /**
         * walletBalanceInBaiMeng : 0
         * merchantAccountBalance : 0
         * totalChargeAmount : 0
         * totalWithdrawAmount : 0
         * totalWithdrawAmountInBaiMeng : 0
         * transferExpenses : 0
         * transferIncome : 0
         * totalPoints : 0
         * toActivatePoints : 0
         * redeemedPointsAmount : 0
         * redeemedPoints : 0
         * introducerName : 无
         * authCode : PWtIVTBsWFdsVkVjb1JVV3pGR2UxTkViMVpuVExsbWNxOWlabGhrTnlaVlc=
         * dictUserTypeName : 用户
         * dictUserType : 100600
         * agentName :
         * settlementProportion :
         * birthday :
         * dictStatusName : 默认状态
         * dictStatus : 200000
         * comment :
         * identifier : 378507043400000004
         * authentication : 1
         * dictAccountStatusName : 启用
         * dictAccountStatus : 100000
         * age : 0
         * introduceCode : As614k
         * introducer :
         * nickName : 老何
         * avatar : http://baimeng.oss-cn-beijing.aliyuncs.com/1529745897626.png
         * intro :
         * dictSexName : 男
         * dictSex : 111110
         * area :
         * idCard :
         * securityPassword :
         * lastLoginTime : Thu Jul 05 10:16:07 CST 2018
         * lastLoginTimeDate : 2018-07-05
         * lastLoginTimeTime : 2018-07-05 10:16:07
         * alipayAccount : 18704201023
         * idCardFrontUrl :
         * idCardBackUrl :
         * bindingBankCard : 0
         * mobile : 18704201023
         * dictLevelName : 普通会员
         * dictLevel : 100500
         * realName : 老何
         * insertTime : Wed Jul 04 14:34:38 CST 2018
         * insertTimeDate : 2018-07-04
         * insertTimeTime : 2018-07-04 14:34:38
         * current : 1
         * operation : operation
         * id : 9726be85decb4539ac6d869b8b16877a
         * size : 10
         */

        private int walletBalanceInBaiMeng;
        private int merchantAccountBalance;
        private int totalChargeAmount;
        private int totalWithdrawAmount;
        private int totalWithdrawAmountInBaiMeng;
        private int transferExpenses;
        private int transferIncome;
        private int totalPoints;
        private int toActivatePoints;
        private int redeemedPointsAmount;
        private int redeemedPoints;
        private String introducerName;

        private String authCode;
        private String dictUserTypeName;
        private String dictUserType;
        private String agentName;
        private String settlementProportion;
        private String birthday;
        private String dictStatusName;
        private String dictStatus;
        private String comment;
        private String identifier;
        private int authentication;
        private String dictAccountStatusName;
        private String dictAccountStatus;
        private int age;
        private String introduceCode;
        private String introducer;
        private String nickName;
        private String avatar;
        private String intro;
        private String dictSexName;
        private int dictSex;
        private String area;
        private String idCard;
        private String securityPassword;
        private String lastLoginTime;
        private String lastLoginTimeDate;
        private String lastLoginTimeTime;
        private String alipayAccount;
        private String idCardFrontUrl;
        private String idCardBackUrl;
        private int bindingBankCard;
        private String mobile;
        private String dictLevelName;
        private String dictLevel;
        private String realName;
        private String insertTime;
        private String insertTimeDate;
        private String insertTimeTime;
        private int current;
        private String operation;
        private String id;
        private int size;

        public int getWalletBalanceInBaiMeng() {
            return walletBalanceInBaiMeng;
        }

        public void setWalletBalanceInBaiMeng(int walletBalanceInBaiMeng) {
            this.walletBalanceInBaiMeng = walletBalanceInBaiMeng;
        }

        public int getMerchantAccountBalance() {
            return merchantAccountBalance;
        }

        public void setMerchantAccountBalance(int merchantAccountBalance) {
            this.merchantAccountBalance = merchantAccountBalance;
        }

        public int getTotalChargeAmount() {
            return totalChargeAmount;
        }

        public void setTotalChargeAmount(int totalChargeAmount) {
            this.totalChargeAmount = totalChargeAmount;
        }

        public int getTotalWithdrawAmount() {
            return totalWithdrawAmount;
        }

        public void setTotalWithdrawAmount(int totalWithdrawAmount) {
            this.totalWithdrawAmount = totalWithdrawAmount;
        }

        public int getTotalWithdrawAmountInBaiMeng() {
            return totalWithdrawAmountInBaiMeng;
        }

        public void setTotalWithdrawAmountInBaiMeng(int totalWithdrawAmountInBaiMeng) {
            this.totalWithdrawAmountInBaiMeng = totalWithdrawAmountInBaiMeng;
        }

        public int getTransferExpenses() {
            return transferExpenses;
        }

        public void setTransferExpenses(int transferExpenses) {
            this.transferExpenses = transferExpenses;
        }

        public int getTransferIncome() {
            return transferIncome;
        }

        public void setTransferIncome(int transferIncome) {
            this.transferIncome = transferIncome;
        }

        public int getTotalPoints() {
            return totalPoints;
        }

        public void setTotalPoints(int totalPoints) {
            this.totalPoints = totalPoints;
        }

        public int getToActivatePoints() {
            return toActivatePoints;
        }

        public void setToActivatePoints(int toActivatePoints) {
            this.toActivatePoints = toActivatePoints;
        }

        public int getRedeemedPointsAmount() {
            return redeemedPointsAmount;
        }

        public void setRedeemedPointsAmount(int redeemedPointsAmount) {
            this.redeemedPointsAmount = redeemedPointsAmount;
        }

        public int getRedeemedPoints() {
            return redeemedPoints;
        }

        public void setRedeemedPoints(int redeemedPoints) {
            this.redeemedPoints = redeemedPoints;
        }

        public String getIntroducerName() {
            return introducerName;
        }

        public void setIntroducerName(String introducerName) {
            this.introducerName = introducerName;
        }

        public String getAuthCode() {
            return authCode;
        }

        public void setAuthCode(String authCode) {
            this.authCode = authCode;
        }

        public String getDictUserTypeName() {
            return dictUserTypeName;
        }

        public void setDictUserTypeName(String dictUserTypeName) {
            this.dictUserTypeName = dictUserTypeName;
        }

        public String getDictUserType() {
            return dictUserType;
        }

        public void setDictUserType(String dictUserType) {
            this.dictUserType = dictUserType;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getSettlementProportion() {
            return settlementProportion;
        }

        public void setSettlementProportion(String settlementProportion) {
            this.settlementProportion = settlementProportion;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
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

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public int getAuthentication() {
            return authentication;
        }

        public void setAuthentication(int authentication) {
            this.authentication = authentication;
        }

        public String getDictAccountStatusName() {
            return dictAccountStatusName;
        }

        public void setDictAccountStatusName(String dictAccountStatusName) {
            this.dictAccountStatusName = dictAccountStatusName;
        }

        public String getDictAccountStatus() {
            return dictAccountStatus;
        }

        public void setDictAccountStatus(String dictAccountStatus) {
            this.dictAccountStatus = dictAccountStatus;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getIntroduceCode() {
            return introduceCode;
        }

        public void setIntroduceCode(String introduceCode) {
            this.introduceCode = introduceCode;
        }

        public String getIntroducer() {
            return introducer;
        }

        public void setIntroducer(String introducer) {
            this.introducer = introducer;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getDictSexName() {
            return dictSexName;
        }

        public void setDictSexName(String dictSexName) {
            this.dictSexName = dictSexName;
        }

        public int getDictSex() {
            return dictSex;
        }

        public void setDictSex(int dictSex) {
            this.dictSex = dictSex;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getSecurityPassword() {
            return securityPassword;
        }

        public void setSecurityPassword(String securityPassword) {
            this.securityPassword = securityPassword;
        }

        public String getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(String lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public String getLastLoginTimeDate() {
            return lastLoginTimeDate;
        }

        public void setLastLoginTimeDate(String lastLoginTimeDate) {
            this.lastLoginTimeDate = lastLoginTimeDate;
        }

        public String getLastLoginTimeTime() {
            return lastLoginTimeTime;
        }

        public void setLastLoginTimeTime(String lastLoginTimeTime) {
            this.lastLoginTimeTime = lastLoginTimeTime;
        }

        public String getAlipayAccount() {
            return alipayAccount;
        }

        public void setAlipayAccount(String alipayAccount) {
            this.alipayAccount = alipayAccount;
        }

        public String getIdCardFrontUrl() {
            return idCardFrontUrl;
        }

        public void setIdCardFrontUrl(String idCardFrontUrl) {
            this.idCardFrontUrl = idCardFrontUrl;
        }

        public String getIdCardBackUrl() {
            return idCardBackUrl;
        }

        public void setIdCardBackUrl(String idCardBackUrl) {
            this.idCardBackUrl = idCardBackUrl;
        }

        public int getBindingBankCard() {
            return bindingBankCard;
        }

        public void setBindingBankCard(int bindingBankCard) {
            this.bindingBankCard = bindingBankCard;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDictLevelName() {
            return dictLevelName;
        }

        public void setDictLevelName(String dictLevelName) {
            this.dictLevelName = dictLevelName;
        }

        public String getDictLevel() {
            return dictLevel;
        }

        public void setDictLevel(String dictLevel) {
            this.dictLevel = dictLevel;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
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
