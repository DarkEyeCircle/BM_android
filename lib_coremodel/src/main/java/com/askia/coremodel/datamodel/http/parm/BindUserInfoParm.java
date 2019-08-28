package com.askia.coremodel.datamodel.http.parm;

public class BindUserInfoParm {

    /**
     * {
     * "mobile":"13656634304",
     * "codeType":"binding.code", 固定参数
     * "realName":"刘智晴",
     * 地区说明：页面要求需要地区和详情地址，由于后台数据库只有area一个字段;可以把用户输入的两个字段用“,”拼接，查出时按此规则分离即可
     * "area":"安徽省安庆市",
     * "idCard":"340827199508238546",
     * "smsCode":"123456"
     * }
     */

    private String mobile;
    private String codeType = "binding.code";
    private String realName;
    private String area;
    private String idCard;
    private String smsCode;
    private String idCardFrontUrl;
    private String idCardBackUrl;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
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
}
