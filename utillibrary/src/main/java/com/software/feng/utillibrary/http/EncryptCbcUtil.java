package com.software.feng.utillibrary.http;

import android.util.Base64;

import com.software.feng.utillibrary.util.Cons;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptCbcUtil {


    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    // AES 加密
    private static final String AES = "AES";


    // 加密

    /**
     * 28      * AES 加密
     * 29      *
     * 30      * @param strKey            加密密钥
     * 31      * @param strClearText      待加密内容
     * 32      * @param mstrIvParameter   密钥偏移量
     * 33      * @return 返回Base64转码后的加密数据
     * 34
     */
    public static String encrypt(String strClearText) {

        try {
            byte[] raw = Cons.ENCRYPT_KEY_TEST.getBytes();
            // 创建AES密钥
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            // 创建偏移量
            IvParameterSpec iv = new IvParameterSpec(Cons.VECTOR_TEST.getBytes());
            // 初始化加密器
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            // 执行加密操作
            byte[] cipherText = cipher.doFinal(strClearText.getBytes());
            //Log.d(TAG, "encrypt result(not BASE64): " + cipherText.toString());
            String strBase64Content = Base64.encodeToString(cipherText, Base64.DEFAULT); // encode it by BASE64 again
            //Log.d(TAG, "encrypt result(BASE64): " + strBase64Content);
            strBase64Content = strBase64Content.replaceAll(System.getProperty("line.separator"), "");

            return strBase64Content;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 解密

    /**
     * 64      * AES 解密
     * 65      *
     * 66      * @param strKey            解密密钥
     * 67      * @param strCipherText      待解密内容
     * 68      * @param mstrIvParameter   偏移量
     * 69      * @return 返回Base64转码后的加密数据
     * 70
     */
    public static String decrypt(String strCipherText) throws Exception {

        try {
            byte[] raw = Cons.ENCRYPT_KEY_TEST.getBytes("ASCII");
            // 创建AES秘钥
            SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
            // 创建密码器
            Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
            // 创建偏移量
            IvParameterSpec iv = new IvParameterSpec(Cons.VECTOR_TEST.getBytes());
            // 初始化解密器
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            // 执行解密操作
            byte[] cipherText = Base64.decode(strCipherText, Base64.DEFAULT); // decode by BASE64 first
            //Log.d(TAG, "BASE64 decode result(): " + cipherText.toString());
            byte[] clearText = cipher.doFinal(cipherText);
            String strClearText = new String(clearText);
            //Log.d(TAG, "decrypt result: " + strClearText);

            return strClearText;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
