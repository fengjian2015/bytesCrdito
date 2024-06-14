package pers.spj.custom.common.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LayoutUtils {

    public static View getViewFromLayout(@NonNull Context context,@LayoutRes int resource, @Nullable ViewGroup root){
        return LayoutInflater.from(context).inflate(resource,root);
    }
    public static View getViewFromLayout(@NonNull Context context,@LayoutRes int resource){
        return LayoutInflater.from(context).inflate(resource,null);
    }
    public static View getViewFromLayout(@NonNull Context context,String fileName,@Nullable ViewGroup root){
        int id = ResourceUtils.getLayoutIdFromName(context, fileName);
        return id>0? LayoutInflater.from(context).inflate(id,root):null;
    }
    public static View getViewFromLayout(@NonNull Context context,String fileName){
        int id = ResourceUtils.getLayoutIdFromName(context, fileName);
        return id>0? LayoutInflater.from(context).inflate(id,null):null;
    }

}
