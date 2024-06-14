package pers.spj.custom.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @author bluefox
 * @date 2023/7/3.
 */

public class CommonUtils {
    public static View getViewFormLayout(Context context, @LayoutRes int lauyoutId, ViewGroup root) {
        if (context == null) {
            return null;
        }
        View view = LayoutInflater.from(context).inflate(lauyoutId, root, false);
        return view;
    }

    public static String[] getStringArray(Context context, @ArrayRes int arrayId) {
        if (context == null) {
            return null;
        }
        String[] stringArray = context.getResources().getStringArray(arrayId);
        return stringArray;
    }

    public static int[] getIntArray(Context context, @ArrayRes int id) {
        if (context == null) {
            return null;
        }
        int[] intArray = context.getResources().getIntArray(id);
        return intArray;
    }

    public static Drawable getDrawable(Context context, @DrawableRes int id) {
        if (context == null) {
            return null;
        }
        Drawable drawable = ContextCompat.getDrawable(context, id);
        return drawable;
    }

    public static int getDimensionPixelSize(Context context, @DimenRes int id) {
        if (context == null) {
            return 0;
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(id);
        return dimensionPixelSize;
    }

    public static int getColor(Context context, @ColorRes int id) {
        if (context == null) {
            return 0;
        }
        int color = ContextCompat.getColor(context, id);
        return color;
    }

    public static String getResString(Context context, @StringRes int id) {
        String str = context.getResources().getString(id);
        return str;
    }

    public static Bitmap compressBySampleSize(Bitmap bitmap, int quality, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] imageData = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
    }

