package com.askia.coremodel.datamodel.http.repository;

import android.app.Application;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.askia.coremodel.GlobalUserDataStore;
import com.askia.coremodel.datamodel.database.db.UserLoginData;
import com.askia.coremodel.datamodel.database.repository.DBRepository;
import com.askia.coremodel.datamodel.http.ApiClient;
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
import com.askia.coremodel.datamodel.http.parm.AddAddressParm;
import com.askia.coremodel.datamodel.http.parm.ApplyAgencyParm;
import com.askia.coremodel.datamodel.http.parm.BillDetailsParm;
import com.askia.coremodel.datamodel.http.parm.BindNewMobileParm;
import com.askia.coremodel.datamodel.http.parm.BindUserCardParm;
import com.askia.coremodel.datamodel.http.parm.BindUserInfoParm;
import com.askia.coremodel.datamodel.http.parm.BuyCardParm;
import com.askia.coremodel.datamodel.http.parm.BuyMemberParm;
import com.askia.coremodel.datamodel.http.parm.ChangeMobileParm;
import com.askia.coremodel.datamodel.http.parm.LoginParm;
import com.askia.coremodel.datamodel.http.parm.ModifyLoginPwdParm;
import com.askia.coremodel.datamodel.http.parm.ModifySecurityPwdParm;
import com.askia.coremodel.datamodel.http.parm.NearStoreParm;
import com.askia.coremodel.datamodel.http.parm.PaymentForQR;
import com.askia.coremodel.datamodel.http.parm.PaymentForQRParm;
import com.askia.coremodel.datamodel.http.parm.RechargeParm;
import com.askia.coremodel.datamodel.http.parm.RegisterParm;
import com.askia.coremodel.datamodel.http.parm.RetrieveLoginPwdParm;
import com.askia.coremodel.datamodel.http.parm.RetrieveSecurityPwdParm;
import com.askia.coremodel.datamodel.http.parm.SettingSecurityPwdParm;
import com.askia.coremodel.datamodel.http.parm.TransferParm;
import com.askia.coremodel.datamodel.http.parm.UserDictionary;
import com.askia.coremodel.datamodel.http.parm.WithDrawalParm;
import com.askia.coremodel.datamodel.loaction.LocationData;
import com.askia.coremodel.util.JsonUtil;
import com.askia.coremodel.viewmodel.AddAddressViewModel;
import com.askia.coremodel.viewmodel.AddBankCardViewModel;
import com.askia.coremodel.viewmodel.AgencyViewModel;
import com.askia.coremodel.viewmodel.ApplyAgencyStateViewModel;
import com.askia.coremodel.viewmodel.ApplyAgencyViewModel;
import com.askia.coremodel.viewmodel.BillsDetilsViewModel;
import com.askia.coremodel.viewmodel.BindNewMobileViewModel;
import com.askia.coremodel.viewmodel.BindingAlipayAccountViewModel;
import com.askia.coremodel.viewmodel.BuyCardViewModel;
import com.askia.coremodel.viewmodel.BuyMemberViewModel;
import com.askia.coremodel.viewmodel.ChangeMobileViewModel;
import com.askia.coremodel.viewmodel.ForgotLoginPwdViewModel;
import com.askia.coremodel.viewmodel.IdentityApproveViewModel;
import com.askia.coremodel.viewmodel.LoginViewModel;
import com.askia.coremodel.viewmodel.ModifyLoginPwdViewModel;
import com.askia.coremodel.viewmodel.ModifySecurityPwdViewModel;
import com.askia.coremodel.viewmodel.PaymentViewModel;
import com.askia.coremodel.viewmodel.ProceedsViewModel;
import com.askia.coremodel.viewmodel.PurchaseStatesViewModel;
import com.askia.coremodel.viewmodel.RechargeViewModel;
import com.askia.coremodel.viewmodel.RegisterViewModel;
import com.askia.coremodel.viewmodel.RetrieveLoginPwdViewModel;
import com.askia.coremodel.viewmodel.RetrieveSecurityPwdViewModel;
import com.askia.coremodel.viewmodel.ScanfPaymentViewModel;
import com.askia.coremodel.viewmodel.SettingSecurityPwdViewModel;
import com.askia.coremodel.viewmodel.SwitchAccountViewModel;
import com.askia.coremodel.viewmodel.TransferViewModel;
import com.askia.coremodel.viewmodel.UpdateAddressViewModel;
import com.askia.coremodel.viewmodel.WalletBalanceViewModel;
import com.askia.coremodel.viewmodel.WithDrawalViewModel;
import com.baidu.location.BDLocation;
import com.google.gson.JsonObject;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 所有发送数据格式为JsonObject 而不是Json格式化字符串
 */

