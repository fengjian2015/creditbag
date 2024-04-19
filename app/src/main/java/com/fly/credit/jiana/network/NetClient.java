package com.fly.credit.jiana.network;



import com.fly.credit.jiana.BuildConfig;
import com.fly.credit.jiana.util.Cons;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetClient {

    private static NetClient netClient;
    private static NewService newService;

    public static NewService getNewService() {
        if (newService == null){
            newService = getInstance().initOkHttpClient();
        }
        return newService;
    }

    public static NetClient getInstance(){
        if (netClient == null){
            netClient = new NetClient();
        }
        return netClient;
    }

    public NewService initOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60,TimeUnit.SECONDS);
        builder.writeTimeout(60,TimeUnit.SECONDS);
        builder.sslSocketFactory(getSSLSocketFactory());
        builder.retryOnConnectionFailure(false);
        builder.addInterceptor(new ValueInterceptor());
        builder.addInterceptor(new EncryptIntercept());
//        if (isAuth) {
//            builder.addInterceptor(new GzipRequestInterceptor());
//        }
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor =new HttpLoggingInterceptor("NetClientLog");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }

        Retrofit build = new Retrofit.Builder()
                .baseUrl(Cons.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();
        return build.create(NewService.class);
    }

    public OkHttpClient initUploadOkHttpClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.retryOnConnectionFailure(false);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60,TimeUnit.SECONDS);
        builder.writeTimeout(60,TimeUnit.SECONDS);
        builder.sslSocketFactory(getSSLSocketFactory());
        builder.addInterceptor(new ValueInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor =new HttpLoggingInterceptor("NetClientLog");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder.build();
    }

    //获取这个SSLSocketFactory
    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManager(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //获取TrustManager
    private static TrustManager[] getTrustManager() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // return null; 或者
                        return new X509Certificate[]{}; // 空实现
                    }
                }
        };
        return trustAllCerts;
    }

}
