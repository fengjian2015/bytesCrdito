package com.software.feng.bytescrdito.observer;

import android.content.Intent;

public interface ObserverSubject {

    void registerObserver(ItemObserver observer);

    void sendNotify(String s);

    void unRegisterObserver(ItemObserver observer);
}
