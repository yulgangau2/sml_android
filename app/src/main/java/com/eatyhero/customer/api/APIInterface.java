package com.eatyhero.customer.api;

import com.eatyhero.customer.model.SettingsList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIInterface {

    @GET("sitesetting")
    Call<SettingsList> callSiteSettings();

    //  X-Consumer-Custom-ID

    /*@GET("country/list")
    Call<CountryCodeResponseObject> getCountryCode();

    @POST("forget-password")
    Call<ForgotPasswordResponseObject> setForgotPasswordRequest(@Body ForgotPasswordRequestObject forgotPasswordRequestObject);

    @POST("new-password")
    Call<NewForgotPasswordResponseObject> setNewForgotPasswordRequest(@Body NewForgotPasswordRequestObject newForgotPasswordRequestObject);

    @POST("change-password")
    Call<ChangePasswordResponseObject> changePassword(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body ChangePasswordRequestObject changePasswordRequestObject);

    @POST("customer/login")
    Call<LoginResponseObject> login(@Body LoginRequestObject loginRequestObject);

    @POST("customer/signup")
    Call<SignUpResponseObject> signUp(@Body SignUpRequestObject signUpRequestObject);

    @GET("check-phone-exist/{phone}/{country}")
    Call<PhoneNumberExistsResponseObject> checkPhoneNumberExists(@Path("phone") String mobileNumber, @Path("country") String country);

    @POST("customer/verify-otp")
    Call<VerifyOTPResponseObject> verifyOTP(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body VerifyOTPRequestObject verifyOTPRequestObject);

    @POST("customer/send-otp")
    Call<SendOTPResponseObject> resendOTP(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("customer/verify-pass-code")
    Call<VerifyLauncherPassCodeResponseObject> verifyPassCode(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body VerifyLauncherPassCodeRequestObject verifyLauncherPassCodeRequestObject);

    @POST("customer/add-pass-code")
    Call<AddPassCodeResponseObject> addPassCode(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body AddPassCodeRequestObject addPassCodeRequestObject);

    @POST("customer/profile")
    Call<UpdateProfileResponseObject> updateProfile(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body UpdateProfileRequestObject updateProfileRequestObject);

    @GET("get-refer-list")
    Call<GetReferListResponseObject> getReferList(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @GET("customer/user")
    Call<GetProfileResponseObject> getProfile(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @DELETE("logout")
    Call<LogoutResponseObject> logout(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @GET("individualspin/customer/list")
    Call<IndividualCustomerListResponseObject> individualCustomerList(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("customer/kyc-update")
    Call<UpdateKYCResponseObject> updateKYC(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body UpdateKYCRequestObject updateKYCRequestObject);

    @POST("spin/play")
    Call<SpinPlayResponseObject> spinPlay(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body SpinPlayRequestObject spinPlayRequestObject);

    @POST("spin/winning")
    Call<GameResultResponseObject> gameResult(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body GameResultRequestObject gameResultRequestObject);

    @GET("spin/transaction")
    Call<GameHistoryResponseObject> getGameHistory(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @GET("topuprequest/view/{id}")
    Call<ViewTopUpResponseObject> topUpHistoryView(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Path("id") String countryID);

    @GET("bank-info/{countryId}")
    Call<GetTopUpBankDetailsResponseObject> getTopUpBankDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Path("countryId") String countryID);

    @POST("bank-image")
    Call<SubmitTopUpResponseObject> submitTopUp(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body SubmitTopUpRequestObject submitTopUpRequestObject);

    @GET("topuprequest")
    Call<TopUpDetailsResponseObject> getTopUpDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("withdrawal-list")
    Call<WithDrawDetailsResponseObject> getWithDrawDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("profile-update-otp")
    Call<UpdateNumberResponseObject> updateNumber(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body UpdateNumberRequestObject updateNumberRequestObject);

    @GET("groupspin/customer/list")
    Call<GetInviteDetailsResponseObject> getInviteDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("add-beneficiary")
    Call<AddInviteContactResponseObject> addInviteContact(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body AddInviteContactRequestObject addInviteContactRequestObject);

    @GET("beneficiary-list")
    Call<InviteContactListResponseObject> inviteContactList(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("groupspin/private/invite")
    Call<AddInviteSpinResponseObject> addInviteSpin(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body AddInviteSpinRequestObject addInviteSpinRequestObject);

    @GET("notifications-status")
    Call<NotificationStatusResponseObject> notificationStatus(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @GET("notifications-list")
    Call<NotificationListResponseObject> notificationList(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("game-details")
    Call<GameInviteListResponseObject> getGameInviteDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body GameInviteListRequestObject gameInviteListRequestObject);

    @GET("join-invite-game")
    Call<JoinInviteGameResponseObject> getJoinInviteGame(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @GET("ongoing-game")
    Call<OngoingGameResponseObject> getOngoingGame(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("groupspin/private/accept")
    Call<InviteStatusResponseObject> getInviteStatus(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body InviteStatusRequestObject inviteStatusRequestObject);

    @POST("groupspin/public/accept")
    Call<InviteStatusResponseObject> getGroupInviteStatus(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body InviteStatusRequestObject inviteStatusRequestObject);

    @POST("groupspin/private/winner")
    Call<PrivateWinnerDetailsResponseObject> getPrivateWinnerDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body PrivateWinnerDetailsRequestObject privateWinnerDetailsRequestObject);

    @GET("bank-list")
    Call<BankListResponseObject> getBankList(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken);

    @POST("add-bank")
    Call<AddBankAccountResponseObject> addBankAccountDetails(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body AddBankAccountRequestObject addBankAccountRequestObject);

    @POST("withdraw")
    Call<WithDrawAmountResponseObject> withDrawAmount(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body WithDrawAmountRequestObject withDrawAmountRequestObject);

    @POST("groupspin/public/invite")
    Call<PublicInviteResponseObject> publicInvite(@Header("X-Consumer-Custom-ID") String customerID, @Header("platform") String platform, @Header("auth_token") String authenticationToken, @Body PublicInviteRequestObject publicInviteRequestObject);*/
}