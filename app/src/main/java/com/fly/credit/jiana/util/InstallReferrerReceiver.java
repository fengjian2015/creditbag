package com.fly.credit.jiana.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Time：2024/4/15
 * Author：feng
 * Description：
 */
public class InstallReferrerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {
        LogUtil.d("InstallReferrerReceiver");
//        String rawReferrer = data.getStringExtra(REFERRER); /// 你想要的referrer值
//        if (null == rawReferrer) {
//            return;
//        }
    }
}
