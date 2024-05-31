package com.software.feng.bytescrdito.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Time：2024/5/31
 * Author：feng
 * Description：
 */
public class RiskUtilJava {
    /**
     *   获取android总运行内存大小
     */
    public static long getTotalMemory() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            // 获得系统总内存，单位是KB
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            //int值乘以1024转换为long类型
            initial_memory = new Long((long) i * 1024);
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return initial_memory;// Byte转换为KB或者MB，内存大小规格化
    }
}
