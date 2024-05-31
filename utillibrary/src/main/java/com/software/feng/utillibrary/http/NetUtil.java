package com.software.feng.utillibrary.http;



import androidx.annotation.NonNull;

import java.util.concurrent.Executors;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NetUtil {
    private static Scheduler scheduler;

    @NonNull
    public static  <T> ObservableTransformer<T, T> applySchedulers() {
        if(scheduler == null){
            scheduler =  Schedulers.from(Executors.newFixedThreadPool(3));
        }
        return (ObservableTransformer) upstream -> upstream.subscribeOn(scheduler)
                .unsubscribeOn(scheduler)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
