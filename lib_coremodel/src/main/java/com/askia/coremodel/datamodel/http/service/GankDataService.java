package com.askia.coremodel.datamodel.http.service;

import com.askia.coremodel.datamodel.http.entities.AddressData;
import com.askia.coremodel.datamodel.http.entities.AgencyData;
import com.askia.coremodel.datamodel.http.entities.BandCardData;
import com.askia.coremodel.datamodel.http.entities.BankCardInfoData;
import com.askia.coremodel.datamodel.http.entities.BaseResponseData;
import com.askia.coremodel.datamodel.http.entities.BillDetailsData;
import com.askia.coremodel.datamodel.http.entities.BuyCardData;
import com.askia.coremodel.datamodel.http.entities.BuyMemberData;
import com.askia.coremodel.datamodel.http.entities.CardsData;
import com.askia.coremodel.datamodel.http.entities.FansData;
import com.askia.coremodel.datamodel.http.entities.IntegralMallDetailsData;
import com.askia.coremodel.datamodel.http.entities.MemberUpdateBagsData;
import com.askia.coremodel.datamodel.http.entities.MoneyStatistics;
import com.askia.coremodel.datamodel.http.entities.NearSotreData;
import com.askia.coremodel.datamodel.http.entities.PaymentData;
import com.askia.coremodel.datamodel.http.entities.PaymentScaleData;
import com.askia.coremodel.datamodel.http.entities.ProceedsData;
import com.askia.coremodel.datamodel.http.entities.QRUrlData;
import com.askia.coremodel.datamodel.http.entities.RechargeOrderData;
import com.askia.coremodel.datamodel.http.entities.ScanfPaymentData;
import com.askia.coremodel.datamodel.http.entities.ScanfProceedsData;
import com.askia.coremodel.datamodel.http.entities.StatementBillDetailsData;
import com.askia.coremodel.datamodel.http.entities.User;
import com.askia.coremodel.datamodel.http.entities.UserCardsData;
import com.askia.coremodel.datamodel.http.entities.UserInfoByQRCode;
import com.askia.coremodel.datamodel.http.entities.WalletBalanceData;
import com.askia.coremodel.datamodel.http.entities.WithDrawalBalanceAndScaleData;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface GankDataService {

    //登陆
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("userLogin/web")
    Observable<User> GetLoginData(@Body JsonObject jsonObject);

    //放二次提交
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("alpha/get")
    Observable<BaseResponseData> GetAlpha(@Body JsonObject empthJsonObject);

    //注册
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/register/phoneCheck/reCheck")
    Observable<BaseResponseData> RegistUserAccount(@Header("alpha") String token, @Body JsonObject jsonObject);

    //获取验证码-注册
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/register/sendCode")
    Observable<BaseResponseData> SendSmsCodeForRegist(@Body JsonObject jsonObject);

    //获取验证码-找回登陆密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/retrieve/sendCode")
    Observable<BaseResponseData> SendSmsCodeForRetrieveLoginPwd(@Body JsonObject jsonObject);

    //找回登陆密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/retrieve/phoneCheck")
    Observable<BaseResponseData> RetrieveLoginPwd(@Body JsonObject jsonObject);

    //信息绑定（实名认证）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/binding/phoneCheck")
    Observable<User> BindUserInfo(@Header("token") String token, @Body JsonObject jsonObject);

    //信息绑定（实名认证）发送验证码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/binding/sendCode")
    Observable<BaseResponseData> SendSmsCodeForBinding(@Body JsonObject jsonObject);

    //绑定支付宝账户
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/bindingAlipayAccount")
    Observable<BaseResponseData> BindAlipayAccount(@Header("token") String token, @Body JsonObject jsonObject);

    //修改登陆密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/change/password")
    Observable<BaseResponseData> ModifyLoginPwd(@Header("token") String token, @Body JsonObject jsonObject);

    //修改安全密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/change/securityPassword")
    Observable<BaseResponseData> ModifySecurityPwd(@Header("token") String token, @Body JsonObject jsonObject);

    //获取验证码-找回安全密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/retrieveSecurityPassword/sendCode")
    Observable<BaseResponseData> SendSmsCodeForRetrieveSecurityPwd(@Body JsonObject jsonObject);

    //找回安全密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/retrieveSecurityPassword/phoneCheck")
    Observable<BaseResponseData> RetrieveSecurityPwd(@Body JsonObject jsonObject);

    //修改个人信息
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/change")
    Observable<User> ModifyUserInfo(@Header("token") String token, @Body JsonObject jsonObject);


    //查询已经绑定的银行卡
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userBank/query")
    Observable<BandCardData> GetUserBankCardData(@Header("token") String token, @Body JsonObject jsonObject);


    //绑定银行卡
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userBank/binding/phoneCheck")
    Observable<BaseResponseData> BindUserCard(@Header("token") String token, @Body JsonObject jsonObject);

    //绑定银行卡发送验证码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userBank/binding/sendCode")
    Observable<BaseResponseData> SendSmsForBindCard(@Header("token") String token, @Body JsonObject jsonObject);

    // 根据银行卡号获取银行卡信息
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userBank/getBankInfo")
    Observable<BankCardInfoData> GetBankCardInfo(@Header("token") String token, @Body JsonObject jsonObject);

    //解绑银行卡
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userBank/unbind ")
    Observable<BaseResponseData> UnbindUserBankCard(@Header("token") String token, @Body JsonObject jsonObject);

    //修改手机号发送验证码:验证原来的手机号
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/changeMobile/sendCode")
    Observable<BaseResponseData> SendSmsForChangeMobile(@Header("token") String token, @Body JsonObject jsonObject);

    //修改手机号发送验证码：验证更换的新手机号
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/determineMobile/sendCode")
    Observable<BaseResponseData> SendSmsForChangeNewMobile(@Header("token") String token, @Body JsonObject jsonObject);

    //修改手机号:验证原来的手机号
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/changeMobile/phoneCheck")
    Observable<BaseResponseData> CheckOldMobileForChangeMobile(@Header("token") String token, @Body JsonObject jsonObject);

    //修改手机号:确认修改
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/determineMobile/phoneCheck")
    Observable<User> BindNewMobile(@Header("token") String token, @Body JsonObject jsonObject);

    //新增用户收货地址
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/address/insertAddress")
    Observable<BaseResponseData> AddUserAddress(@Header("token") String token, @Body JsonObject jsonObject);

    //获取用户收货地址
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/address/getAddress")
    Observable<AddressData> GetAddress(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //删除用户收货地址
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/address/deleteAddress")
    Observable<BaseResponseData> DelUserAddress(@Header("token") String token, @Body JsonObject JsonObject);

    //更新用户收货地址
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/address/updateAddress")
    Observable<BaseResponseData> UpdateUserAddress(@Header("token") String token, @Body JsonObject JsonObject);

    //通过ID查询地址信息
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/address/queryAddress")
    Observable<BaseResponseData> QueryUserAddress(@Header("token") String token, @Body JsonObject JsonObject);

    //获取资金统计（提现总额和充值总额）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/getFundStatistics")
    Observable<MoneyStatistics> GetMoneyStatistics(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //获取总的消费明细
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/consumptionDetail/getConsumptionDetail")
    Observable<StatementBillDetailsData> GetConsumptionDetail(@Header("token") String token, @Body JsonObject jsonObject);

    //获取积分商城明细
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/getIntegralStoreDetail")
    Observable<IntegralMallDetailsData> GetIntegralMallDetails(@Header("token") String token, @Body JsonObject jsonObject);

    //获取百盟、商户、会员钱包总明细(钱包明细)
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/getDetailByWalletType")
    Observable<BillDetailsData> GetAllWalletDetails(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //根据Type获取某一个钱包明细
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/getDetailByWalletType")
    Observable<BillDetailsData> GetOneWalletDetails(@Header("token") String token, @Body JsonObject jsonObject);

    //根据Type获取资金操作类型获取相关明细（充值提现收款转账明細）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/getDetailByChangeType")
    Observable<BillDetailsData> GetDetailByChangeType(@Header("token") String token, @Body JsonObject jsonObject);

    //获取钱包余额 (积分积分 待激活积分同理)
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/wallet/getWalletInfo")
    Observable<WalletBalanceData> getWalletBalance(@Header("token") String token, @Body JsonObject jsonObject);


    //激活待支付积分
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/wallet/activation")
    Observable<WalletBalanceData> ActivationWaitIntegral(@Header("token") String token, @Body JsonObject jsonObject);

    //申请加入代理
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/agent/create")
    Observable<BaseResponseData> ApplyAgency(@Header("token") String token, @Body JsonObject jsonObject);

    //获取代理信息
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/agent/getAgentInfo")
    Observable<AgencyData> GetAgentInfo(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //领取积分奖励
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/agent/getAward")
    Observable<BaseResponseData> GetAward(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //设置安全密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/setSecurityPassword/phoneCheck")
    Observable<BaseResponseData> SettingSecurityPwd(@Header("token") String token, @Body JsonObject jsonObject);

    //获取验证码-设置安全密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/setSecurityPassword/sendCode")
    Observable<BaseResponseData> SendSmsCodeForSetSecurityPwd(@Header("token") String token, @Body JsonObject jsonObject);

    //获取商城卡券
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/cardBag/query")
    Observable<CardsData> GetCardVouchersData(@Header("token") String token, @Body JsonObject jsonObject);

    //获取买卡记录
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userCardBag/getMyCards")
    Observable<UserCardsData> GetBuyCardHistoryData(@Header("token") String token, @Body JsonObject jsonObject);

    //购买卡券
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userCardBag/buyCard")
    Observable<BuyCardData> BuyCard(@Header("token") String token, @Body JsonObject jsonObject);

    //兑换积分
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/userCardBag/exchange")
    Observable<BaseResponseData> ExchangeIntegral(@Header("token") String token, @Body JsonObject jsonObject);

    //获取质保金比例
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/payMoney/getScale")
    Observable<PaymentScaleData> GetPaymentScale(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //获取固定金额付款二维码url
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/payMoney/getQRcodeUrl")
    Observable<QRUrlData> GetQRcodeUrl(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //验证安全支付密码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/payMoney/verifyPassword")
    Observable<BaseResponseData> VerifySecurityPassword(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //二维码收款
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/payMoney/pay")
    Observable<ScanfProceedsData> ProceedsForQRCode(@Header("token") String token, @Body JsonObject jsonObject);

    //获取收款二维码 如何不设置金额可不传
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/earnMoney/getQRcodeUrl")
    Observable<QRUrlData> GetProceedQRCode(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //获取收款方用户信息（头像，昵称）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/earnMoney/getUserInfo")
    Observable<UserInfoByQRCode> GetUserInfoByQRCode(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //付款(二维码扫描)
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/earnMoney/charge")
    Observable<ScanfPaymentData> PaymentForQR(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //检查付款成功与否状态（轮训方式）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/earnMoney/isSuccess")
    Observable<ProceedsData> CheckTradingStateFroPayment(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //检查收款成功与否状态（轮训方式）
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/earnMoney/isSuccess")
    Observable<PaymentData> CheckTradingStateFroProceeds(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //充值操作
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/userTopUp")
    Observable<RechargeOrderData> CreatRechargeOrder(@Header("token") String token, @Body JsonObject jsonObject);


    //获取粉丝及其贡献值
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/getFansAndTotalReward")
    Observable<FansData> GetFansData(@Header("token") String token, @Body JsonObject jsonObject);

    //获取附近门店
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/user/queryNearbyStore")
    Observable<NearSotreData> GetNearStores(@Header("token") String token, @Body JsonObject jsonObject);

    //获取会员包列表
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/member/getBags")
    Observable<MemberUpdateBagsData> GetMemberBags(@Header("token") String token, @Body JsonObject jsonObject);

    //购买会员
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/member/buyMember")
    Observable<BuyMemberData> BuyMember(@Header("token") String token, @Body JsonObject jsonObject);

    //提现操作
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/withdrawDeposit")
    Observable<BaseResponseData> WithDrawal(@Header("token") String token, @Body JsonObject jsonObject);

    //获取提现手续费比例
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/userRemainingSum")
    Observable<WithDrawalBalanceAndScaleData> GetWithDrawalBalanceAndScale(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //转账
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-payment/walletDetail/transferAccounts")
    Observable<BaseResponseData> DoTransfer(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //获取推荐二维码
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/introducer/getQrCode")
    Observable<QRUrlData> GetQrCode(@Header("token") String token, @Body JsonObject emptyJsonObject);

    //成为粉丝
    @Headers({"Content-Type: application/json;charset=UTF-8", "Accept: application/json"})
    @POST("bm-user/introducer/fans")
    Observable<BaseResponseData> BecomeFans(@Header("token") String token, @Body JsonObject jsonObject);

}
