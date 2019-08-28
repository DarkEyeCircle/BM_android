package com.askia.coremodel.datamodel.http.parm;

public class WithDrawalParm {


 /*
	 * 接口说明：提现功能
	 *
	 * 请求方式：POST
	 *
    	 * 请求路径：/walletDetail/withdrawDeposit
	 *
	 * 传入参数：
    	 * 	提现金额：money
    	 * 		输入的金额（校验是否为空，是否超过钱包余额，截取空格，是否为小于等于0.1）
    	 *
    	 * 	钱包类型：dictType
    	 * 		WALLET_BM = 100200; - 百盟钱包
    	 * 		WALLET_MERCHANT = 100205; - 商铺钱包
    	 * 		WALLET_MENMBER = 100210; - 会员钱包
	 *
    	 * 	提现渠道类型：dictChannelType
    	 * 		CHANNEL_WECHAT = 100260; - 微信
    	 * 		CHANNEL_UPAY = 100265; - 银联
    	 * 		CHANNEL_ALIPAY = 100270; - 支付宝
    	 * 		CHANNEL_BM = 100275; - 百盟/商铺钱包
    	 *
		时间方式：incomeTime
			TIMEMODE_SEVEN  =  101115  =  7天
			TIMEMODE_THIRTY =  101116  =  1个月

	 *
	 * 返回结果：
	 *	code:900000 失败
	 *      msg:失败消息
	 *
	 *	code:1000 成功
	 *      msg:成功消息
	 *
	 *		||
	 *
	 *	{
    "operation": "baimeng",

	 *	     "msgName": null,

	 *	     "code": "1000",

	 *	     "gsys": "gsys",

	 *	     "msg": "百盟钱包提现成功！！！"

	 *	}
    	 */


    private String money;
    private String dictType;
    private String dictChannelType;
    private String incomeTime;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictChannelType() {
        return dictChannelType;
    }

    public void setDictChannelType(String dictChannelType) {
        this.dictChannelType = dictChannelType;
    }

    public String getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(String incomeTime) {
        this.incomeTime = incomeTime;
    }
}
