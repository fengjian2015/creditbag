package com.fly.credit.jiana.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;


import com.fly.credit.jiana.MyApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ComUtil {
    private static final int IMG_WIDTH = 480; //超過此寬、高則會 resize圖片
    private static final int IMG_HIGHT = 800;
    private static final int COMPRESS_QUALITY = 70; //壓縮 JPEG使用的品質(70代表壓縮率為 30%)


    public static Location getLastKnownLocation(LocationManager locationManager) {
        try {
            List<String> providers = locationManager.getProviders(true);
            Location bestLocation = null;
            for (String provider : providers) {
                @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
                if (l == null) {
                    continue;
                }
                if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                    bestLocation = l;
                }
            }
            return bestLocation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }






    public static boolean isNumber(String string) {
        Pattern pattern = Pattern.compile("^[-+]?[0-9]");
        if (pattern.matcher(string).matches()) {
            //数字
            return true;
        } else {
            //非数字
            return false;
        }

    }

    public static Integer stringToInt(String i) {
        try {
            return Integer.parseInt(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Long stringToLong(String i) {
        try {
            return Long.parseLong(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }



    public static @Nullable
    File compressImage(String srcImgPath) {
        //先取得原始照片的旋轉角度
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(srcImgPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //計算取 Bitmap時的參數"inSampleSize"
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcImgPath, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > IMG_HIGHT || width > IMG_WIDTH) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= IMG_HIGHT
                    && (halfWidth / inSampleSize) >= IMG_WIDTH) {
                inSampleSize *= 2;
            }
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        //取出原檔的 Bitmap(若寬高超過會 resize)並設定原始的旋轉角度
        Bitmap srcBitmap = BitmapFactory.decodeFile(srcImgPath, options);
        if (srcBitmap == null) {
            Log.e("ImageUtils", "decode file error " + srcImgPath);
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        Bitmap outBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, false);
        //壓縮並存檔至 cache路徑下的 File
        File tempImgDir = new File(getImageDir());
        if (!tempImgDir.exists()) {
            tempImgDir.mkdirs();
        }
        String compressedImgName = SystemClock.currentThreadTimeMillis() + getFileNameFromPath(srcImgPath);
        File compressedImgFile = new File(tempImgDir, compressedImgName);
        FileOutputStream fos = null;
        try {
            compressedImgFile.createNewFile();
            fos = new FileOutputStream(compressedImgFile);
            outBitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, fos);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                srcBitmap.recycle();
                outBitmap.recycle();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return compressedImgFile;
    }

    /**
     * 获取文件名
     *
     * @param filepath dir+filename
     */
    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }



    public static String getImageDir() {
        Context context = MyApplication.getApplication();
        String path = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            File f = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if (f == null) {
                path = context.getFilesDir().getAbsolutePath();
            } else {
                path = f.getAbsolutePath();
            }
        } else {
            path = context.getFilesDir().getAbsolutePath();
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                f.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }
}
