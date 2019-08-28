package com.askia.coremodel.datamodel.http.entities;

public class ResponseCode {

    //服务器无响应
    public static int ServerNotResponding = -100;
    //连接超时
    public static int ConnectTimeOut = -99;
    //已被其他设备登陆-后台尚未加入
    public static int OthersLogined = -98;
    //账号已锁定-后台尚未加入
    public static int AccountLocked = -97;

    //响应成功
    public static int ResponseSuccessCode = 1000;
    //响应成功
    public static int ResponseRradingSuccessCode = 100001;
    //已实名认证
    public static int RealNameAuthentication = 1;
    //未实名认证
    public static int RealNotNameAuthentication = 0;
    //已绑定银行卡
    public static int BindedBankCard = 1;
    //未绑定银行卡
    public static int NotBindedBankCard = 0;
    //性别:男
    public static int SEX_MAN = 111110;

    //性别:女
    public static int SEX_WOMAN = 111111;

    //性别:中性
    public static int SEX_NEUTRAL = 111112;

    //一般状态：启用
    public static int USING = 100000;

    //一般状态：锁定
    public static int LOCK = 100005;

    //一般状态：禁用
    public static int DISABLE = 100010;

    //一般状态：成功
    public static int SUCCESS = 100001;

    //一般状态：失败
    public static int FAILURE = 100002;

    //审核状态：审核未通过
    public static int CHECK_NOTPASS = 100020;

    //审核状态：审核进行中
    public static int CHECK_UNDERWAY = 100030;

    //审核状态：审核已通过
    public static int CHECK_PASS = 100040;

    //默认状态
    public static int DEFAULT = 200000;

    //财务审核状态：待处理
    public static int wait_handle = 100650;

    //财务审核状态：已处理
    public static int already_handle = 100651;

    //提现已到账
    public static int ALREADY_ARRIVED = 100800;

    //消息已读状态
    public static int MESSAGE_READ = 100900;

    //消息未读状态
    public static int MESSAGE_UNREAD = 100901;

    //返回状态码：操作失败
    public static int OPERATION_FAILED = 900000;

    //返回状态码：token为空
    public static int TOKEN_IS_BLANK = 900001;

    //返回状态码：token无效
    public static int TOKEN_INVALID = 900002;

    //返回状态码：token过期
    public static int token_expire = 900003;

    //返回状态码：验证码为空
    public static int code_is_blank = 900004;

    //返回状态码：验证码错误
    public static int code_error = 900005;

    //异常状态码：ContentType错误:必须是application/json
    public static int HEADERS_ERROR = 900010;

    //异常状态码：空指针
    public static int NULL_POINTER = 900011;

    //异常状态码：数据库相关异常
    public static int database_exception = 900020;

    //异常状态码：json格式错误或转换异常
    public static int json_exception = 900021;

    //异常状态码：类相关异常
    public static int class_exception = 900022;


    //--------------充值相关状态----------------

    // 充值未支付
    public static int UNPAID = 100700;

    // 交易支付成功
    public static int PAID = 100701;

    //充值待核账(财务状态)
    public static int CHECK = 100702;

    //充值核账成功(财务状态)
    public static int CHECK_SUCCESS = 100703;

    //充值核账失败(财务状态)
    public static int CHECK_FAIL = 100704;

    // 交易支付成功，可退款【扩展字段】
    public static int REFUND = 100705;

    //--------------充值相关状态----------------


    //--------------转账相关状态----------------

    // 转账等待中：
    public static int TRANSFER_WAIT = 101111;

    // 转账失败：
    public static int TRANSFER_FALSE = 101112;

    // 转账/提现成功：
    public static int TRANSFER_TRUE = 101113;

    // 立即提现：
    public static int WITHDRAW_GO = 101114;

    // 24小时提现
    public static int WITHDRAW_ONE = 101115;

    // 48小时提现
    public static int WITHDRAW_TWO = 101116;

    // 72小时提现
    public static int WITHDRAW_THREE = 101117;

    //--------------转账相关状态---------------

}
