package com.fly.credit.jiana.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import com.fly.credit.jiana.R;

public class LoadingUtil {
    private static AlertDialog alertDialog;

    public static void show() {
        Activity activity = ActivityManager.INSTANCE.getCurrentActivity();
        if (activity != null && !activity.isFinishing()) {
            initLoading(activity);
            if (alertDialog != null && !alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    public static synchronized void dismiss() {
        try {
            if ((alertDialog != null) && alertDialog.isShowing()) {
                Context context = ((ContextWrapper) alertDialog.getContext()).getBaseContext();
                if (context instanceof Activity) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed())
                            alertDialog.dismiss();
                    } else {
                        alertDialog.dismiss();
                    }
                } else
                    alertDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
        } catch (Throwable e) {
        } finally {
            alertDialog = null;
        }
    }

    private static void initLoading(Context context) {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.LoadingDialogStyle);
            builder.setView(R.layout.layout_loading);
            builder.setCancelable(true);
            alertDialog = builder.create();
        }
    }
}
