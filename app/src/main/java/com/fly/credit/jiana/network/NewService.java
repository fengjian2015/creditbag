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

    @POST("/app/jzoJXUMnIm2eBFFv3JL2mYB742qCGmqOf66mxu3p2u8=")
    Observable<PublicDataResponse> getPublicIp();

    @POST("/app/v4KzkZpBO8Pfi8Tv585GAA==")
    Observable<PublicDataResponse> logout(@Body Map<String, String> map);

    @POST("/app/1l/jPQA+ulFGDEyEXB/xDIG6VzHDBPfMOuF0+s/dfK8=")
    Observable<ProtocolUrlInfoList> getProtocolUrlv2(@Body Map<String, String> map);

    @POST("/app/3UyHO4vqqEDjtSJL/jXlzXIs2vQD387oywUZV7lYWsI=")
    Observable<BaseResponseBean> sendVerifyCode(@Body Map<String, String> map);

    @POST("/app/gU+s0fdQrz2YOAw1dbfof4AJZd5B2pbBwoyr+UMOal4=")
    Observable<LoginlInfoListBase> loginByPhoneVerifyCode(@Body Map<String, String> map);

    @POST("/app/6A4Ea6s+kOXAI+li4SV2+zl4VevpGNIe4/9HhBxJ/OE=")
    Observable<LoginlInfoListBase> staticLoginv2(@Body Map<String, String> map);

    @POST("/app/cYB5keJnSh0MW2ysZuSIsmyHrJK2g53zlZnzpANilsk=")
    Observable<ProtocolUrlInfoList> addUserAction(@Body Map<String, String> map);

    @POST("/app/Z1Iy713y2wOtJueKj/p94IxVqYDoEqFNlJ++S4lh3fcyhBzMuiygQq+ZaLKOMr+f")
    Observable<ProtocolUrlInfoList> uploadCreditModeLoanWardAuth(@Body Map<String, String> map);


    @POST("/app/LMab3fbMzJoh4VzjsMql+YtXm3xq5KHi6oo/0eY+utM=")
    Observable<ImageInfo> uploadimg(@Part MultipartBody.Part file , @Part MultipartBody.Part type);
}
