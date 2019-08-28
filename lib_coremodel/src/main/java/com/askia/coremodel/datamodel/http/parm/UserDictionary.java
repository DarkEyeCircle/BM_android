package com.askia.coremodel.datamodel.http.parm;

public class UserDictionary {

    //代理等级:全国级
    public static final int AGENCY_GRADE_ONE = 100061;

    //代理等级:省级
    public static final int AGENCY_GRADE_TWO = 100062;

    //代理等级:市级
    public static final int AGENCY_GRADE_THREE = 100063;

    //代理等级:区级
    public static final int AGENCY_GRADE_FOUR = 100064;

    //代理等级:经纪人
    public static final int AGENCY_GRADE_Five = 100065;

    //平台类型：淘宝
    public static final int PLATFORM_TB = 100110;

    //平台类型：京东
    public static final int PLATFORM_JD = 100111;

    //用户登录授权类型:密码登录
    public static final int LOGIN_AUTH_PASSWORD = 100150;

    //用户登录授权类型:微信授权
    public static final int LOGIN_AUTH_WECHAT = 100155;

    //用户登录授权类型:新浪微博授权
    public static final int LOGIN_AUTH_SINA = 100160;

    //用户登录授权类型:QQ授权
    public static final int LOGIN_AUTH_QQ = 100165;

    //钱包类型:百盟钱包
    public static final int WALLET_BM = 100200;

    //钱包类型:商户钱包
    public static final int WALLET_MERCHANT = 100205;

    //钱包类型:会员余额
    public static final int WALLET_MENMBER = 100210;

    //钱包类型:商城积分
    public static final int WALLET_STORE = 100215;

    //钱包类型:待激活积分
    public static final int WALLET_WAIT_ACTIVE = 100220;

    //资金操作类型:收款：增加
    public static final int EARNING = 100235;

    //资金操作类型:充值：增加
    public static final int RECHARGE = 100236;

    //积分操作类型:积分激活(待激活积分激活成商城积分：1、手动激活，需要手续费；2、购买的商品收货成功自动激活，不需要手续费):增加
    public static final int ACTIVATE = 100237;

    //积分操作类型:积分转换(商城积分按一定比例转换成会员余额)：增加
    public static final int CHANGE = 100238;

    //积分操作类型:赠送（获得）积分(向百盟钱包充值或购买卡赠送的商城积分；商城购买物品赠送对应商品价格的待激活积分)：增加
    public static final int GIVE = 100239;

    ////积分操作类型:代理领取积分：商城积分减少
    public static final int AGENT_AWARD = 100250;

    //积分操作类型:赠送（获得）积分(承担保证金赠送积分)：增加
    public static final int GIVE_PAY = 100244;

    //积分操作类型:获得积分(做任务获得的待激活积分)：增加
    public static final int GET = 100243;

    //积分操作类型型:奖励商城积分（用户推荐的人消费(买卡或商城消费)之后会按比例增加此用户的商城积分）：增加
    public static final int reward = 100234;

    //积分操作类型:系统手动扣除积分：商城积分减少
    public static final int SUB_SYSTEM = 100246;

    ////积分操作类型:系统手动增加积分：商城积分增加
    public static final int ADD_SYSTEM = 100247;

    //积分操作类型:积分被转换(扣除被转换的商城积分)：商城积分减少
    public static final int PASSIVE_CHANGE = 100241;

    //积分操作类型:积分被激活(扣除被激活的待激活积分)：待激活积分减少
    public static final int PASSIVE_ACTIVATE = 100242;

    //资金操作类型:支付（付款）：减少
    public static final int PAYMENT = 100230;

    //资金操作类型:用户承担保证金（付款）：减少
    public static final int PAYMENT_USER = 100231;

    //资金操作类型:商户承担保证金（付款）：减少
    public static final int PAYMENT_MERCHANT = 100232;

    //资金操作类型:转账：减少
    public static final int TRANSFER = 100240;

    //资金操作类型:领取奖励扣除手续费：减少
    public static final int AWARD_SUB = 100249;

    //资金操作类型:收账：增加
    public static final int COLLECTING = 100248;

    //资金操作类型:提现：减少
    public static final int WITHDRAW = 100245;

    //资金流动渠道类型:微信
    public static final int CHANNEL_WECHAT = 100260;

    //资金流动渠道类型:银联
    public static final int CHANNEL_UPAY = 100265;

    //资金流动渠道类型:支付宝
    public static final int CHANNEL_ALIPAY = 100270;

    //资金流动渠道类型:百盟钱包
    public static final int CHANNEL_BM = 100275;

    //资金流动渠道类型:系统
    public static final int CHANNEL_SYSTEM = 100280;

    //消费类型:日常用品
    public static final int CONSUMPTION_DAILY = 100300;

    //卡类型：积分兑换卡
    public static final int INTEGRAL_EXCHANGE = 100400;

    //期限类型：永久
    public static final int term_permanent = 100450;

    //期限类型：固定期
    public static final int term_fixed = 100451;

    //期限类型：有效期
    public static final int term_effective = 100452;

    //会员等级：普通会员
    public static final int member_zero = 100500;

    //会员等级：一级会员
    public static final int member_one = 100501;

    //会员等级：二级会员
    public static final int member_two = 100502;

    //会员等级：三级会员
    public static final int member_three = 100503;

    //用户类型：用户
    public static final int user = 100600;

    //用户类型：商户
    public static final int merchant = 100601;

    //文件类型：卡券背景图
    public static final int card_background_image = 101000;

    //文件类型：会员礼包背景图
    public static final int members_bags_background_image = 101001;


    //充值类型:微信
    public static final int RECHARGE_WECHAT = 1;

    //充值类型:支付宝
    public static final int RECHARGE_ALIPAY = 2;

    //充值类型:银联
    public static final int RECHARGE_UPAY = 3;

    //未申请代理
    public static final int APPLY_AGENCY_NOT = 100015;

    //申请代理正在审核中
    public static final int APPLY_AGENCY_AUIT = 100030;

    //申请代理成功
    public static final int APPLY_AGENCY_SUCCESS = 100040;

    //申请代理失败
    public static final int APPLY_AGENCY_FAILER = 100020;

    //会员等级
    public static final int MEMBER_LEVEL_0 = 100500;
    public static final int MEMBER_LEVEL_1 = 100501;
    public static final int MEMBER_LEVEL_2 = 100502;
    public static final int MEMBER_LEVEL_3 = 100503;
    public static final int MEMBER_LEVEL_4 = 100504;
    public static final int MEMBER_LEVEL_5 = 100505;
    public static final int MEMBER_LEVEL_6 = 100506;

    //到账时间
    public static final int TIMEMODE_SEVEN = 101115;
    public static final int TIMEMODE_THIRTY = 101116;

    //转账方式
    public static final int TRANSFER_BM = 1;
    public static final int TRANSFER_BANKCARD = 2;
    public static final int TRANSFER_ALOPAY = 3;
}
