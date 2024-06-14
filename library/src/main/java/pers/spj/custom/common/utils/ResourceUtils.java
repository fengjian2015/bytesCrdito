package pers.spj.custom.common.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ResourceUtils {
   public static String readRawResource(@NonNull Context context, int resourceId) {
      StringBuilder stringBuilder = new StringBuilder();
      try {
         Resources res = context.getResources();
         // 打开资源文件输入流
         InputStream inputStream = res.openRawResource(resourceId);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         String line;
         // 逐行读取文本内容并添加到StringBuilder中
         while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
         }
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      // 返回最终的字符串
      return stringBuilder.toString();
   }
   public static String readRawResource(@NonNull Context context, String fileName) {
      StringBuilder stringBuilder = new StringBuilder();
      try {
         AssetManager assetManager = context.getAssets();
         // 打开资源文件输入流
         InputStream inputStream = assetManager.open(fileName);
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         String line;
         // 逐行读取文本内容并添加到StringBuilder中
         while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
         }
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      // 返回最终的字符串
      return stringBuilder.toString();
   }
   public static String readRawResourceFd(@NonNull Context context, int resourceId) {
      StringBuilder stringBuilder = new StringBuilder();
      try {
         Resources res = context.getResources();
         // 获取资源文件描述符
         AssetFileDescriptor descriptor = res.openRawResourceFd(resourceId);
         FileInputStream inputStream = descriptor.createInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
         String line;
         // 逐行读取文本内容并添加到StringBuilder中
         while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
         }
         reader.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      // 返回最终的字符串
      return stringBuilder.toString();
   }

   /**
    * getXmlResource(context, R.xml.your_xml_resource)
    * @param context
    * @param resourceId
    * @return
    */
   public static XmlResourceParser getXmlResource(@NonNull Context context, int resourceId) {
      Resources res = context.getResources();
      return res.getXml(resourceId);
   }
   /**
    * getXmlResource(context, "your_xml_resource.xml")
    * @param context
    * @param fileName
    * @return
    */
   public static XmlResourceParser getXmlResource(@NonNull Context context, String fileName) {
      XmlResourceParser xmlResourceParser = null;
      try {
         AssetManager assetManager = context.getAssets();
         // 打开XML资源文件解析器
         xmlResourceParser = assetManager.openXmlResourceParser(fileName);
      } catch (IOException e) {
         e.printStackTrace();
      }
      return xmlResourceParser;
   }

   //图片资源位于res/drawable目录下，并命名为image.png
   public static Drawable getDrawable(@NonNull Context context,int drawableId){
      Drawable drawable = context.getResources().getDrawable(drawableId,null);
      return drawable;
   }
   public static int getImageResourceIdFromName(@NonNull Context context,@NonNull String resName){
      int resId = context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
      return resId;
   }
   public static int getLayoutIdFromName(@NonNull Context context,@NonNull String layoutName){
      String packageName = context.getPackageName();
      int resourceId = context.getResources().getIdentifier(layoutName, "layout", packageName);
      if (resourceId != 0) {
         // 找到了对应的资源ID
        return resourceId;
      }
      return 0;
   }
   public static int getRawResourceIdFromName(@NonNull Context context,@NonNull String fileName){
      int resId = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
      return resId != 0?resId:0;
   }
   public static  PackageInfo getPackageInfo(@NonNull Context context){
      // 获取当前应用的包名
      String packageName = context.getPackageName();
      // 获取当前应用的资源对象
      Resources resources = context.getResources();
      // 获取所有已安装的包信息
      try {
         PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
         return packageInfo;
      } catch (PackageManager.NameNotFoundException e) {
         e.printStackTrace();
      }
      return null;
   }
   public static Bitmap getBitmapFromResourceId(@NonNull Context context, int resourceId) {
      return BitmapFactory.decodeResource(context.getResources(), resourceId);
   }

   @Deprecated
   public static Bitmap getBitmapFromResourceIdOld(Context context, int resourceId) {
      Drawable drawable = context.getResources().getDrawable(resourceId);
      if (drawable instanceof BitmapDrawable) {
         return ((BitmapDrawable) drawable).getBitmap();
      }
      return null;
   }
   public interface OnGetResourceListener<T>{
      T getResource(int resourceId);
      String getNameTag();
   }
   public static <T> List<T> getResourceList(@NonNull Context context,OnGetResourceListener<T> listener){
      ArrayList<T> arrayList = new ArrayList<>();
      if(listener==null){return arrayList;}
      PackageInfo packageInfo= getPackageInfo(context);
      Field[] fields = packageInfo.applicationInfo.getClass().getDeclaredFields();
      try {
         for (Field field : fields) {
            if (field.getType() == int.class && field.getName().contains(listener.getNameTag())) {
               field.setAccessible(true);
               int resourceId = field.getInt(packageInfo.applicationInfo);
               T t = listener.getResource(resourceId);
               arrayList.add(t);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return arrayList;
   }
   public static List<String> getAllStrings(@NonNull Context context) {
      Resources resources = context.getResources();
     return getResourceList(context, new OnGetResourceListener<String>() {

         @Override
         public String getResource(int resourceId) {
            return resources.getString(resourceId);
         }

         @Override
         public String getNameTag() {
            return "string";
         }
      });
   }
   public static List<Drawable> getAllDrawable(@NonNull Context context) {
     return getResourceList(context, new OnGetResourceListener<Drawable>() {
         @Override
         public Drawable getResource(int resourceId) {
            return getDrawable(context,resourceId);
         }
         @Override
         public String getNameTag() {
            return "drawable";
         }
      });
   }

}
