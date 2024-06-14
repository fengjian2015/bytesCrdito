package pers.spj.custom.common.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author bluefox
 * @date 2023/7/20.
 */

public class CusLog {
    private static boolean isDebug= false;
    public static void setDebug(boolean debug){
        CusLog.isDebug=debug;
    }
    private static final String TAG = CusLog.class.getName();
    public static void d(String tagName, String msg){
        if (isDebug){
            if(!TextUtils.isEmpty(msg)) {
                Log.d(TAG + "-" + tagName, msg);
            }
        }
    }

    public static void i(String msg) {
        if (isDebug) {
            Log.i(TAG, msg);
        }
    }
    public static void i(String tagName, String msg) {
        if (isDebug) {
            Log.d(TAG + "-" + tagName, msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public static void w(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String tagName, String msg) {
        if (isDebug) {
            Log.e(TAG + "--" + tagName, msg);
        }
    }

    public static void e(Throwable e, String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }
}
