package com.software.feng.utillibrary.http;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class NetCallback< T extends Object> implements Observer<T> {
    public abstract void businessFail(NetErrorModel netErrorModel);
    public abstract void businessSuccess(T data);

    @Override
    public void onNext(T t) {
        if (t == null){
            businessFail(new NetErrorModel(-20, "NetCallback : network error"));
        }else {
            businessSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        businessFail(new NetErrorModel(-20, "NetCallback : "+e.toString()));
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onSubscribe(Disposable d) {

    }
}
