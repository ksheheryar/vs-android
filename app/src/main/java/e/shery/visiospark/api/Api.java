package e.shery.visiospark.api;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Api {


    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> Register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("c_password") String password1,
            @Field("department_id") String id,
            @Field("contact") String contact,
            @Field("contact_person") String personName
    );

//    @FormUrlEncoded
    @POST("event")
    Call<ResponseBody> Event(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register/submit")
    Call<ResponseBody> Event_Registeration(
            @Field("eventId") String eventId,
            @Field("_token") String token,
            @Field("user_id") String userId,
            @Field("particpant[0][0]") String particpant,
            @Field("particpant[0][1]") String particpant1,
            @Field("particpant[0][2]") String particpant2,
            @Field("particpant[0][3]") String particpant3,
            @Field("particpant[0][4]") String particpant4,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register/submit")
    Call<ResponseBody> Event_Registeration1(
            @Field("eventId") String eventId,
            @Field("_token") String token,
            @Field("user_id") String userId,
            @Field("particpant[0][0]") String particpant,
            @Field("particpant[0][1]") String particpant1,
            @Field("particpant[0][2]") String particpant2,
            @Field("particpant[0][3]") String particpant3,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register/submit")
    Call<ResponseBody> Event_Registeration2(
            @Field("eventId") String eventId,
            @Field("_token") String token,
            @Field("user_id") String userId,
            @Field("particpant[0][0]") String particpant,
            @Field("particpant[0][1]") String particpant1,
            @Field("particpant[0][2]") String particpant2,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register/submit")
    Call<ResponseBody> Event_Registeration3(
            @Field("eventId") String eventId,
            @Field("_token") String token,
            @Field("user_id") String userId,
            @Field("particpant[0][0]") String particpant,
            @Field("particpant[0][1]") String particpant1,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register/submit")
    Call<ResponseBody> Event_Registeration4(
            @Field("eventId") String eventId,
            @Field("_token") String token,
            @Field("user_id") String userId,
            @Field("particpant[0][0]") String particpant,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("event_register")
    Call<ResponseBody> Registered_event(
            @Field("user_id") String userId,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @GET("getdata")
    Call<ResponseBody> getData();

    @POST("superadministrator/registeredUsers")
    Call<ResponseBody> Registered_participant(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @POST("superadministrator/verifiedUsers")
    Call<ResponseBody> Verified_participant(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @POST("superadministrator/status")
    Call<ResponseBody> admin_status(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @POST("superadministrator/finance")
    Call<ResponseBody> admin_finance(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("superadministrator/setStatus")
    Call<ResponseBody> admin_setStatus(
            @Field("type") String type,
            @Field("value") String value,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("superadministrator/getTeamsByEmail")
    Call<ResponseBody> admin_emailDetail(
            @Field("email") String email,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("superadministrator/financeDetail")
    Call<ResponseBody> admin_UserFinanceDetail(
            @Field("uniName") String uniName,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @POST("superadministrator/concludedEvents")
    Call<ResponseBody> admin_concludedEvenets(
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("foodToken")
    Call<ResponseBody> food_token(
            @Field("qr_code") String qrCode,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("passwordReset")
    Call<ResponseBody> pass_reset(
            @Field("user_id") String userId,
            @Field("currentPassword") String cPassword,
            @Field("newPassword") String newPassword,
            @Field("confirmPassword") String confPassword,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

    @FormUrlEncoded
    @POST("eventHead")
    Call<ResponseBody> eventHead_data(
            @Field("eventHeadId") String userId,
            @Header("Active") String Active,
            @Header("Authorization") String Auth
    );

}
