package com.askia.common.base;

/**
 * 路由path
 * <p>
 * Aty : Activity
 * Fgt : Fragment
 */

public class ARouterPath {

    //Activity----------------------------------------------------------------

    /*启动界面LaunchActivity*/
    public static final String ActivityLaunch = "/aty/launch";
    /*欢迎界面WelcomeActivity*/
    public static final String ActivityWelcome = "/aty/welcome";
    /*登陆界面LoginActivity*/
    public static final String ActivityLogin = "/aty/login";
    /*登陆界面RegisterActivity*/
    public static final String ActivityRegister = "/aty/register";
    /*主界面MainActivity*/
    public static final String ActivityMain = "/aty/main";
    /*二维码扫描界面ScanORActivity*/
    public static final String ActivityScanQR = "/aty/scanQR";
    /*付款界面PaymentActivity*/
    public static final String ActivityPayment = "/aty/payment";
    /*收款界面ProceedsActivity*/
    public static final String ActivityProceeds = "/aty/proceeds";
    /*充值界面RechargeActivity*/
    public static final String ActivityRecharge = "/aty/recharge";
    /*提现界面 WithDrawalActivity*/
    public static final String ActivityWithDrawal = "/aty/withDrawal";
    /*卡包界面CardBagActivity*/
    public static final String ActivityCardBag = "/aty/cardBag";
    /*转账界面TransferActivity*/
    public static final String ActivityTransfer = "/aty/transfer";
    /*转账到百盟钱包 TransferToBMWalletActivity*/
    public static final String ActivityTransferToBMWallet = "/aty/transferToBMWallet";
    /*转账到支付宝 TransferToAlipayActivity*/
    public static final String ActivityTransferToAlipay = "/aty/transferToAlipay";
    /*转账到银行卡 TransferToBankCardActivity*/
    public static final String ActivityTransferToBankCard = "/aty/transferToBankCard";
    /*社交圈界面SocialCircleActivity*/
    public static final String ActivitySocialCircle = "/aty/socialCircle";
    /*我的钱包界面MyWalletActivity*/
    public static final String ActivityMyWallet = "/aty/myWallet";
    /*积分账户界面IntegralAccountActivity*/
    public static final String ActivityIntegralAccount = "/aty/integralAccount";
    /*账单界面BillActivity*/
    public static final String ActivityBill = "/aty/bill";
    /*服务条款界面ServiceTermsActivity*/
    public static final String ActivityServiceTerms = "/aty/serviceTerms";
    /*购买卡片成功界面PurchaseSucceedsActivity*/
    public static final String ActivityPurchaseStates = "/aty/purchaseStates";
    /*实名认证界面IdentityApproveActivity*/
    public static final String ActivityIdentityApprove = "/aty/identityApprove";
    /*我的资料 MyInformationActivity*/
    public static final String ActivityMyInformation = "/aty/myInformation";
    /*修改昵称ModifyNickNameActivity*/
    public static final String ActivityModifyNickName = "/aty/modifyNickName";
    /*地址管理AddressManagerActivity*/
    public static final String ActivityAddressManager = "/aty/addressManager";
    /*新增地址AddAddressActivity*/
    public static final String ActivityAddAddress = "/aty/addAddress";
    /*更新地址 UpdateAddressActivity*/
    public static final String ActivityUpdateAddress = "/aty/updateAddress";
    /*银行卡管理BankCardManagerActivity*/
    public static final String ActivityBankCardManager = "/aty/bankCardManager";
    /*添加银行卡AddBankCardActivity*/
    public static final String ActivityAddBankCard = "/aty/addBankCard";
    /*设置中心SettingCenterActivity*/
    public static final String ActivitySettingCenter = "/aty/settingCenter";
    /*密码管理界面PwdManagerActivity*/
    public static final String ActivityPwdManager = "/aty/pwdManager";
    /*切换账号界面SwitchAccountActivity*/
    public static final String ActivitySwitchAccount = "/aty/switchAccount";
    /*账号管理界面 AccountManagerActivity*/
    public static final String ActivityAccountManager = "/aty/accountManager";
    /*代理界面 AgencyActivity*/
    public static final String ActivityAgency = "/aty/agency";
    /*申请加入代理界面 ApplyAgencyActivity*/
    public static final String ActivityApplyAgency = "/aty/applyAgency";
    /*推荐界面 RecommendedActivity*/
    public static final String ActivityRecommended = "/aty/recommended";
    /*我的海报界面MyPostersActivity*/
    public static final String ActivityMyPosters = "/aty/myPosters";
    /*忘记密码界面 ForgotLoginPwdActivity*/
    public static final String ActivityForgotLoginPwd = "/aty/forgotLoginPwd";
    /*修改登陆密码界面 ModifyLoginPwdActivity*/
    public static final String ActivityModifyLoginPwd = "/aty/modifyLoginPwd";
    /*绑定支付宝界面 BindingAlipayAccountActivity*/
    public static final String ActivityBindingAlipayAccount = "/aty/bindingAlipayAccount";
    /*修改支付密码界面 ModifySecurityPwdActivity*/
    public static final String ActivityModifySecurityPwd = "/aty/modifyPayPwd";
    /*找回登陆密码界面 RetrieveLoginPwdActivity*/
    public static final String ActivityRetrieveLoginPwd = "/aty/retrieveLoginPwd";
    /*找回支付密码界面 RetrieveLoginPwdActivity*/
    public static final String ActivityRetrievePayPwd = "/aty/retrieveSecurityPwd";
    //修改手机号界面 ChangeMobileActivity
    public static final String ActivityChangeMobile = "/aty/changeMobile";
    //绑定新手机号界面 BindNewMobileActivity
    public static final String ActivityBindNewMobile = "/aty/bindNewMobile";
    //账单明细界面 BillsDetailsActivity
    public static final String ActivityBillsDetails = "/aty/billsDetails";
    //消费明细界面 StatementBillsDetailsActivity
    public static final String ActivityStatementBillsDetails = "/aty/statementBillsDetails";
    //积分商城明细 IntegralMallDetailsActivity
    public static final String ActivityIntegralMallDetails = "/aty/integralMallDetails";
    //设置安全密码界面 SettingSecurityPwdActivity
    public static final String ActivitySettingSecurityPwd = "/aty/settingSecurityPwd";
    //买卡历史界面 BuyCardHistoryActivity
    public static final String ActivityBuyCardHistory = "/aty/buyCardHistory";
    /*充值界面 BuyCardActivity*/
    public static final String ActivityBuyCard = "/aty/buyCard";
    /*收款成功界面 ProceedsStatesActivity*/
    public static final String ActivityScanfProceeds = "/aty/proceedsStates";
    /*扫描成为粉丝 ScanfBecomeFansActivity*/
    public static final String ActivityScanfBecomeFans = "/aty/scanfBecomeFans";
    /*二维码扫描付款界面 ScanfPaymentActivity*/
    public static final String ActivityScanfPayment = "/aty/scanfPayment";
    /*交易状态界面 TradingStateActivity*/
    public static final String ActivityTradingState = "/aty/tradingState";
    /*头像界面 HeadImagePreviewActivity*/
    public static final String ActivityHeadImagePreview = "/aty/headImagePreview";
    //附近门店列表 NearStoreListActivity
    public static final String ActivityNearStoreList = "/aty/nearStoreList";
    /*门店详情 StoreDetailsActivity*/
    public static final String ActivityStoreDetails = "/aty/storeDetails";
    /*升级会员 UpgradeMembershipActivity*/
    public static final String ActivityUpgradeMembership = "/aty/upgradeMembership";
    /*购买会员界面 BuyMemberActivity*/
    public static final String ActivityBuyMember = "/aty/buyMember";


    //Fragment----------------------------------------------------------------

    /*首页*/
    public static final String FragmentHome = "/fgt/home";
    /*网购*/
    public static final String FragmentNetShopping = "/fgt/netShopping";
    /*乐购*/
    public static final String FragmentFunShopping = "/fgt/funShopping";
    /*我的*/
    public static final String FragmentMine = "/fgt/mine";
    /*百盟钱包界面 BMWalletFragment*/
    public static final String FragmentBMWallet = "/fgt/bmWallet";
    /*商户钱包界面 MerchantsWalletFragment*/
    public static final String FragmentMerchantsWallet = "/fgt/merchantsWallet";
    /*会员钱包界面 MembersWalletFragment*/
    public static final String FragmentMembersWallet = "/fgt/membersWallet";
    /*积分商城界面 TotalMallFragment*/
    public static final String FragmentTotalMall = "/fgt/totalMall";
    /*待激活积分界面 TotalWaitActiveFragment*/
    public static final String FragmentTotalWaitActive = "/fgt/totalWaitActive";

}
