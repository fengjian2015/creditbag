package com.fly.credit.jiana.network;


import com.fly.credit.jiana.bean.BaseResponseBean;
import com.fly.credit.jiana.bean.ImageInfo;
import com.fly.credit.jiana.bean.LoginlInfoListBase;
import com.fly.credit.jiana.bean.ProtocolUrlInfoList;
import com.fly.credit.jiana.bean.PublicDataResponse;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface NewService {

    //system/getPublicIpv2  获取IP地址
    @POST("/app/VuC8dp8BUUEOW060Q2xU/83QHC2iJaoxWEAIYmUviVY=")
    Observable<PublicDataResponse> getPublicIp();

    //system/logout   登出
    @POST("/app/2hDNLIkuSCXGUtnsm7VwaA==")
    Observable<PublicDataResponse> logout(@Body Map<String, String> map);

    //system/getProtocolUrlv2  获取协议
    @POST("/app/tKmLDCZwJQfBmAYNe9+qJOC4h9TvbBA448LyePIgOuA=")
    Observable<ProtocolUrlInfoList> getProtocolUrlv2(@Body Map<String, String> map);

    //account/sendVerifyCode   发送验证码接口
    @POST("/app/XoHAjwZ2obcZiHaRPY8lJ+Q0CF6G4eRgKkuIUOp/nu4=")
    Observable<BaseResponseBean> sendVerifyCode(@Body Map<String, String> map);

    ///account/loginByPhoneVerifyCode   登录接口
    @POST("/app/SgGwPxKCorlX/q0Hctc+ESn/aTw99Qy8aicvKNkov+w=")
    Observable<LoginlInfoListBase> loginByPhoneVerifyCode(@Body Map<String, String> map);

    ///account/staticLoginv2  登录接口
    @POST("/app/QJ0b9mkySRzjZxNlDnuUWQigqFjnG3ISz4ENEZlJF+o=")
    Observable<LoginlInfoListBase> staticLoginv2(@Body Map<String, String> map);

    ///auth/addUserAction  埋点
    @POST("/app/22BZNNkzaP76ByNtjZ2BxWg0inbbBMT72r8RcDcRX/8=")
    Observable<ProtocolUrlInfoList> addUserAction(@Body Map<String, String> map);

    //auth/uploadCreditModeLoanWardAuth  封控数据
    @POST("/app/tgMqSVLzc9Mrpd9yOfpiJuHL/r073596XMj3OU+6U2eOUQR5owUH3+GM8m7f/q1C")
    Observable<ProtocolUrlInfoList> uploadCreditModeLoanWardAuth(@Body Map<String, String> map);


    //system/uploadimg  上传图片
    @POST("/app/M8OKnpOJpWn7IH7YhGU11w65EQyyMJfgbgC4Ry4pVxU=")
    Observable<ImageInfo> uploadimg(@Part MultipartBody.Part file , @Part MultipartBody.Part type);
}
