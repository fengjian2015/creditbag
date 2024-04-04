package com.fly.credit.jiana.network;


import com.fly.credit.jiana.MyApplication;
import com.fly.credit.jiana.R;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public abstract class NetCallback < T extends Object> implements Observer<T> {
    public abstract void businessFail(NetErrorModel netErrorModel);
    public abstract void businessSuccess(T data);

    @Override
    public void onNext(T t) {
        if (t == null){
            businessFail(new NetErrorModel(-20, MyApplication.getApplication().getString(R.string.network_error)));
        }else {
            businessSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        businessFail(new NetErrorModel(-20, e.toString()));
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
