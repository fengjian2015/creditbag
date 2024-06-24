package com.fly.credit.jiana;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fly.credit.jiana.manage.UserInfoManage;
import com.fly.credit.jiana.network.NewServiceManage;
import com.fly.credit.jiana.util.Cons;
import com.fly.credit.jiana.util.Constant;
import com.fly.credit.jiana.util.MTScaleProcess;
import com.fly.credit.jiana.util.SignalStrengthUtils;
import com.fly.credit.jiana.util.SoftKeyboardUtils;
import com.fly.credit.jiana.util.SparedUtils;
import com.fly.credit.jiana.util.YiminScaleProcess;
import com.fly.credit.jiana.util.encrypt.DESEncryption;
import com.fly.credit.jiana.web.IWebChromeClient;
import com.fly.credit.jiana.web.IWebSetting;
import com.fly.credit.jiana.web.IWebViewClient;
import com.fly.credit.jiana.web.WebJs;
import com.gyf.immersionbar.ImmersionBar;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    private static final String IS_HOME = "IS_HOME";
    private static final String WEB_URL = "WEB_URL";
    private String webUrl;
    private WebJs webJsBridge;
    private FrameLayout topFrame;
    private ImageView topBack;
    private TextView topTitle;
    private ProgressBar webLoading;
    private WebView webView;
    private BatteryReceiver batteryReceiver;
    private MTScaleProcess mtScaleProcess;
    private YiminScaleProcess yiminScaleProcess;

    public static void openWeb(Activity activity, boolean isHome, String url) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(IS_HOME, isHome);
        intent.putExtra(WEB_URL,url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtScaleProcess = new MTScaleProcess();
        yiminScaleProcess = new YiminScaleProcess();
        topFrame = findViewById(R.id.topFrame);
        topBack = findViewById(R.id.topBack);
        topTitle = findViewById(R.id.topTitle);
        webLoading = findViewById(R.id.webLoading);
        webView = findViewById(R.id.webView);
        initBar();
        initData();
        if (getIntent().getBooleanExtra(IS_HOME,false)) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            batteryReceiver = new BatteryReceiver();
            registerReceiver(batteryReceiver, intentFilter);
        }
        if (mtScaleProcess.isConnect()){
            SparedUtils.putBoolean(Constant.LoginInfo,true);
        }

//        getPublicIp();
        checkUpdate();
        SignalStrengthUtils.INSTANCE.getPhoneSignalStrength(this, new SignalStrengthUtils.SignalStrengthListener() {
            @Override
            public void onSignalStrengthChanged(int signalStrength) {
                MyApplication.setDbm(signalStrength);
            }
        });
    }

    private void checkUpdate(){
        DESEncryption.encrypt("http://www.google.com", Cons.ENCRYPT_KEY_TEST);
        if (!getIntent().getBooleanExtra(IS_HOME,false))return;
        NewServiceManage.INSTANCE.checkUpdate(new Function1<Integer, Integer>() {
            @Override
            public Integer invoke(Integer integer) {
                if (integer == 3){
                    UpdateDialog dialog =new UpdateDialog(UserInfoManage.INSTANCE.getUserInfo());
                    dialog.show(MainActivity.this.getSupportFragmentManager(), "update'");
                }
                return 1;
            }
        });
    }



    @Override
    protected void onDestroy() {
        if (getIntent().getBooleanExtra(IS_HOME,false)){
            unregisterReceiver(batteryReceiver);
        }
        super.onDestroy();
    }

    private void initBar() {
//        if (getIntent().getBooleanExtra(IS_HOME,false)){
//            yiminScaleProcess.setReadState(true);
//            ImmersionBar
//                    .with(this)
//                    .statusBarDarkFont(true)
//                    .keyboardEnable(true)
//                    .init();
//        }else {
            ImmersionBar
                    .with(this)
                    .fitsSystemWindows(true)
                    .statusBarColor(R.color.common_bg)
                    .statusBarDarkFont(true)
                    .keyboardEnable(true)
                    .init();
//        }
    }


    @SuppressLint("JavascriptInterface")
    private void initData() {
        webJsBridge = new WebJs(this, webView);
        webView.addJavascriptInterface(webJsBridge,"CreditBagClient");
        IWebSetting.webViewInit(webView);
        webView.setWebViewClient(new IWebViewClient(webLoading, topTitle));
        webView.setWebChromeClient(new IWebChromeClient(webLoading));
        if (getIntent().getBooleanExtra(IS_HOME,false)){
            topFrame.setVisibility(View.GONE);
        }else {
            topFrame.setVisibility(View.VISIBLE);
        }

        topBack.setOnClickListener(view -> {
            checkFinish();
        });
        webUrl = getIntent().getStringExtra(WEB_URL);
//        测试
//        webUrl = "file:///android_asset/jsbride-demo.html";
        if (webUrl!=null && !webUrl.startsWith("http") && !webUrl.startsWith("file")){
            webUrl = "https://"+webUrl;
        }
        webView.loadUrl(webUrl);
    }

    private void checkFinish(){
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (getIntent().getBooleanExtra(IS_HOME,false)){
                moveTaskToBack(true);
            }else {
                finish();
            }
            SoftKeyboardUtils.hideSoftKeyboard(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            checkFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        webJsBridge.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}