public class GankDataRepository {

    public static Observable<BaseResponseData> GetAlpha() {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().GetAlpha(new JsonObject());
        return responseData;
    }

    //登陆
    public static Observable<User> getLoginDataRepository(LoginViewModel.PageData pageData) {
        //构造登陆参数
        LoginParm loginParm = new LoginParm();
        loginParm.setMobile(pageData.getPhoneNum().get().replace(" ", ""));
        loginParm.setPassword(pageData.getPassWord().get());
        Observable<User> responseData = ApiClient.getGankDataService().GetLoginData(JsonUtil.JavaBean2JsonObject(loginParm));
        return responseData;
    }

    public static Observable<User> DoAutoLogin() {
        UserLoginData userLoginData = DBRepository.QueryUserLoginData();
        //构造登陆参数
        LoginParm loginParm = new LoginParm();
        loginParm.setMobile(userLoginData.getPhoneNum());
        loginParm.setPassword(userLoginData.getUserPwd());
        Observable<User> responseData = ApiClient.getGankDataService().GetLoginData(JsonUtil.JavaBean2JsonObject(loginParm));
        return responseData;
    }


    //切换账号
    public static Observable<User> getLoginDataRepository(SwitchAccountViewModel.PageData pageData) {
        //构造登陆参数
        LoginParm parm = new LoginParm();
        parm.setMobile(pageData.getPhoneNum().get().replace(" ", ""));
        parm.setPassword(pageData.getPassWord().get());
        Observable<User> responseData = ApiClient.getGankDataService().GetLoginData(JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //注册
    public static Observable<BaseResponseData> registUserAccount(RegisterViewModel.PageData pageData, String alpha) {
        RegisterParm parm = new RegisterParm();
        parm.setMobile(pageData.getPhoneNum().get().replace(" ", ""));
        parm.setIntroducer(pageData.getInviteCode().get());
        parm.setSmsCode(pageData.getVerificationCode().get());
        parm.setPassword(pageData.getPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().RegistUserAccount(alpha, JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //发送验证码-注册
    public static Observable<BaseResponseData> sendSmsCodeRepositoryForRegist(RegisterViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForRegist(JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //发送验证码-忘记登陆密码
    public static Observable<BaseResponseData> sendSmsCodeRepositoryForForgotLoginPwd(ForgotLoginPwdViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForRetrieveLoginPwd(JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //忘记登陆密码
    public static Observable<BaseResponseData> ForgotLoginPwd(ForgotLoginPwdViewModel.PageData pageData) {
        RetrieveLoginPwdParm parm = new RetrieveLoginPwdParm();
        parm.setMobile(pageData.getPhoneNum().get().replace(" ", ""));
        parm.setSmsCode(pageData.getVerificationCode().get());
        parm.setPassword(pageData.getNewPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().RetrieveLoginPwd(JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //修改登陆密码
    public static Observable<BaseResponseData> ModifyLoginPwd(ModifyLoginPwdViewModel.PageData pageData) {
        ModifyLoginPwdParm parm = new ModifyLoginPwdParm();
        parm.setPassword(pageData.getOldPassWord().get());
        parm.setNewPassword(pageData.getNewPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().ModifyLoginPwd(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //修改支付密码
    public static Observable<BaseResponseData> ModifySecurityPwd(ModifySecurityPwdViewModel.PageData pageData) {
        ModifySecurityPwdParm parm = new ModifySecurityPwdParm();
        parm.setSecurityPassword(pageData.getOldPassWord().get());
        parm.setNewSecurityPassword(pageData.getNewPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().ModifySecurityPwd(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //发送验证码-找回密码
    public static Observable<BaseResponseData> sendSmsCodeRepositoryForRetrieveLoginPwd(RetrieveLoginPwdViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForRetrieveLoginPwd(JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //找回密码
    public static Observable<BaseResponseData> retrieveLoginPwd(RetrieveLoginPwdViewModel.PageData pageData) {
        RetrieveLoginPwdParm parm = new RetrieveLoginPwdParm();
        parm.setMobile(pageData.getRealPhoneNum());
        parm.setSmsCode(pageData.getVerificationCode().get());
        parm.setPassword(pageData.getNewPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().RetrieveLoginPwd(JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //发送验证码-找回支付密码
    public static Observable<BaseResponseData> sendSmsCodeRepositoryForRetrieveSecurityPwd(RetrieveSecurityPwdViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForRetrieveSecurityPwd(JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //找回支付密码
    public static Observable<BaseResponseData> retrieveSecurityPwd(RetrieveSecurityPwdViewModel.PageData pageData) {
        RetrieveSecurityPwdParm parm = new RetrieveSecurityPwdParm();
        parm.setMobile(pageData.getRealPhoneNum());
        parm.setSmsCode(pageData.getVerificationCode().get());
        parm.setSecurityPassword(pageData.getNewPassWord().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().RetrieveSecurityPwd(JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //绑定支付宝账户
    public static Observable<BaseResponseData> BindAlipayAccount(BindingAlipayAccountViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().BindAlipayAccount(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("alipayAccount", pageData.getAccount().get()));
        return responseData;
    }

    //发送验证码-信息绑定|实名认证
    public static Observable<BaseResponseData> SendSmsCodeForBinding(IdentityApproveViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForBinding(JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //信息绑定|实名认证
    public static Observable<User> BindUserInfo(IdentityApproveViewModel.PageData pageData) {
        BindUserInfoParm parm = new BindUserInfoParm();
        parm.setMobile(pageData.getRealPhoneNum());
        parm.setSmsCode(pageData.getVerificationCode().get());
        parm.setArea(pageData.getCurCity().get() + "," + pageData.getDetailLocation().get());
        parm.setIdCard(pageData.getIDCardNum().get().replace(" ", ""));
        parm.setRealName(pageData.getRealName().get());
        parm.setIdCardFrontUrl(pageData.getIdCardImg_front_url());
        parm.setIdCardBackUrl(pageData.getIdCardImg_behind_url());
        Observable<User> responseData = ApiClient.getGankDataService().BindUserInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //修改个人信息 -头像
    public static Observable<User> ModifyUserInfoForHeadImg(String headImgUrl) {
        Observable<User> responseData = ApiClient.getGankDataService().ModifyUserInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("avatar", headImgUrl));
        return responseData;
    }


    //修改个人信息 -性别
    public static Observable<User> ModifyUserInfoForSex(int sexCode) {
        Observable<User> responseData = ApiClient.getGankDataService().ModifyUserInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("dictSex", sexCode + ""));
        return responseData;
    }

    //修改个人信息-昵称
    public static Observable<User> ModifyUserInfoForNickName(String nickName) {
        Observable<User> responseData = ApiClient.getGankDataService().ModifyUserInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("nickName", nickName));
        return responseData;
    }

    //修改个人信息-出生日期
    public static Observable<User> ModifyUserInfoForBornData(String bornDate) {
        Observable<User> responseData = ApiClient.getGankDataService().ModifyUserInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("birthday", bornDate));
        return responseData;
    }

    //查询已经绑定的银行卡
    public static Observable<BandCardData> GetUserBankCardData() {
        Observable<BandCardData> responseData = ApiClient.getGankDataService().GetUserBankCardData(GlobalUserDataStore.getInstance().getToken(), new JsonObject());
        return responseData;
    }

    //绑定银行卡
    public static Observable<BaseResponseData> BindUserCard(AddBankCardViewModel.PageData pageData) {
        BindUserCardParm parm = new BindUserCardParm();
        parm.setCardNo(pageData.getBankCardNum().get());
        parm.setMobile(pageData.getRealPhoneNum());
        parm.setOpeningBank(pageData.getBankName().get());
        parm.setSmsCode(pageData.getVerifiCode().get());
        parm.setCity(pageData.getBankCity().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().BindUserCard(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //绑定银行卡发送验证码
    public static Observable<BaseResponseData> SendSmsForBindCard(AddBankCardViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsForBindCard(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    // 根据银行卡号获取银行卡信息
    public static Observable<BankCardInfoData> GetBankCardInfo(String bankCardNum) {
        Observable<BankCardInfoData> responseData = ApiClient.getGankDataService().GetBankCardInfo(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("cardNo", bankCardNum));
        return responseData;
    }

    //解绑银行卡
    public static Observable<BaseResponseData> UnbindUserBankCard(BandCardData.ListBean listBean) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().UnbindUserBankCard(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("id", listBean.getId()));
        return responseData;
    }

    //修改手机号发送验证码:验证原来的手机号
    public static Observable<BaseResponseData> SendSmsForChangeMobile(ChangeMobileViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsForChangeMobile(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("mobile", pageData.getRealPhoneNum()));
        return responseData;
    }

    //修改手机号发送验证码：验证更换的新手机号
    public static Observable<BaseResponseData> SendSmsForChangeNewMobile(BindNewMobileViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsForChangeNewMobile(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("mobile", pageData.getPhoneNum().get().replace(" ", "")));
        return responseData;
    }

    //修改手机号:验证原来的手机号
    public static Observable<BaseResponseData> CheckOldMobileForChangeMobile(ChangeMobileViewModel.PageData pageData) {
        ChangeMobileParm parm = new ChangeMobileParm();
        parm.setMobile(pageData.getPhoneNum().get().replaceAll(" ", ""));
        parm.setSmsCode(pageData.getVerificationCode().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().CheckOldMobileForChangeMobile(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //修改手机号:确认修改
    public static Observable<User> BindNewMobile(BindNewMobileViewModel.PageData pageData) {
        BindNewMobileParm parm = new BindNewMobileParm();
        parm.setMobile(pageData.getPhoneNum().get().replaceAll(" ", ""));
        parm.setSmsCode(pageData.getVerificationCode().get());
        Observable<User> responseData = ApiClient.getGankDataService().BindNewMobile(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //新增用户收货地址
    public static Observable<BaseResponseData> AddUserAddress(AddAddressViewModel.PageData pageData) {
        AddAddressParm parm = new AddAddressParm();
        parm.setName(pageData.getName().get());
        parm.setArea(pageData.getArea().get());
        parm.setIsDefault(pageData.getDefaultAddr().get());
        parm.setAddress(pageData.getDetailAddr().get());
        parm.setMobile(pageData.getPhoneNum().get().replaceAll(" ", ""));
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().AddUserAddress(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //获取用户收货地址
    public static Observable<AddressData> GetAddressData() {
        Observable<AddressData> responseData = ApiClient.getGankDataService().GetAddress(GlobalUserDataStore.getInstance().getToken(), new JsonObject());
        return responseData;
    }

    //删除用户收货地址
    public static Observable<BaseResponseData> DelUserAddress(AddressData.ObjBean objBean) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().DelUserAddress(GlobalUserDataStore.getInstance().getToken(), JsonUtil.Str2JsonObject("id", objBean.getId()));
        return responseData;
    }

    //更新用户收货地址
    public static Observable<BaseResponseData> UpdateUserAddress(UpdateAddressViewModel.PageData pageData) {
        AddressData.ObjBean objBean = new AddressData.ObjBean();
        objBean.setId(pageData.getID());
        objBean.setArea(pageData.getArea().get());
        objBean.setAddress(pageData.getDetailAddr().get());
        objBean.setName(pageData.getName().get());
        objBean.setMobile(pageData.getPhoneNum().get().replaceAll(" ", ""));
        objBean.setDefault(pageData.getDefaultAddr().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().UpdateUserAddress(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(objBean));
        return responseData;
    }

    //设为默认收货地址
    public static Observable<BaseResponseData> SetDefaultAddress(AddressData.ObjBean item) {
        AddressData.ObjBean objBean = new AddressData.ObjBean();
        objBean.setId(item.getId());
        objBean.setArea(item.getArea());
        objBean.setAddress(item.getAddress());
        objBean.setName(item.getName());
        objBean.setMobile(item.getMobile().replaceAll(" ", ""));
        objBean.setDefault(true);
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().UpdateUserAddress(GlobalUserDataStore.getInstance().getToken(), JsonUtil.JavaBean2JsonObject(objBean));
        return responseData;
    }

    //获取资金统计（提现总额和充值总额）
    public static Observable<MoneyStatistics> GetMoneyStatistics() {
        Observable<MoneyStatistics> responseData = ApiClient.getGankDataService().GetMoneyStatistics(GlobalUserDataStore.getInstance().getToken(), new JsonObject());
        return responseData;
    }

    //获取消费明细账单
    public static Observable<StatementBillDetailsData> GetStatementBills(int curPage) {
        String pages = String.valueOf(curPage);
        Observable<StatementBillDetailsData> responseData = ApiClient.getGankDataService().GetConsumptionDetail(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("current", pages));
        return responseData;
    }

    //获取积分商城明细
    public static Observable<IntegralMallDetailsData> GetIntegralMallDetails(int curPage) {
        String pages = String.valueOf(curPage);
        Observable<IntegralMallDetailsData> responseData = ApiClient.getGankDataService().GetIntegralMallDetails(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("current", pages));
        return responseData;
    }

    //获取账单
    public static Observable<BillDetailsData> GetBills(BillsDetilsViewModel.BillType billType, int curPage) {
        String pages = String.valueOf(curPage);
        Observable<BillDetailsData> responseData = null;
        BillDetailsParm billDetailsParm = new BillDetailsParm();
        switch (billType) {
            //积分商城明细
            case PointsMallDetails:
                billDetailsParm.setType(UserDictionary.WALLET_STORE);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetOneWalletDetails(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //钱包总明细
            case WalletDetails:
                responseData = ApiClient.getGankDataService().GetAllWalletDetails(GlobalUserDataStore.getInstance().getToken(),
                        JsonUtil.Str2JsonObject("current", pages));
                break;
            //提现明细
            case DrawMoneyDetails:
                billDetailsParm.setType(UserDictionary.WITHDRAW);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetDetailByChangeType(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //商户明细
            case MerchantsDetails:
                billDetailsParm.setType(UserDictionary.WALLET_MERCHANT);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetOneWalletDetails(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //收款明细
            case CollectionDetails:
                billDetailsParm.setType(UserDictionary.COLLECTING);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetDetailByChangeType(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //充值明细
            case RechargeDetails:
                billDetailsParm.setType(UserDictionary.RECHARGE);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetDetailByChangeType(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //百盟钱包明细
            case BmWalletDetails:
                billDetailsParm.setType(UserDictionary.WALLET_BM);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetOneWalletDetails(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
            //会员钱包明细
            case MembersWalletDetails:
                billDetailsParm.setType(UserDictionary.WALLET_MENMBER);
                billDetailsParm.setCurrent(curPage);
                responseData = ApiClient.getGankDataService().GetOneWalletDetails(GlobalUserDataStore.getInstance().getToken()
                        , JsonUtil.JavaBean2JsonObject(billDetailsParm));
                break;
        }
        return responseData;
    }

    //获取钱包余额
    public static Observable<WalletBalanceData> getWalletBalance(WalletBalanceViewModel.BalanceType balanceType) {
        String type = null;
        switch (balanceType) {
            case BMWallet:  //百盟钱包余额
                type = String.valueOf(UserDictionary.WALLET_BM);
                break;
            case MerchantsWallet: //商户钱包余额
                type = String.valueOf(UserDictionary.WALLET_MERCHANT);
                break;
            case MemberWallet:   //会员钱包余额
                type = String.valueOf(UserDictionary.WALLET_MENMBER);
                break;
            case MallPoints:  //商城积分余额
                type = String.valueOf(UserDictionary.WALLET_STORE);
                break;
            case WaitActiviePoints:  //待激活积分余额
                type = String.valueOf(UserDictionary.WALLET_WAIT_ACTIVE);
                break;
        }
        Observable<WalletBalanceData> responseData = ApiClient.getGankDataService().getWalletBalance(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("type", type));
        return responseData;
    }

    //获取消费明细账单
    public static Observable<WalletBalanceData> ActivationWaitIntegral(long needActiveIntegral) {
        Observable<WalletBalanceData> responseData = ApiClient.getGankDataService().ActivationWaitIntegral(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("integral", String.valueOf(needActiveIntegral)));
        return responseData;
    }

    //申请加入代理
    public static Observable<BaseResponseData> ApplyAgency(ApplyAgencyViewModel.PageData pageData) {
        ApplyAgencyParm applyAgencyParm = new ApplyAgencyParm();
        applyAgencyParm.setArea(pageData.getAgencyArea().get());
        String agency = pageData.getAgencyLevel().get();
        ApplyAgencyViewModel.AgencyLevel agencyLevel = ApplyAgencyViewModel.AgencyLevel.getAgencyType(agency);
        switch (agencyLevel) {
            case AGENCY_GRADE_ONE:
                applyAgencyParm.setDictGrade(String.valueOf(UserDictionary.AGENCY_GRADE_ONE));
                break;
            case AGENCY_GRADE_TWO:
                applyAgencyParm.setDictGrade(String.valueOf(UserDictionary.AGENCY_GRADE_TWO));
                break;
            case AGENCY_GRADE_THREE:
                applyAgencyParm.setDictGrade(String.valueOf(UserDictionary.AGENCY_GRADE_THREE));
                break;
            case AGENCY_GRADE_FOUR:
                applyAgencyParm.setDictGrade(String.valueOf(UserDictionary.AGENCY_GRADE_FOUR));
                break;
            case AGENCY_GRADE_Five:
                applyAgencyParm.setDictGrade(String.valueOf(UserDictionary.AGENCY_GRADE_Five));
                break;
        }
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().ApplyAgency(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(applyAgencyParm));
        return responseData;
    }

    //重新申请代理
    public static Observable<BaseResponseData> ApplyAgencyAgain(ApplyAgencyStateViewModel.PageData pageData) {
        ApplyAgencyParm applyAgencyParm = new ApplyAgencyParm();
        applyAgencyParm.setArea(pageData.getAgencyArea().get());
        applyAgencyParm.setDictGrade(pageData.getAgencyLevelCode());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().ApplyAgency(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(applyAgencyParm));
        return responseData;
    }

    //获取代理信息
    public static Observable<AgencyData> GetAgentInfo() {
        Observable<AgencyData> responseData = ApiClient.getGankDataService().GetAgentInfo(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //领取积分奖励
    public static Observable<BaseResponseData> GetAward(AgencyViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().GetAward(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("integral", pageData.getIntegral().get()));
        return responseData;
    }


    //获取验证码-设置安全密码
    public static Observable<BaseResponseData> SendSmsCodeForSetSecurityPwd(SettingSecurityPwdViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SendSmsCodeForSetSecurityPwd(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("mobile", pageData.getPhoneNum().get().replace(" ", "")));
        return responseData;
    }

    //设置安全密码
    public static Observable<BaseResponseData> SettingSecurityPwd(SettingSecurityPwdViewModel.PageData pageData) {
        SettingSecurityPwdParm parm = new SettingSecurityPwdParm();
        parm.setSecurityPassword(pageData.getConfirmPwd().get());
        parm.setMobile(pageData.getPhoneNum().get().replaceAll(" ", ""));
        parm.setSmsCode(pageData.getVerificationCode().get());
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().SettingSecurityPwd(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //获取商城卡券
    public static Observable<CardsData> GetCardVouchersData(int curPage) {
        String pages = String.valueOf(curPage);
        Observable<CardsData> responseData = ApiClient.getGankDataService().GetCardVouchersData(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("current", pages));
        return responseData;
    }

    //获取买卡记录
    public static Observable<UserCardsData> GetBuyCardHistoryData(int curPage) {
        String pages = String.valueOf(curPage);
        Observable<UserCardsData> responseData = ApiClient.getGankDataService().GetBuyCardHistoryData(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("current", pages));
        return responseData;
    }

    //购买卡券
    public static Observable<BuyCardData> BuyCard(BuyCardViewModel.PageData pageData) {
        BuyCardParm buyCardParm = new BuyCardParm();
        buyCardParm.setCardId(pageData.getCardId());
        buyCardParm.setSecurityPassword(pageData.getSecurityPassword());
        Observable<BuyCardData> responseData = ApiClient.getGankDataService().BuyCard(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(buyCardParm));
        return responseData;
    }

    //兑换积分
    public static Observable<BaseResponseData> ExchangeIntegral(PurchaseStatesViewModel.PageData pageData) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().ExchangeIntegral(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("cardId", pageData.getCardId()));
        return responseData;
    }

    //获取质保金比例
    public static Observable<PaymentScaleData> GetPaymentScale() {
        Observable<PaymentScaleData> responseData = ApiClient.getGankDataService().GetPaymentScale(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //获取固定金额付款二维码url
    public static Observable<QRUrlData> GetQRcodeUrl(PaymentViewModel.PageData pageData) {
        PaymentForQR paymentForQR = new PaymentForQR();
        paymentForQR.setMoney(pageData.getPaymentMoney().get());
        switch (pageData.getMarginType()) {
            case MembersBear:
                paymentForQR.setType(String.valueOf(UserDictionary.PAYMENT_USER));
                break;
            case MerchantsBear:
                paymentForQR.setType(String.valueOf(UserDictionary.PAYMENT_MERCHANT));
                break;
        }
        Observable<QRUrlData> responseData = ApiClient.getGankDataService().GetQRcodeUrl(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(paymentForQR));
        return responseData;
    }

    //验证安全支付密码
    public static Observable<BaseResponseData> VerifySecurityPassword(String pwd) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().VerifySecurityPassword(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("password", pwd));
        return responseData;
    }

    //获取收款二维码 不设置金额
    public static Observable<QRUrlData> GetProceedQRCode() {
        Observable<QRUrlData> responseData = ApiClient.getGankDataService().GetProceedQRCode(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //获取固定金额收款二维码
    public static Observable<QRUrlData> GetProceedQRCode(ProceedsViewModel.PageData pageData) {
        Observable<QRUrlData> responseData = ApiClient.getGankDataService().GetProceedQRCode(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("money", pageData.getMoney()));
        return responseData;
    }

    //二维码收款
    public static Observable<ScanfProceedsData> ProceedsForQRCode(String codeId) {
        Observable<ScanfProceedsData> responseData = ApiClient.getGankDataService().ProceedsForQRCode(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("codeId", codeId));
        return responseData;
    }

    // 获取收款方用户信息（头像，昵称）
    public static Observable<UserInfoByQRCode> GetUserInfoByQRCode(String codeId) {
        Observable<UserInfoByQRCode> responseData = ApiClient.getGankDataService().GetUserInfoByQRCode(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("codeId", codeId));
        return responseData;
    }

    //付款(二维码扫描)
    public static Observable<ScanfPaymentData> PaymentForQR(ScanfPaymentViewModel.PageData pageData) {
        PaymentForQRParm parm = new PaymentForQRParm();
        parm.setReceiveUserId(pageData.getReceiveUserId());
        parm.setMoney(pageData.getMoney().get());
        Observable<ScanfPaymentData> responseData = ApiClient.getGankDataService().PaymentForQR(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //检查付款成功与否状态（轮训方式）
    public static Observable<ProceedsData> CheckTradingStateFroPayment() {
        Observable<ProceedsData> responseData = ApiClient.getGankDataService().CheckTradingStateFroPayment(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //检查收款成功与否状态（轮训方式）
    public static Observable<PaymentData> CheckTradingStateFroProceeds() {
        Observable<PaymentData> responseData = ApiClient.getGankDataService().CheckTradingStateFroProceeds(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //用户充值
    public static Observable<RechargeOrderData> CreatRechargeOrder(RechargeViewModel.PageData pageData) {
        RechargeParm parm = new RechargeParm();
        parm.setBalance(pageData.getPayAmount().get());
        switch (pageData.getRechargeType()) {
            case Wechat:
                parm.setPayType(String.valueOf(UserDictionary.RECHARGE_WECHAT));
                break;
            case AliPay:
                parm.setPayType(String.valueOf(UserDictionary.RECHARGE_ALIPAY));
                break;
            case Unionpay:
                parm.setPayType(String.valueOf(UserDictionary.RECHARGE_UPAY));
                break;
        }
        Observable<RechargeOrderData> responseData = ApiClient.getGankDataService().CreatRechargeOrder(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //上传头像
    public static void UploadHead(final String filePath, Application application, MaybeObserver<String> observer) {
        Maybe.create(new MaybeOnSubscribe<String>() {
            @Override
            public void subscribe(MaybeEmitter<String> emitter) {
                File file = new File(filePath);
                String fileName = System.currentTimeMillis() + file.getName();
                String endpoint = "http://oss-cn-beijing.aliyuncs.com";
                OSSCredentialProvider credentialProvider =
                        new OSSPlainTextAKSKCredentialProvider("LTAI2a6Ti1wwXlYS", "ms6umOQghallLeX6tr9qg6Ef1rfYZn");
                //该配置类如果不设置，会有默认配置，具体可看该类
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(10 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(10 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                OSSLog.enableLog();
                OSSClient oss = new OSSClient(application, endpoint, credentialProvider);
                PutObjectRequest put = new PutObjectRequest("baimeng", fileName, filePath);
                try {
                    PutObjectResult putResult = oss.putObject(put);
                    String imageUrl = "http://baimeng.oss-cn-beijing.aliyuncs.com/" + fileName;
                    emitter.onSuccess(imageUrl);
                    emitter.onComplete();
                } catch (ClientException e) {
                    // 本地异常如网络异常等
                    e.printStackTrace();
                    emitter.onError(e);
                    emitter.onComplete();
                } catch (ServiceException e) {
                    // 服务异常
                    e.printStackTrace();
                    emitter.onError(e);
                    emitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    //获取粉丝
    public static Observable<FansData> GetFansData(int curPage) {
        String pages = String.valueOf(curPage);
        Observable<FansData> responseData = ApiClient.getGankDataService().GetFansData(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("current", pages));
        return responseData;
    }

    //获取门店
    public static Observable<NearSotreData> GetNearStores(int page) {
        NearStoreParm parm = new NearStoreParm();
        BDLocation location = LocationData.getLocatonData().getLocation();
        parm.setLatitude(String.valueOf(location.getLatitude()));
        parm.setLongitude(String.valueOf(location.getLongitude()));
        parm.setLocation(location.getCity());
        parm.setSize(10);
        if (page > 0) {
            parm.setCurrent(page + "");
        }
        Observable<NearSotreData> responseData = ApiClient.getGankDataService().GetNearStores(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //获取会员包列表
    public static Observable<MemberUpdateBagsData> GetMemberBags() {
        Observable<MemberUpdateBagsData> responseData = ApiClient.getGankDataService().GetMemberBags(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }


    //提现
    public static Observable<BaseResponseData> WithDrawal(WithDrawalViewModel.PageData pageData) {
        WithDrawalParm parm = new WithDrawalParm();
        parm.setMoney(pageData.getPayAmount().get());
        switch (pageData.getWithDrawalType()) {
            case BMWithDrawa:
                parm.setDictType(String.valueOf(UserDictionary.WALLET_BM));
                break;
            case MerchantsWithDrawa:
                parm.setDictType(String.valueOf(UserDictionary.WALLET_MERCHANT));
                break;
            case MembersWithDrawa:
                parm.setDictType(String.valueOf(UserDictionary.WALLET_MENMBER));
                break;
        }
        switch (pageData.getWithDrawalAccount()) {
            case Wechat:
                parm.setDictChannelType(String.valueOf(UserDictionary.CHANNEL_WECHAT));
                break;
            case AliPay:
                parm.setDictChannelType(String.valueOf(UserDictionary.CHANNEL_ALIPAY));
                break;
            case Unionpay:
                parm.setDictChannelType(String.valueOf(UserDictionary.CHANNEL_UPAY));
                break;
            case BMWallet:
                parm.setDictChannelType(String.valueOf(UserDictionary.CHANNEL_BM));
                break;
        }
        switch (pageData.getAccountingDateType()) {
            case WithDrawalViewModel.AccountingDateType_7Days:
                parm.setIncomeTime(String.valueOf(UserDictionary.TIMEMODE_SEVEN));
                break;
            case WithDrawalViewModel.AccountingDateType_1Monthes:
                parm.setIncomeTime(String.valueOf(UserDictionary.TIMEMODE_THIRTY));
                break;

        }
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().WithDrawal(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //获取提现手续费比例
    public static Observable<WithDrawalBalanceAndScaleData> GetWithDrawalBalanceAndScale() {
        Observable<WithDrawalBalanceAndScaleData> responseData = ApiClient.getGankDataService().GetWithDrawalBalanceAndScale(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //转账
    public static Observable<BaseResponseData> DoTransfer(TransferViewModel.PageData pageData) {
        TransferParm parm = new TransferParm();
        parm.setMoney(pageData.getPayAmount().get());
        switch (pageData.getTransferType()) {
            case TransferToBM:
                parm.setTransferWay(String.valueOf(UserDictionary.TRANSFER_BM));
                parm.setAdverseAccount(pageData.getBMaccount().get().replaceAll(" ", ""));
                break;
            case TransferToAlipay:
                parm.setTransferWay(String.valueOf(UserDictionary.TRANSFER_ALOPAY));
                parm.setAlipayAccount(pageData.getAlipayAccount().get());
                parm.setAlipayName(pageData.getAlipayName().get());
                break;
            case TransferToBankCard:
                parm.setTransferWay(String.valueOf(UserDictionary.TRANSFER_BANKCARD));
                parm.setBankAccount(pageData.getBankAccount().get().replaceAll(" ", ""));
                parm.setBankName(pageData.getBankName().get());
                parm.setOpeningBank(pageData.getOpeningBank().get());
                parm.setBankCity(pageData.getBankCity().get());
                break;
        }
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().DoTransfer(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.JavaBean2JsonObject(parm));
        return responseData;
    }

    //获取转账历史
    public static Observable<BillDetailsData> GetTransferHistory(int curPage) {
        BillDetailsParm parm = new BillDetailsParm();
        parm.setCurrent(curPage);
        parm.setType(UserDictionary.TRANSFER);
        return ApiClient.getGankDataService().GetDetailByChangeType(GlobalUserDataStore.getInstance().getToken()
                , JsonUtil.JavaBean2JsonObject(parm));
    }

    //购买会员
    public static Observable<BuyMemberData> BuyMember(BuyMemberViewModel.PageData pageData) {
        BuyMemberParm parm = new BuyMemberParm();
        parm.setId(pageData.getCardId());
        parm.setPayType(pageData.getPayType());
        return ApiClient.getGankDataService().BuyMember(GlobalUserDataStore.getInstance().getToken()
                , JsonUtil.JavaBean2JsonObject(parm));
    }

    //获取推荐二维码
    public static Observable<QRUrlData> GetQrCode() {
        Observable<QRUrlData> responseData = ApiClient.getGankDataService().GetQrCode(GlobalUserDataStore.getInstance().getToken(),
                new JsonObject());
        return responseData;
    }

    //成为粉丝
    public static Observable<BaseResponseData> BecomeFans(String codeId) {
        Observable<BaseResponseData> responseData = ApiClient.getGankDataService().BecomeFans(GlobalUserDataStore.getInstance().getToken(),
                JsonUtil.Str2JsonObject("introduceCode", codeId));
        return responseData;
    }
}
