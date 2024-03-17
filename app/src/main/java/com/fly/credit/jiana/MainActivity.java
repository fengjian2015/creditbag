package com.fly.credit.jiana;

import static com.fly.credit.jiana.util.Cons.KEY_PUBLIC_IP;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
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

import com.fly.credit.jiana.bean.PublicDataResponse;
import com.fly.credit.jiana.manage.UserInfoManage;
import com.fly.credit.jiana.network.EncryptCbcUtil;
import com.fly.credit.jiana.network.NetCallback;
import com.fly.credit.jiana.network.NetClient;
import com.fly.credit.jiana.network.NetErrorModel;
import com.fly.credit.jiana.network.NetUtil;
import com.fly.credit.jiana.network.NewServiceManage;
import com.fly.credit.jiana.util.LogUtil;
import com.fly.credit.jiana.util.MMKVCacheUtil;
import com.fly.credit.jiana.util.SignalStrengthUtils;
import com.fly.credit.jiana.util.SoftKeyboardUtils;
import com.fly.credit.jiana.web.IWebChromeClient;
import com.fly.credit.jiana.web.IWebSetting;
import com.fly.credit.jiana.web.IWebViewClient;
import com.fly.credit.jiana.web.WebJs;
import com.gyf.immersionbar.ImmersionBar;

import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {
    private boolean isHome = false;
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
        webUrl = getIntent().getStringExtra(WEB_URL);
        isHome = getIntent().getBooleanExtra(IS_HOME,false);
        initView();
        initBar();
        initData();
        initBattery();
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
        NewServiceManage.INSTANCE.checkUpdate(new Function1<Integer, Integer>() {
            @Override
            public Integer invoke(Integer integer) {
                UpdateDialog dialog =new UpdateDialog(UserInfoManage.INSTANCE.getUserInfo());
                dialog.show(MainActivity.this.getSupportFragmentManager(), "update'");
                return 1;
            }
        });
    }



    private void initBattery() {
        if (isHome) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
            batteryReceiver = new BatteryReceiver();
            registerReceiver(batteryReceiver, intentFilter);
        }
    }


    @Override
    protected void onDestroy() {
        if (isHome){
            unregisterReceiver(batteryReceiver);
        }
        super.onDestroy();
    }

    private void initBar() {
        if (isHome){
            ImmersionBar
                    .with(this)
                    .statusBarDarkFont(true)
                    .keyboardEnable(true)
                    .init();
        }else {
            ImmersionBar
                    .with(this)
                    .fitsSystemWindows(true)
                    .statusBarColor(R.color.common_bg)
                    .statusBarDarkFont(true)
                    .keyboardEnable(true)
                    .init();
        }
    }

    private void initView() {
        topFrame = findViewById(R.id.topFrame);
        topBack = findViewById(R.id.topBack);
        topTitle = findViewById(R.id.topTitle);
        webLoading = findViewById(R.id.webLoading);
        webView = findViewById(R.id.webView);

    }

    @SuppressLint("JavascriptInterface")
    private void initData() {
        webJsBridge = new WebJs(this, webView);
        webView.addJavascriptInterface(webJsBridge,"CreditBagClient");
        IWebSetting.webViewInit(webView);
        webView.setWebViewClient(new IWebViewClient(webLoading, topTitle));
        webView.setWebChromeClient(new IWebChromeClient(webLoading));
        if (isHome){
//            UpdateUtil.checkUpdate(this);
            topFrame.setVisibility(View.GONE);
        }else {
            topFrame.setVisibility(View.VISIBLE);
        }

        topBack.setOnClickListener(view -> {
            checkFinish();
        });
//        测试
        webUrl = "file:///android_asset/jsbride-demo.html";
        if (webUrl!=null && !webUrl.startsWith("http") && !webUrl.startsWith("file")){
            webUrl = "https://"+webUrl;
        }
        webView.loadUrl(webUrl);
    }

    private void checkFinish(){
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if (isHome){
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