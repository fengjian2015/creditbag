package com.fly.credit.jiana.util;

import static android.content.ContentValues.TAG;
import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;


import com.fly.credit.jiana.MyApplication;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class DeviceInfoUtil {

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }


    public static String getDeviceName() {
        return Settings.Secure.getString(MyApplication.getApplication().getContentResolver(), "bluetooth_name");
    }

    public static String getKernelVersion() {
        try {
            return System.getProperty("os.version");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static boolean isLocServiceEnable() {
        LocationManager locationManager = (LocationManager) MyApplication.getApplication().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true;
        }
        return false;
    }

    public static void openLocService(){
        if (!isLocServiceEnable()) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getApplication().startActivity(intent);
        }
    }
    /**
     * 获取当前网络连接的类型
     *
     * @param context context
     * @return int
     */
    public static int getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            return 0;
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        TelephonyManager telephonyManager =
                (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") int networkType = telephonyManager.getNetworkType();
        return networkType;
    }

    public static String getTimeZoneId() {
        TimeZone aDefault = TimeZone.getDefault();
        return aDefault.getID();
    }

    public static String getTimeZone() {
        TimeZone aDefault = TimeZone.getDefault();
        return aDefault.getDisplayName(false, TimeZone.SHORT);
    }


    public static String getOperatorName() {
        TelephonyManager systemService = (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        return systemService.getSimOperatorName();
    }


    public static String getPhoneType() {
        TelephonyManager systemService = (TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (systemService == null) return "0";
        switch (systemService.getPhoneType()) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return "1";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "3";
            case TelephonyManager.PHONE_TYPE_SIP:
                return "4";
            default:
                return "0";
        }
    }


    public static String isUsbDebug() {
        return Settings.Secure.getInt(MyApplication.getApplication().getContentResolver(), Settings.Secure.ADB_ENABLED, 0
        ) > 0 ? "true" : "false";
    }


    public static String isVpn() {
        ConnectivityManager systemService = (ConnectivityManager) MyApplication.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = systemService.getNetworkInfo(ConnectivityManager.TYPE_VPN);
        if (networkInfo == null) return "false";
        return networkInfo.isConnected() ? "true" : "false";
    }


    public static String isProxy() {
        try {
            String proxyAddress;
            int proxyPort;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                proxyAddress = System.getProperty("http.proxyHost");
                String property = System.getProperty("http.proxyPort");
                if (TextUtils.isEmpty(property)) property = "-1";
                proxyPort = stringToInt(property);
            } else {
                proxyAddress = Proxy.getHost(MyApplication.getApplication());
                proxyPort = Proxy.getPort(MyApplication.getApplication());
            }
            return !TextUtils.isEmpty(proxyAddress) && proxyPort != -1 ? "true" : "false";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }

    public static Integer stringToInt(String i) {
        try {
            return Integer.parseInt(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getWifiInfo() {
        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getBSSID() == null ? "" : info.getBSSID();
    }


    @SuppressLint("MissingPermission")
    public static String getWifiMac() {
        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress() == null ? "" : info.getMacAddress();
    }

    @SuppressLint("MissingPermission")
    public static String getWifiName() {
        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getSSID() == null ? "" : info.getSSID();
    }

    // 获取当前热点最新的信号强度
    public static String getCurrentNetworkRssi() {
        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) return "";
        return String.valueOf(wifiInfo.getRssi());
    }

    //获取 GAID
    public static String getGAID() {
        String gaid = "";
        AdvertisingIdClient.Info adInfo = null;
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(MyApplication.getApplication().getApplicationContext());
        } catch (IOException e) {
            // Unrecoverable error connecting to Google Play services (e.g.,
            // the old version of the service doesn't support getting AdvertisingId).
            Log.e("getGAID", "IOException");
        } catch (GooglePlayServicesNotAvailableException e) {
            // Google Play services is not available entirely.
            Log.e("getGAID", "GooglePlayServicesNotAvailableException");
        } catch (Exception e) {
            Log.e("getGAID", "Exception:" + e.toString());
            // Encountered a recoverable error connecting to Google Play services.
        }
        if (adInfo != null) {
            gaid = adInfo.getId();
        }
        return gaid;
    }



    /**
     * wifi列表
     *
     * @return
     */
    public static List<String> getWifiList() {
        WifiManager wifiManager = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> scanWifiList = wifiManager.getScanResults();
        List<String> wifiList = new ArrayList<>();
        if (scanWifiList != null && scanWifiList.size() > 0) {
            for (int i = 0; i < scanWifiList.size(); i++) {
                ScanResult scanResult = scanWifiList.get(i);
                if (!TextUtils.isEmpty(scanResult.SSID) && !wifiList.contains(scanResult.SSID)) {
                    wifiList.add(scanResult.SSID);
                }
            }
            return wifiList;
        } else {
            Log.e(TAG, "非常遗憾搜索到wifi");
            return wifiList;
        }
    }

    public static int checkSDK(){
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
           return 1;
        }
        return 0;
    }

    public static void getPhoneState(Context context) {
        final TelephonyManager telephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener MyPhoneListener = new PhoneStateListener() {
            @Override
            //获取对应网络的ID，这个方法在这个程序中没什么用处
            public void onCellLocationChanged(CellLocation location) {
                if (location instanceof GsmCellLocation) {
                    int CID = ((GsmCellLocation) location).getCid();
                } else if (location instanceof CdmaCellLocation) {
                    int ID = ((CdmaCellLocation) location).getBaseStationId();
                }
            }

            //系统自带的服务监听器，实时监听网络状态
            @Override
            public void onServiceStateChanged(ServiceState serviceState) {
                super.onServiceStateChanged(serviceState);
            }

            //这个是我们的主角，就是获取对应网络信号强度
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                //这个ltedbm 是4G信号的值
                String signalinfo = signalStrength.toString();
                String[] parts = signalinfo.split(" ");
                String ltedbm = String.valueOf(parts[9]);
                //这个dbm 是2G和3G信号的值
                int asu = signalStrength.getGsmSignalStrength();
                int dbm = -113 + 2 * asu;
                MyApplication.setDbm(dbm);
                if (ActivityCompat.checkSelfPermission(com.fly.credit.jiana.util.ActivityManager.INSTANCE.getCurrentActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_LTE) {
                    Log.i("NetWorkUtil", "网络：LTE 信号强度：" + ltedbm + "======Detail:" + signalinfo);
                } else if (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSUPA ||
                        telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS) {
                    String bin;
                    if (dbm > -75) {
                        bin = "网络很好";
                    } else if (dbm > -85) {
                        bin = "网络不错";
                    } else if (dbm > -95) {
                        bin = "网络还行";
                    } else if (dbm > -100) {
                        bin = "网络很差";
                    } else {
                        bin = "网络错误";
                    }
                    MyApplication.setDbm(dbm);
                    Log.i("NetWorkUtil", "网络：WCDMA 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
                } else {
                    String bin;
                    if (asu < 0 || asu >= 99) bin = "网络错误";
                    else if (asu >= 16) bin = "网络很好";
                    else if (asu >= 8) bin = "网络不错";
                    else if (asu >= 4) bin = "网络还行";
                    else bin = "网络很差";
                    MyApplication.setDbm(dbm);
                    Log.i("NetWorkUtil", "网络：GSM 信号值：" + dbm + "========强度：" + bin + "======Detail:" + signalinfo);
                }
                super.onSignalStrengthsChanged(signalStrength);
            }
        };
        telephonyManager.listen(MyPhoneListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }


    /**
     * 获取IMEI
     */
    public static String getIMEI() {
        String IMEI;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            IMEI = DeviceUtils.getAndroidID();
        } else {
            IMEI = ((TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = DeviceUtils.getAndroidID();
            }
        }
        return IMEI;
    }

    public static String getLocaleDisplayLanguage(){
        String language =Locale.getDefault().getLanguage();
        if (language.contains("zh")){
            return "中文";
        }else if (language.contains("en")){
            return "英文";
        }else {
            return language;
        }
    }

    public static String getlocaleDisplayName(){
        String country =Locale.getDefault().getCountry();
        if (country.contains("CN")){
            return getLocaleDisplayLanguage()+"(中国)";
        }else if (country.contains("GH")){
            return getLocaleDisplayLanguage()+"(加纳)";
        }else {
            return getLocaleDisplayLanguage()+"("+country+")";
        }
    }

    public static String getIMEI1() {
        String IMEI;
        try {
            IMEI =  ((TelephonyManager) MyApplication.getApplication().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            return IMEI;
        } catch (Exception ignored) {

        }
        return null;
    }

    public static Long stringToLong(String i) {
        try {
            return Long.parseLong(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 获取传感器数量
     *
     * @return
     */
    public static String getSensorCount() {
        SensorManager sm = (SensorManager) MyApplication.getApplication().getSystemService(SENSOR_SERVICE);  //获取系统的传感器服务并创建实例
        List<Sensor> list = sm.getSensorList(Sensor.TYPE_ALL);  //获取传感器的集合
        if (list != null) {
            return list.size() + "";
        }
        return 0 + "";
    }


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

    /**
     *  获取android当前可用运行内存大小
     */
    public static long getAvailMemory() {
        ActivityManager am = (ActivityManager) MyApplication.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // mi.availMem; 当前系统的可用内存
        return mi.availMem;// 将获取的内存大小规格化
    }


    /**
     * 获取剩余空间
     *
     * @return
     */
    public static String getAvailableSpace() {
        return queryWithStorageManager(MyApplication.getApplication())[1];
    }

    /**
     * 获取总空间
     *
     * @return
     */
    public static String getTotalRam() {
        return queryWithStorageManager(MyApplication.getApplication())[0];
    }


    /**
     * 获取内存大小和剩余空间
     *
     * @param context
     * @return
     */
    private static String[] queryWithStorageManager(Context context) {
        String[] size = new String[]{"0", "0"};
        //5.0 查外置存储
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        int version = Build.VERSION.SDK_INT;
        if (version < Build.VERSION_CODES.M) {//小于6.0
            try {
                Method getVolumeList = StorageManager.class.getDeclaredMethod("getVolumeList");
                StorageVolume[] volumeList = (StorageVolume[]) getVolumeList.invoke(storageManager);
                long totalSize = 0, availableSize = 0;
                if (volumeList != null) {
                    Method getPathFile = null;
                    for (StorageVolume volume : volumeList) {
                        if (getPathFile == null) {
                            getPathFile = volume.getClass().getDeclaredMethod("getPathFile");
                        }
                        File file = (File) getPathFile.invoke(volume);
                        totalSize += file.getTotalSpace();
                        availableSize += file.getUsableSpace();
                    }
                }
                size[0] = totalSize + "";
                size[1] = availableSize + "";
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Method getVolumes = StorageManager.class.getDeclaredMethod("getVolumes");//6.0
                List<Object> getVolumeInfo = (List<Object>) getVolumes.invoke(storageManager);
                long total = 0L, used = 0L, systemSize = 0L;
                for (Object obj : getVolumeInfo) {

                    Field getType = obj.getClass().getField("type");
                    int type = getType.getInt(obj);
                    if (type == 1) {//TYPE_PRIVATE
                        long totalSize = 0L;
                        //获取内置内存总大小
                        if (version >= Build.VERSION_CODES.O) {//8.0
                            Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid");
                            String fsUuid = (String) getFsUuid.invoke(obj);
                            totalSize = getTotalSize(context, fsUuid);//8.0 以后使用
                        } else if (version >= Build.VERSION_CODES.N_MR1) {//7.1.1
                            Method getPrimaryStorageSize = StorageManager.class.getMethod("getPrimaryStorageSize");//5.0 6.0 7.0没有
                            totalSize = (long) getPrimaryStorageSize.invoke(storageManager);
                        }
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);

                            if (totalSize == 0) {
                                totalSize = f.getTotalSpace();
                            }
                            systemSize = totalSize - f.getTotalSpace();
                            used += totalSize - f.getFreeSpace();
                            total += totalSize;
                        }
                    } else if (type == 0) {//TYPE_PUBLIC
                        //外置存储
                        Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                        boolean readable = (boolean) isMountedReadable.invoke(obj);
                        if (readable) {
                            Method file = obj.getClass().getDeclaredMethod("getPath");
                            File f = (File) file.invoke(obj);
                            used += f.getTotalSpace() - f.getFreeSpace();
                            total += f.getTotalSpace();
                        }
                    } else if (type == 2) {//TYPE_EMULATED

                    }
                }
                size[0] = (total + systemSize) + "";
                size[1] = (total - used) + "";
                LogUtil.d(
                        "总内存 total = " + total + "\n已用 used(with system) = " + used + "可用 available = "
                                + (total - used) + "系统大小：" + systemSize
                );
            } catch (SecurityException e) {
                Log.e(TAG, "缺少权限：permission.PACKAGE_USAGE_STATS");
            } catch (Exception e) {
                e.printStackTrace();
                size = queryWithStatFs(size);
            }
        }
        return size;
    }

    private static String[] queryWithStatFs(String[] strings) {
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        //存储块
        long blockCount = statFs.getBlockCount();
        //块大小
        long blockSize = statFs.getBlockSize();
        //可用块数量
        long availableCount = statFs.getAvailableBlocks();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        strings[0] = (blockSize * blockCount) + "";
        strings[1] = (blockSize * availableCount) + "";
        return strings;
    }


    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private static long getTotalSize(Context context, String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = context.getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */
    public static String getBasebandVer() {
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[]{String.class, String.class});
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            return (String) result;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 屏幕分辨率
     *
     * @return
     */
    public static String getScreenResolution() {
        WindowManager windowManager = com.fly.credit.jiana.util.ActivityManager.INSTANCE.getCurrentActivity().getWindow().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        return height + " * " + width;
    }

    /**
     * 屏幕分辨率
     *
     * @return
     */
    public static String getScreenResolutionHeight() {
        WindowManager windowManager = com.fly.credit.jiana.util.ActivityManager.INSTANCE.getCurrentActivity().getWindow().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int height = metrics.heightPixels;
        return height+"";
    }

    /**
     * 屏幕分辨率
     *
     * @return
     */
    public static String getScreenResolutionWidth() {
        WindowManager windowManager = com.fly.credit.jiana.util.ActivityManager.INSTANCE.getCurrentActivity().getWindow().getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        return width+"";
    }

    public static String getPhysicalSize(){
        DisplayMetrics metrics = new DisplayMetrics();
        com.fly.credit.jiana.util.ActivityManager.INSTANCE.getCurrentActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float screenWidthInInches = metrics.widthPixels / metrics.xdpi;
        float screenHeightInInches = metrics.heightPixels / metrics.ydpi;
        return screenHeightInInches + " * " + screenWidthInInches;
    }

    public static String getIPAddress() {
        WifiManager wifi_service = (WifiManager) MyApplication.getApplication().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        String localIp = getLocalIPAddress();
        if (wifiinfo.getIpAddress() == 0) {
            //DhcpInfo中的ipAddress是一个int型的变量，通过Formatter将其转化为字符串IP地址
            return localIp;
        } else {
            return Formatter.formatIpAddress(dhcpInfo.ipAddress);
        }
    }

    private static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

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

    //  public static final int KEYBOARD_UNDEFINED = 0; // 未定义的键盘
    //    public static final int KEYBOARD_NOKEYS = 1; // 无键键盘，没有外接键盘时为该类型
    //    public static final int KEYBOARD_QWERTY = 2; // 标准外接键盘
    //    public static final int KEYBOARD_12KEY = 3; // 12键小键盘
    // 软件盘是否可以显示
    public static String onEvaluateInputViewShown() {
        Configuration config = MyApplication.getApplication().getResources().getConfiguration();
        if (config.keyboard == Configuration.KEYBOARD_NOKEYS){
            return "1";
        }else if (config.keyboard == Configuration.KEYBOARD_UNDEFINED){
            return "0";
        }else if (config.keyboard == Configuration.KEYBOARD_QWERTY){
            return "2";
        }else if (config.keyboard == Configuration.KEYBOARD_12KEY){
            return "3";
        }
        return "0";
    }

    public static String getCounter(){
        BatteryManager manager = (BatteryManager) MyApplication.getApplication().getSystemService(BATTERY_SERVICE);
        int counter = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
        return counter+"";
    }


}
