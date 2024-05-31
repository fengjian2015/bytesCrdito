package com.software.feng.bytescrdito.observer;

import android.content.Intent;

import com.software.feng.utillibrary.util.LogUtil;

import java.util.concurrent.CopyOnWriteArrayList;

public class ObserverManager implements ObserverSubject {

    private static ObserverManager manager;
    private static final CopyOnWriteArrayList<ItemObserver> observerList;

    static {
        observerList = new CopyOnWriteArrayList<>();
        observerList.clear();
    }

    private ObserverManager() {
    }

    public synchronized static ObserverManager getManager() {
        if (null == manager) {
            manager = new ObserverManager();
        }
        return manager;
    }

    @Override
    public void registerObserver(ItemObserver observer) {
        LogUtil.INSTANCE.d("注册当前ItemObserver");
        if (null != observer) {
            if (null != observerList) {
                if (!observerList.contains(observer)) {
                    observerList.add(observer);
                }
            }
        }
    }

    @Override
    public void sendNotify(String s) {
        LogUtil.INSTANCE.d("发送当前ItemObserver");
        if (null != observerList && observerList.size() > 0) {
            for (ItemObserver itemObserver : observerList) {
                itemObserver.receiveNotify(s);
            }
        }
    }

    @Override
    public void unRegisterObserver(ItemObserver observer) {
        LogUtil.INSTANCE.d("释放当前ItemObserver");
        if (null != observer) {
            if (null != observerList && observerList.size() > 0) {
                if (observerList.indexOf(observer) >= 0) {
                    observerList.remove(observer);
                }
            }
        }
    }
}