    public static Bitmap compressBySize(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) maxWidth / width, (float) maxHeight / height);
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap compressByQuality(Bitmap bitmap, int quality) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] imageData = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
    }

    public static int pxToSp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    public static int spToPx(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    public static int pxToDp(Context context, int px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int getScreenOrientation(Activity activity) {
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }

    public static void showDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setPositiveButton("确定", (dialog, id) -> {
            dialog.cancel();
        }).setNegativeButton("取消", (dialog, id) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void startActivity(Context context, Class<?> cls) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    /**
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     * @param context
     * @return
     */
    public static void showToast(Context context, String content) {
        if (context != null && content != null) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void dispatchTakePictureIntent(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(takePictureIntent, requestCode);
        } else {
            Toast.makeText(activity, "can not start", Toast.LENGTH_SHORT).show();
        }
    }

    public static void makePhoneCall(Context context, String phoneNumber) {
        if (context == null || phoneNumber == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            showToast(context, "can not make mhone call");
        }
    }

    public static void sendEmail(Context context, String emailAddress) {
        if (context == null || emailAddress == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            showToast(context, "can not send email");
        }
    }

    public static void openWebPage(Context context, String url) {
        if (context == null || url == null) {
            return;
        }
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            showToast(context, "can not open");
        }
    }

    /**
     * 需要权限
     * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static int getScreenWidth(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.widthPixels;
        }
        return 0;
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(metrics);
            return metrics.heightPixels;
        }
        return 0;
    }

    public static String[] convertListToArray(List<String> list) {
        String[] array = new String[list.size()];
        return list.toArray(array);
    }

    public static List<Character> toCharList(String str) {
        List<Character> charList = new ArrayList<>();
        for (char c : str.toCharArray()) {
            charList.add(c);
        }
        return charList;
    }

    public static boolean containsSubstring(String str, String substring) {
        return str.contains(substring);
    }

    public static int square(int number) {
        return number * number;
    }

    public static int getMaxValue(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    public static int getMinValue(int[] array) {
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }

    public static String[] splitString(String str, String delimiter) {
        return str.split(delimiter);
    }

    public static String joinStrings(String[] strings, String delimiter) {
        return String.join(delimiter, strings);
    }

    public static boolean startsWith(String str, String prefix) {
        return str.startsWith(prefix);
    }

    public static boolean endsWith(String str, String suffix) {
        return str.endsWith(suffix);
    }

    public static String replaceString(String str, String target, String replacement) {
        return str.replace(target, replacement);
    }

    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    public static char[] toCharArray(String str) {
        return str.toCharArray();
    }

    public static int indexOf(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i;
            }
        }
        return -1;
    }

    public static String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        } else {
            return str.substring(0, maxLength);
        }
    }

    public static void sortArray(int[] array) {
        Arrays.sort(array);
    }

    public static String reverseWords(String str) {
        String[] words = str.split(" ");
        StringBuilder reversedStr = new StringBuilder();

        for (int i = words.length - 1; i >= 0; i--) {
            reversedStr.append(words[i]).append(" ");
        }

        return reversedStr.toString().trim();
    }

    public static boolean isPalindrome(String str) {
        String reversedStr = new StringBuilder(str).reverse().toString();
        return str.equals(reversedStr);
    }

    public static double calculateMedian(int[] array) {
        Arrays.sort(array);
        int n = array.length;

        if (n % 2 == 0) {
            return (array[n / 2 - 1] + array[n / 2]) / 2.0;
        } else {
            return array[n / 2];
        }
    }

    public static float parseFloat(String str) {
        return Float.parseFloat(str);
    }

    public static double parseDouble(String str) {
        return Double.parseDouble(str);
    }

    public static String getCurrentThreadName() {
        Thread currentThread = Thread.currentThread();
        return currentThread.getName();
    }

    public static String[] copyArray(String[] array) {
        String[] newArray = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static byte[] copyArray(byte[] array) {
        byte[] newArray = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static int[] copyArray(int[] array) {
        int[] newArray = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }
        return newArray;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    public static int calculateFactorial(int number) {
        if (number == 0 || number == 1) {
            return 1;
        } else {
            return number * calculateFactorial(number - 1);
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isPrime(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static String reverseString(String str) {
        StringBuilder reversedStr = new StringBuilder(str);
        return reversedStr.reverse().toString();
    }

    public static String capitalizeFirstLetter(String str) {
        if (str.isEmpty()) {
            return str;
        }
        char firstChar = Character.toUpperCase(str.charAt(0));
        String remainingStr = str.substring(1);
        return firstChar + remainingStr;
    }

    public static boolean isEven(int number) {
        return number % 2 == 0;
    }

    public static double calculateAverage(int[] array) {
        int sum = 0;

        for (int num : array) {
            sum += num;
        }

        return (double) sum / array.length;
    }

    public static String decimalToBinary(int decimal) {
        return Integer.toBinaryString(decimal);
    }

    public static int binaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

    public static int getCurrentSecond() {
        LocalTime currentTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTime = LocalTime.now();
            return currentTime.getSecond();
        }
        return 0;
    }

    public static int parseInt(String str) {
        return Integer.parseInt(str);
    }

    public static DayOfWeek getCurrentDayOfWeek() {
        LocalDate currentDate = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
            if (currentDate != null) {
                return currentDate.getDayOfWeek();
            }
        }
        return null;
    }

    public static boolean containsElement(int[] array, int element) {
        for (int num : array) {
            if (num == element) {
                return true;
            }
        }
        return false;
    }

    public static boolean parseBooleanIgnoreCase(String str) {
        return Boolean.parseBoolean(str);
    }

    public static int countOccurrences(String text, String target) {
        int count = 0;
        int index = text.indexOf(target);

        while (index != -1) {
            count++;
            index = text.indexOf(target, index + target.length());
        }
        return count;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static boolean arraysEqual(int[] array1, int[] array2) {
        return Arrays.equals(array1, array2);
    }
    // 计算最小公倍数
    public static int lcm(int a, int b) {
        int gcd = a;
        while (gcd % b != 0) {
            gcd += a;
        }
        return gcd;
    }
    // 计算最大公约数和最小公倍数
    public static int[] gcdAndLcm(int a, int b) {
        int[] result = new int[2];
        // 计算最大公约数
        int gcd = a;
        while (gcd % b != 0) {
            gcd += a;
        }
        // 计算最小公倍数
        int lcm = (a * b) / gcd;
        result[0] = gcd;
        result[1] = lcm;
        return result;
    }
    public static int calculateGCD(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return calculateGCD(b, a % b);
        }
    }
    public File getFileDir(Context context) {
        if(context==null){return null;}
        return context.getFilesDir();
    }

    public File getCacheDir(Context context) {
        if(context==null){return null;}
        return context.getCacheDir();
    }

    public void writeFile(Context context,String fileName, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir(), fileName));
        fos.write(content.getBytes());
        fos.close();
    }

    public static String readFile(Context context,String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(new File(context.getFilesDir(), fileName));
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        fis.close();
        return sb.toString();
    }

    public static boolean deleteFile(Context context,String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.delete();
    }
    public static String getLocalMacAddress(Context mContext) {
        try {
            WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
    }
}
