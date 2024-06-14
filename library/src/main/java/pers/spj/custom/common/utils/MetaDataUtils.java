package pers.spj.custom.common.utils;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

import androidx.annotation.NonNull;

/**
 * @author bluefox
 * @date 2023/7/7.
 */

public class MetaDataUtils {
    private MetaDataUtils() {
        throw new RuntimeException("Not Instantiable");
    }

    public static String getMetaDataInApp(@NonNull Context context, @NonNull final String key) {
        String value = "";
        PackageManager pm =context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            ApplicationInfo ai = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            value = ai.metaData.getString(key,"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getMetaDataInActivity(@NonNull  Context context, @NonNull final Class<? extends Activity> clz, @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ActivityInfo ai = pm.getActivityInfo(componentName, PackageManager.GET_META_DATA);
            value = ai.metaData.getString(key,"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getMetaDataInService(@NonNull  Context context, @NonNull final Class<? extends Service> clz, @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ServiceInfo info = pm.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            value = info.metaData.getString(key,"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getMetaDataInReceiver(@NonNull  Context context, @NonNull final Class<? extends BroadcastReceiver> clz, @NonNull final String key) {
        String value = "";
        PackageManager pm = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, clz);
        try {
            ActivityInfo info = pm.getReceiverInfo(componentName, PackageManager.GET_META_DATA);
            value = info.metaData.getString(key,"");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }
}
