package com.fly.credit.jiana.util;


import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.CHANGE_WIFI_STATE;
import static android.Manifest.permission.INTERNET;
import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;


import com.fly.credit.jiana.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeviceUtils {
    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether device is rooted.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
                "/system/sbin/", "/usr/bin/", "/vendor/bin/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return whether ADB is enabled.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAdbEnabled() {
        return Settings.Secure.getInt(
                MyApplication.getApplication().getContentResolver(),
                Settings.Global.ADB_ENABLED, 0
        ) > 0;
    }


    /**
     * Return the version name of device's system.
     *
     * @return the version name of device's system
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Return version code of device's system.
     *
     * @return version code of device's system
     */
    public static int getSDKVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * Return the android id of device.
     *
     * @return the android id of device
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID() {
        String id = Settings.Secure.getString(
                MyApplication.getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        if ("9774d56d682e549c".equals(id)) return "";
        return id == null ? "" : id;
    }

    public static boolean isSunmiQ2(){
        String brand = Build.BRAND;
        String model = Build.MODEL;
        if ("SUNMI".equals(brand)&&"Qbao_h2".equals(model)){
            return true;
        }
        return false;
    }

    public static String isCal(){
       return CalendarReminderUtils.timeStamp2Date(MyApplication.getAppStartTime());
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />},
     * {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @return the MAC address
     */
    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, CHANGE_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddress((String[]) null);
        if (!TextUtils.isEmpty(macAddress) || getWifiEnabled()) return macAddress;
        setWifiEnabled(true);
        setWifiEnabled(false);
        return getMacAddress((String[]) null);
    }

    private static boolean getWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) MyApplication.getApplication().getSystemService(WIFI_SERVICE);
        if (manager == null) return false;
        return manager.isWifiEnabled();
    }

    /**
     * Enable or disable wifi.
     * <p>Must hold {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />}</p>
     *
     * @param enabled True to enabled, false otherwise.
     */
    @RequiresPermission(CHANGE_WIFI_STATE)
    private static void setWifiEnabled(final boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager manager = (WifiManager) MyApplication.getApplication().getSystemService(WIFI_SERVICE);
        if (manager == null) return;
        if (enabled == manager.isWifiEnabled()) return;
        manager.setWifiEnabled(enabled);
    }

    /**
     * Return the MAC address.
     * <p>Must hold {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />},
     * {@code <uses-permission android:name="android.permission.INTERNET" />}</p>
     *
     * @return the MAC address
     */
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

    @RequiresPermission(ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiInfo() {
        try {
            final WifiManager wifi = (WifiManager) MyApplication.getApplication()
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

    private static ShellUtils.CommandResult execCmd(final String command, final boolean isRooted) {
        return ShellUtils.execCmd(command, isRooted);
    }

    private static String getMacAddressByFile() {
        ShellUtils.CommandResult result = execCmd("getprop wifi.interface", false);
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

    /**
     * Return the manufacturer of the product/hardware.
     * <p>e.g. Xiaomi</p>
     *
     * @return the manufacturer of the product/hardware
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * Return the model of device.
     * <p>e.g. MI2SC</p>
     *
     * @return the model of device
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

    /**
     * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
     * element in the list.
     *
     * @return an ordered list of ABIs supported by this device
     */
    public static String[] getABIs() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_ABIS;
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
            }
            return new String[]{Build.CPU_ABI};
        }
    }

    /**
     * Return whether device is tablet.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isTablet() {
        return (Resources.getSystem().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * Return whether device is emulator.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isEmulator() {
        boolean checkProperty = Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.toLowerCase().contains("vbox")
                || Build.FINGERPRINT.toLowerCase().contains("test-keys")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
        if (checkProperty) return true;

        String operatorName = "";
        TelephonyManager tm = (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            String name = tm.getNetworkOperatorName();
            if (name != null) {
                operatorName = name;
            }
        }
        boolean checkOperatorName = operatorName.toLowerCase().equals("android");
        if (checkOperatorName) return true;

        String url = "tel:" + "123456";
        Intent intent = new Intent();
        intent.setData(Uri.parse(url));
        intent.setAction(Intent.ACTION_DIAL);
        boolean checkDial = intent.resolveActivity(MyApplication.getApplication().getPackageManager()) == null;
        if (checkDial) return true;

//        boolean checkDebuggerConnected = Debug.isDebuggerConnected();
//        if (checkDebuggerConnected) return true;

        return false;
    }

    /**
     * Whether user has enabled development settings.
     *
     * @return whether user has enabled development settings.
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isDevelopmentSettingsEnabled() {
        return Settings.Global.getInt(
                MyApplication.getApplication().getContentResolver(),
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
        ) > 0;
    }


    private static final    String KEY_UDID = "KEY_UDID";
    private volatile static String udid;

    /**
     * Return the unique device id.
     * <pre>{1}{UUID(macAddress)}</pre>
     * <pre>{2}{UUID(androidId )}</pre>
     * <pre>{9}{UUID(random    )}</pre>
     *
     * @return the unique device id
     */
    public static String getUniqueDeviceId() {
        return getUniqueDeviceId("", true);
    }

    /**
     * Return the unique device id.
     * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
     * <pre>{prefix}{2}{UUID(androidId )}</pre>
     * <pre>{prefix}{9}{UUID(random    )}</pre>
     *
     * @param prefix The prefix of the unique device id.
     * @return the unique device id
     */
    public static String getUniqueDeviceId(String prefix) {
        return getUniqueDeviceId(prefix, true);
    }

    /**
     * Return the unique device id.
     * <pre>{1}{UUID(macAddress)}</pre>
     * <pre>{2}{UUID(androidId )}</pre>
     * <pre>{9}{UUID(random    )}</pre>
     *
     * @param useCache True to use cache, false otherwise.
     * @return the unique device id
     */
    public static String getUniqueDeviceId(boolean useCache) {
        return getUniqueDeviceId("", useCache);
    }

    /**
     * Return the unique device id.
     * <pre>android 10 deprecated {prefix}{1}{UUID(macAddress)}</pre>
     * <pre>{prefix}{2}{UUID(androidId )}</pre>
     * <pre>{prefix}{9}{UUID(random    )}</pre>
     *
     * @param prefix   The prefix of the unique device id.
     * @param useCache True to use cache, false otherwise.
     * @return the unique device id
     */
    public static String getUniqueDeviceId(String prefix, boolean useCache) {
        if (!useCache) {
            return getUniqueDeviceIdReal(prefix);
        }
        if (udid == null) {
            synchronized (DeviceUtils.class) {
                if (udid == null) {
                    final String id = MMKVCacheUtil.getString(KEY_UDID, null);
                    if (id != null) {
                        udid = id;
                        return udid;
                    }
                    return getUniqueDeviceIdReal(prefix);
                }
            }
        }
        return udid;
    }

    private static String getUniqueDeviceIdReal(String prefix) {
        try {
            final String androidId = getAndroidID();
            if (!TextUtils.isEmpty(androidId)) {
                return saveUdid(prefix + 2, androidId);
            }

        } catch (Exception ignore) {/**/}
        return saveUdid(prefix + 9, "");
    }

    @RequiresPermission(allOf = {ACCESS_WIFI_STATE, INTERNET, CHANGE_WIFI_STATE})
    public static boolean isSameDevice(final String uniqueDeviceId) {
        // {prefix}{type}{32id}
        if (TextUtils.isEmpty(uniqueDeviceId) && uniqueDeviceId.length() < 33) return false;
        if (uniqueDeviceId.equals(udid)) return true;
        final String cachedId = MMKVCacheUtil.getString(KEY_UDID, null);
        if (uniqueDeviceId.equals(cachedId)) return true;
        int st = uniqueDeviceId.length() - 33;
        String type = uniqueDeviceId.substring(st, st + 1);
        if (type.startsWith("1")) {
            String macAddress = getMacAddress();
            if (macAddress.equals("")) {
                return false;
            }
            return uniqueDeviceId.substring(st + 1).equals(getUdid("", macAddress));
        } else if (type.startsWith("2")) {
            final String androidId = getAndroidID();
            if (TextUtils.isEmpty(androidId)) {
                return false;
            }
            return uniqueDeviceId.substring(st + 1).equals(getUdid("", androidId));
        }
        return false;
    }

    private static String saveUdid(String prefix, String id) {
        udid = getUdid(prefix, id);
        MMKVCacheUtil.putString(KEY_UDID, udid);
        return udid;
    }

    private static String getUdid(String prefix, String id) {
        if (id.equals("")) {
            return prefix + UUID.randomUUID().toString().replace("-", "");
        }
        return prefix + UUID.nameUUIDFromBytes(id.getBytes()).toString().replace("-", "");
    }

    public static boolean hasXposed(Context context) {
        if (context == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> appliacationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo item : appliacationInfoList) {
            if (item.packageName.equals("de.robv.android.xposed.installer")) {
                return true;
            }
        }
        return false;
    }
    public static String getCPUMaxFreq() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"));
            String maxFreq = br.readLine();
            br.close();
            return maxFreq;
        } catch (IOException e) {
            e.printStackTrace();
            return "N/A";
        }
    }

}
