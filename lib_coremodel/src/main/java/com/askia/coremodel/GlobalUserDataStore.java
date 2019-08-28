package com.askia.coremodel;

import android.text.TextUtils;

import com.askia.coremodel.datamodel.http.entities.ResponseCode;
import com.askia.coremodel.datamodel.http.entities.User;

public class GlobalUserDataStore {

    private static GlobalUserDataStore mInstance = null;
    private boolean isLogin = false; //是否已登陆

    private GlobalUserDataStore() {
    }

    public static synchronized GlobalUserDataStore getInstance() {
        if (mInstance == null) {
            synchronized (GlobalUserDataStore.class) {
                if (mInstance == null) {
                    mInstance = new GlobalUserDataStore();
                }
            }
        }
        return mInstance;
    }


    //是否为男性
    public boolean isMan() {
        return dictSex == ResponseCode.SEX_MAN;
    }

    //是否实名认证
    public boolean isIdentityApprove() {
        return authentication == ResponseCode.RealNameAuthentication;
    }

    //是否绑定银行卡
    public boolean isBindedBankCard() {
        return bindingBankCard == ResponseCode.BindedBankCard;
    }

    //是否设置过安全密码
    public boolean isHaveSecurityPwd() {
        return !TextUtils.isEmpty(securityPassword);
    }

    //是否绑定支付宝
    public boolean isBindAlippayAccount() {
        return !TextUtils.isEmpty(alipayAccount);
    }

    //是否为中性
    public boolean isNeutralPeople() {
        return dictSex == ResponseCode.SEX_NEUTRAL;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void updateUserData(User.ObjBean objBean) {
        isLogin = true;
        //数据转换
        setAuthCode(objBean.getAuthCode());
        setRealName(objBean.getRealName());
        setIdentifier(objBean.getIdentifier());
        setAuthentication(objBean.getAuthentication());

        setIntroduceCode(objBean.getIntroduceCode());
        setMobile(objBean.getMobile());
        setDictAccountStatusName(objBean.getDictAccountStatusName());
        setDictAccountStatus(objBean.getDictAccountStatus());
        setSettlementProportion(objBean.getSettlementProportion());

        setSecurityPassword(objBean.getSecurityPassword());
        setDictLevelName(objBean.getDictLevelName());
        setDictLevel(objBean.getDictLevel());
        setAgentName(objBean.getAgentName());
        setIntro(objBean.getIntro());

        setArea(objBean.getArea());
        setDictStatusName(objBean.getDictStatusName());
        setDictStatus(objBean.getDictStatus());
        setBirthday(objBean.getBirthday());
        setAge(objBean.getAge());

        setIdCard(objBean.getIdCard());
        setLastLoginTime(objBean.getLastLoginTime());
        setLastLoginTimeDate(objBean.getLastLoginTimeDate());
        setLastLoginTimeTime(objBean.getLastLoginTimeTime());
        setNickName(objBean.getNickName());

        setDictSexName(objBean.getDictSexName());
        setDictSex(objBean.getDictSex());
        setIntroducer(objBean.getIntroducer());
        setDictUserTypeName(objBean.getDictUserTypeName());
        setDictUserType(objBean.getDictUserType());

        setAlipayAccount(objBean.getAlipayAccount());
        setAvatar(objBean.getAvatar());
        setId(objBean.getId());
        setSize(objBean.getSize());
        setCurrent(objBean.getCurrent());

        setOperation(objBean.getOperation());
        setInsertTime(objBean.getInsertTime());
        setInsertTimeDate(objBean.getInsertTimeDate());
        setInsertTimeTime(objBean.getInsertTimeTime());
        setBindingBankCard(objBean.getBindingBankCard());

        setIntroducerName(objBean.getIntroducerName());
    }

    public void updateUserData(User user) {
        isLogin = true;
        setToken(user.getToken());
        User.ObjBean objBean = user.getObj();
        updateUserData(objBean);
    }

    public void clearUserInfo() {
        isLogin = false;
        token = "";
        authCode = "";
        realName = "";
        identifier = "";
        authentication = 0;

        introduceCode = "";
        mobile = "";
        dictAccountStatusName = "";
        dictAccountStatus = "";
        settlementProportion = "";


        securityPassword = "";
        dictLevelName = "";
        dictLevel = "";
        agentName = "";
        intro = "";

        area = "";
        dictStatusName = "";
        dictStatus = "";
        birthday = "";
        age = 0;

        idCard = "";
        lastLoginTime = "";
        lastLoginTimeDate = "";
        lastLoginTimeTime = "";
        nickName = "";

        dictSexName = "";
        dictSex = ResponseCode.SEX_NEUTRAL;
        introducer = "";
        dictUserTypeName = "";
        dictUserType = "";

        alipayAccount = "";
        avatar = "";
        id = "";
        size = 0;
        current = 0;

        operation = "";
        insertTime = "";
        insertTimeDate = "";
        insertTimeTime = "";
        bindingBankCard = 0;

        introducerName = "";

    }

    private String token;
    private String authCode;
    private String realName;
    private String identifier;
    private int authentication;

    private String introduceCode;
    private String mobile;
    private String dictAccountStatusName;
    private String dictAccountStatus;
    private String settlementProportion;


    private String securityPassword;
    private String dictLevelName;
    private String dictLevel;
    private String agentName;
    private String intro;

    private String area;
    private String dictStatusName;
    private String dictStatus;
    private String birthday;
    private int age;

    private String idCard;
    private String lastLoginTime;
    private String lastLoginTimeDate;
    private String lastLoginTimeTime;
    private String nickName;

    private String dictSexName;
    private int dictSex;
    private String introducer;
    private String dictUserTypeName;
    private String dictUserType;

    private String alipayAccount;
    private String avatar;
    private String id;
    private int size;
    private int current;

    private String operation;
    private String insertTime;
    private String insertTimeDate;
    private String insertTimeTime;
    private int bindingBankCard;

    private String introducerName;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getIntroduceCode() {
        return introduceCode;
    }

    public void setIntroduceCode(String introduceCode) {
        this.introduceCode = introduceCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getSettlementProportion() {
        return settlementProportion;
    }

    public void setSettlementProportion(String settlementProportion) {
        this.settlementProportion = settlementProportion;
    }

    public String getSecurityPassword() {
        return securityPassword;
    }

    public void setSecurityPassword(String securityPassword) {
        this.securityPassword = securityPassword;
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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        //默认值
        if (TextUtils.isEmpty(birthday)) {
            this.birthday = "请选择";
            return;
        }
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getIntroducer() {
        return introducer;
    }

    public void setIntroducer(String introducer) {
        this.introducer = introducer;
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

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public int getBindingBankCard() {
        return bindingBankCard;
    }

    public void setBindingBankCard(int bindingBankCard) {
        this.bindingBankCard = bindingBankCard;
    }

    public String getIntroducerName() {
        return introducerName;
    }

    public void setIntroducerName(String introducerName) {
        this.introducerName = introducerName;
    }
}
