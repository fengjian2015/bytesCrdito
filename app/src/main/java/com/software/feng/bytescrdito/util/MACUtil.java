package com.software.feng.bytescrdito.util;

import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import com.software.feng.bytescrdito.MyApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Time：2024/5/22
 * Author：feng
 * Description：
 */
public class MACUtil {
    private static final String LINE_SEP = System.getProperty("line.separator");

    public static String getCpuName() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String cpuName = "";

        try {
            FileReader fileReader = new FileReader(str1);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((str2 = bufferedReader.readLine()) != null) {
                if (TextUtils.isEmpty(str2)) {
                    continue;
                }
                String[] arrayOfString = str2.split(":\\s+", 2);
                if (TextUtils.equals(arrayOfString[0].trim(), "Hardware")) {
                    cpuName = arrayOfString[1];
                    break;
                }
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuName;
    }

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, CHANGE_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddress((String[]) null);
        if (!TextUtils.isEmpty(macAddress) || getWifiEnabled()) return macAddress;
        setWifiEnabled(true);
        setWifiEnabled(false);
        return getMacAddress((String[]) null);
    }

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE})
    public static String getMacAddress(final String... excepts) {
        String macAddress = getMacAddressByNetworkInterface();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByWifiInfo();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        macAddress = getMacAddressByFile();
        if (isAddressNotInExcepts(macAddress, excepts)) {
            return macAddress;
        }
        return "";
    }

    /**
     * The result of command.
     */
    public static class CommandResult {
        public int    result;
        public String successMsg;
        public String errorMsg;

        public CommandResult(final int result, final String successMsg, final String errorMsg) {
            this.result = result;
            this.successMsg = successMsg;
            this.errorMsg = errorMsg;
        }

        @Override
        public String toString() {
            return "result: " + result + "\n" +
                    "successMsg: " + successMsg + "\n" +
                    "errorMsg: " + errorMsg;
        }
    }


    /**
     * Execute the command.
     *
     * @param command  The command.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final boolean isRooted) {
        return execCmd(new String[]{command}, isRooted, true);
    }

    /**
     * Execute the command.
     *
     * @param command  The command.
     * @param envp     The environment variable settings.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command, final List<String> envp, final boolean isRooted) {
        return execCmd(new String[]{command},
                envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands, final boolean isRooted) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}), isRooted, true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param envp     The environment variable settings.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final List<String> envp,
                                        final boolean isRooted) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                true);
    }

    /**
     * Execute the command.
     *
     * @param commands The commands.
     * @param isRooted True to use root, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands, final boolean isRooted) {
        return execCmd(commands, isRooted, true);
    }

    /**
     * Execute the command.
     *
     * @param command         The command.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param command         The command.
     * @param envp            The environment variable settings.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final List<String> envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp == null ? null : envp.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param command         The command.
     * @param envp            The environment variable settings array.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String command,
                                        final String[] envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(new String[]{command}, envp, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final List<String> commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands == null ? null : commands.toArray(new String[]{}),
                isRooted,
                isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        return execCmd(commands, null, isRooted, isNeedResultMsg);
    }

    /**
     * Execute the command.
     *
     * @param commands        The commands.
     * @param envp            Array of strings, each element of which
     *                        has environment variable settings in the format
     *                        <i>name</i>=<i>value</i>, or
     *                        <tt>null</tt> if the subprocess should inherit
     *                        the environment of the current process.
     * @param isRooted        True to use root, false otherwise.
     * @param isNeedResultMsg True to return the message of result, false otherwise.
     * @return the single {@link CommandResult} instance
     */
    public static CommandResult execCmd(final String[] commands,
                                        final String[] envp,
                                        final boolean isRooted,
                                        final boolean isNeedResultMsg) {
        int result = -1;
        if (commands == null || commands.length == 0) {
            return new CommandResult(result, "", "");
        }
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = null;
        StringBuilder errorMsg = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec(isRooted ? "su" : "sh", envp, null);
            os = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                if (command == null) continue;
                os.write(command.getBytes());
                os.writeBytes(LINE_SEP);
                os.flush();
            }
            os.writeBytes("exit" + LINE_SEP);
            os.flush();
            result = process.waitFor();
            if (isNeedResultMsg) {
                successMsg = new StringBuilder();
                errorMsg = new StringBuilder();
                successResult = new BufferedReader(
                        new InputStreamReader(process.getInputStream(), "UTF-8")
                );
                errorResult = new BufferedReader(
                        new InputStreamReader(process.getErrorStream(), "UTF-8")
                );
                String line;
                if ((line = successResult.readLine()) != null) {
                    successMsg.append(line);
                    while ((line = successResult.readLine()) != null) {
                        successMsg.append(LINE_SEP).append(line);
                    }
                }
                if ((line = errorResult.readLine()) != null) {
                    errorMsg.append(line);
                    while ((line = errorResult.readLine()) != null) {
                        errorMsg.append(LINE_SEP).append(line);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (successResult != null) {
                    successResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return new CommandResult(
                result,
                successMsg == null ? "" : successMsg.toString(),
                errorMsg == null ? "" : errorMsg.toString()
        );
    }

    private static String getMacAddressByFile() {
        CommandResult result = execCmd("getprop wifi.interface", false);
        if (result.result == 0) {
            String name = result.successMsg;
            if (name != null) {
                result = execCmd("cat /sys/class/net/" + name + "/address", false);
                if (result.result == 0) {
                    String address = result.successMsg;
                    if (address != null && address.length() > 0) {
                        return address;
                    }
                }
            }
        }
        return "02:00:00:00:00:00";
    }

    @RequiresPermission(ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiInfo() {
        try {
            final WifiManager wifi = (WifiManager) MyApplication.application
                    .getApplicationContext().getSystemService(WIFI_SERVICE);
            if (wifi != null) {
                final WifiInfo info = wifi.getConnectionInfo();
                if (info != null) {
                    @SuppressLint({"HardwareIds", "MissingPermission"})
                    String macAddress = info.getMacAddress();
                    if (!TextUtils.isEmpty(macAddress)) {
                        return macAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (TextUtils.isEmpty(address)) {
            return false;
        }
        if ("02:00:00:00:00:00".equals(address)) {
            return false;
        }
        if (excepts == null || excepts.length == 0) {
            return true;
        }
        for (String filter : excepts) {
            if (filter != null && filter.equals(address)) {
                return false;
            }
        }
        return true;
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) MyApplication.application.getSystemService(WIFI_SERVICE);
        if (manager == null) return false;
        return manager.isWifiEnabled();
    }
    @RequiresPermission(CHANGE_WIFI_STATE)
    private static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) MyApplication.application.getSystemService(WIFI_SERVICE);
        if (manager == null) return;
        if (enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }


}
