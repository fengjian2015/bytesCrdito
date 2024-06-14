package pers.spj.custom.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.NonNull;

public class FileUtils {

   public static String getExternalStoragePath(String filePath){
      String outputFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"+filePath;
      return outputFilePath;
   }
   public static String getInternalStorageFilePath(@NonNull Context context,String outputFilePath){
      File internalStorageDir = context.getFilesDir();
      String filePath = internalStorageDir.getAbsolutePath();
      return filePath+ (outputFilePath.startsWith("/")?outputFilePath:"/"+outputFilePath);
   }
   public static void copyRawToExternalStorage(@NonNull Context context, int resourceId, String outputFilePath) {
      InputStream inputStream = null;
      OutputStream outputStream = null;
      try {
         inputStream = context.getResources().openRawResource(resourceId);
         File outputFile = new File(outputFilePath);
         outputStream = new FileOutputStream(outputFile);
         byte[] buffer = new byte[1024];
         int length;
         while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
         }
         outputStream.flush();
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            if (inputStream != null) {
               inputStream.close();
            }
            if (outputStream != null) {
               outputStream.close();
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

}
