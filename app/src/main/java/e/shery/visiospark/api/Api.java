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
            @Field("department_id") String id
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

}
