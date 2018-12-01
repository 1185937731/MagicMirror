package com.example.magicmirror.internet;

import com.example.magicmirror.bean.FirmWare;
import com.example.magicmirror.bean.FirmWareInfo;
import com.example.magicmirror.bean.Login;
import com.example.magicmirror.bean.PhotoMessage;
import com.example.magicmirror.bean.PhotoResult;
import com.example.magicmirror.bean.user;

import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface GitHubService{
    @FormUrlEncoded
    @POST("AndroidAddNewUser/")//注册
    Call<String> AndroidAddNewUser(
            @Field("mirror_id") String mirror_id,
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("AndroidLogin/")//登录
    Call<Login> AndroidLogin(
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("AndroidPhoto/")//图片信息
    Call<PhotoMessage> AndroidPhoto(
            @Field("username") String username,
            @Field("mirror_id") String mirror_id);

    @FormUrlEncoded
    @POST("AndroidGetPhoto/")//图片信息
    Call<ResponseBody> AndroidGetPhoto(
            @Field("address") String address);


    @FormUrlEncoded
    @Streaming
    @POST("AndroidGetFirm/") //获取硬件版本信息
    Call<ResponseBody>AndroidGetFirm(
            @Field("address")String address
    );

//    @Multipart
//    @POST("FirmUpload") //提交硬件
//    Call<ResponseBody> upload(@Part("time") RequestBody time,
//                         @Part MultipartBody.Part file
//                        );


    @Multipart
    @POST("PiUpload/")//注册
    Call<String> PiUpload(
            @Part("username") RequestBody username,
            @Part("mirror_id") RequestBody mirror_id,
            @Part MultipartBody.Part  photo,
//            @Part("photo") RequestBody  photo,
            @Part("time") RequestBody time);

    @FormUrlEncoded
    @POST("AndroidShopClientPhotoGet/")//获取某一客户在家拍照的结果
    Call<PhotoResult> AndroidShopClientPhotoGet(
            @Field("username") String username);


    @FormUrlEncoded
    @POST("AndroidFirmWareInfoGet/")//获取固件
    Call<FirmWareInfo> AndroidFirmWareInfoGet(
            @Field("1") String askFirm);

    @Streaming
    @GET("AndroidGetFirm/") //获取硬件版本信息
    Call<ResponseBody> AndroidgetFirm(
            @Query("address") String address);

}
