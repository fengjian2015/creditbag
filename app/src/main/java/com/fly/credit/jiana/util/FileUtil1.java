package com.fly.credit.jiana.util;

/**
 * Time：2024/3/16
 * Author：feng
 * Description：
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fly.credit.jiana.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author Nevio
 * on 2019/1/25
 */
public class FileUtil1 {

    private static final int FLAG_SUCCESS = 1;//创建成功
    private static final int FLAG_EXISTS = 2;//已存在
    private static final int FLAG_FAILED = 3;//创建失败
    //文件夹名
    private static String mainProjectName = "MainProject";
    private static String mainDirName = mainProjectName.concat("/");
    private static final String DIR_NAME_IMG = "images/";
    private static final String DIR_NAME_DOWNLOAD = "download/";
    private static final String DIR_NAME_TAPE_RECORD = "tapeRecord/";
    private static final String DIR_NAME_VIDEO = "video/";
    private static final String DIR_NAME_DOC = "doc/";
    private static final String DIR_NAME_DEBUG_LOG = "debugLog/";
    private static final String DIR_NAME_APK = "apk/";
    private static final String DIR_NAME_WEB_VIEW = "webView/";
    private static final String DIR_NAME_ZIP = "zip/";


    public static void init(String projectName) {
        mainProjectName = projectName;
        mainDirName = mainProjectName.concat("/");
    }

    @NonNull
    public static String getProjectName() {
        return mainProjectName;
    }

    /**
     * 内部存储中的files路径
     */
    public static String getInnerFilePath(Context context) {
        if (context == null) {
            return "";
        }
        String internalFilePath = context.getFilesDir().getAbsolutePath();
        if (!internalFilePath.endsWith(File.separator)) {
            return internalFilePath + File.separator;
        }
        return internalFilePath;
    }

    /**
     * 内部存储中的cache路径
     */
    public static String getInnerCachePath(Context context) {
        if (context == null) {
            return "";
        }
        String innerCachePath = context.getCacheDir().getAbsolutePath();
        if (!innerCachePath.endsWith(File.separator)) {
            return innerCachePath + File.separator;
        }
        return innerCachePath;
    }

    /**
     * SD卡中的files路径,无SD卡则返回对应内部路径
     */
    public static String getSDFilePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalFilesDir(mainDirName);
            if (file != null) {
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(File.separator)) {
                    return filePath + File.separator;
                }
                return filePath;
            }
        }
        return getInnerFilePath(context);
    }

    /**
     * SD卡中的cache路径,无SD卡则返回对应内部路径
     */
    public static String getSDCachePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(File.separator)) {
                    return filePath + File.separator;
                }
                return filePath;
            }
        }
        return getInnerCachePath(context);
    }

    /**
     * SD卡的根路径,无SD卡则返回对应内部路径
     */
    public static String getSDDirPath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            if (!TextUtils.isEmpty(path)) {
                if (!path.endsWith(File.separator)) {
                    return path + File.separator + mainDirName;
                }
                return path + mainDirName;
            } else {
                File file = Environment.getExternalStoragePublicDirectory(mainDirName);
                if (file != null) {
                    String externalPath = file.getAbsolutePath();
                    if (!externalPath.endsWith(File.separator)) {
                        return externalPath + File.separator + mainDirName;
                    }
                    return externalPath + mainDirName;
                }
            }
        }
        return getInnerFilePath(context);
    }

    /**
     * SD卡debugLog路径
     */
    @NonNull
    public static String getSDDebugLogPath(@NonNull Context context) {
        createDir(getSDDirPath(context) + DIR_NAME_DEBUG_LOG);
        return getSDDirPath(context) + DIR_NAME_DEBUG_LOG;
    }

    /**
     * SD卡文档路径
     */
    @NonNull
    public static String getSdDocPath(Context context) {
        createDir(getSDDirPath(context) + DIR_NAME_DOC);
        return getSDDirPath(context) + DIR_NAME_DOC;
    }

    /**
     * SD卡下载路径
     */
    @NonNull
    public static String getSDDownloadPath(Context context) {
        createDir(getSDDirPath(context) + DIR_NAME_DOWNLOAD);
        return getSDDirPath(context) + DIR_NAME_DOWNLOAD;
    }

    /**
     * apk下载路径
     */
    @NonNull
    public static String getSDApkPath(Context context) {
        createDir(getSDDirPath(context) + DIR_NAME_APK);
        return getSDDirPath(context) + DIR_NAME_APK;
    }

    /**
     * SD卡录音路径
     */
    @NonNull
    public static String getSDTapeRecordPath(Context context) {
        createDir(getSDDirPath(context) + DIR_NAME_TAPE_RECORD);
        return getSDDirPath(context) + DIR_NAME_TAPE_RECORD;
    }

    /**
     * webView缓存路径
     */
    @NonNull
    public static String getInnerWebCachePath(Context context) {
        if (context == null) {
            return "";
        }
        createDir(getInnerCachePath(context) + DIR_NAME_WEB_VIEW);
        return getInnerCachePath(context) + DIR_NAME_WEB_VIEW;
    }

    @NonNull
    public static String getInnerApkPath(Context context) {
        if (context == null) {
            return "";
        }
        createDir(getInnerFilePath(context) + DIR_NAME_APK);
        return getInnerFilePath(context) + DIR_NAME_APK;
    }

    @NonNull
    public static String getInnerImgPath(Context context) {
        if (context == null) {
            return "";
        }
        createDir(getInnerFilePath(context) + DIR_NAME_IMG);
        return getInnerFilePath(context) + DIR_NAME_IMG;
    }

    @NonNull
    public static String getInnerDocPath(Context context) {
        if (context == null) {
            return "";
        }
        createDir(getInnerFilePath(context) + DIR_NAME_DOC);
        return getInnerFilePath(context) + DIR_NAME_DOC;
    }

    @NonNull
    public static String getInnerZipPath(Context context) {
        if (context == null) {
            return "";
        }
        createDir(getInnerFilePath(context) + DIR_NAME_ZIP);
        return getInnerFilePath(context) + DIR_NAME_ZIP;
    }

    /**
     * 创建apk下载文件名，传入版本号区分
     */
    @Nullable
    public static File createAPKFile(Context context, String versionName) {
        if (context == null) {
            return null;
        }
        if (versionName == null) {
            versionName = "";
        }
        String root = getSDApkPath(context);
        File file = new File(root, mainProjectName.concat(versionName).concat(".apk"));
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static void writeString(String path, String filename, String content) {
        if (TextUtils.isEmpty(filename) || content == null) {
            return;
        }
        File file = new File(path, filename);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取txt文档内容
     */
    public static String readString(String path, String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        File file = new File(path, fileName);
        StringBuilder sb = new StringBuilder();
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为"txt"文件
                try {
                    InputStream inputStream = new FileInputStream(file);
                    InputStreamReader streamReader
                            = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(streamReader);
                    String line;
                    //分行读取
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    inputStream.close();//关闭输入流
                } catch (java.io.FileNotFoundException e) {
//                    Logan.w("TestFile", "The File doesn't not exist.");
                    e.printStackTrace();
                } catch (IOException e) {
//                    Logan.w("TestFile", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    public static void deleteFile(String filePath){
        if (TextUtils.isEmpty(filePath)){
            return;
        }
        try {
            File file = new File(filePath);
            if (file.exists()){
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定目录下文件及目录
     */
    public static int deleteDir(String filePath) {
        int deleteNum = 0;
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                // 处理目录
                if (file.isDirectory()) {
                    File[] files = file.listFiles();
                    for (File file1 : files) {
                        deleteNum += deleteDir(file1.getAbsolutePath());
                    }
                }
                // 如果是文件，删除
                if (!file.isDirectory()) {
                    file.delete();
                    deleteNum++;
                } else {
                    // 目录下没有文件或者目录，删除
                    if (file.listFiles().length == 0) {
                        file.delete();
                        deleteNum++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deleteNum;
    }

    /**
     * 按保留时间删除缓存文件
     * @param path    路径
     * @param numDays 保留天数
     * @return 被删除文件数量
     */
    private int deleteCacheFolder(final File path, final int numDays) {
        int deletedFiles = 0;
        if (path != null && path.isDirectory()) {
            try {
                for (File child : path.listFiles()) {
                    //first delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += deleteCacheFolder(child, numDays);
                    }
                    //then delete the files and subdirectories in this dir
                    //only empty directories can be deleted, so sub dirs have been done first
                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
//                Logan.w(String.format("Failed to clean the cache, error %s", e.getMessage()));
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    public static boolean isFileExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        try {
            File f = new File(path);
            if (f.exists()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 创建单个文件
     * @param filePath 待创建的文件路径
     * @return 结果码
     */
    public static int createFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
//            Logan.w("filePath is empty");
            return FLAG_FAILED;
        }
        File file = new File(filePath);
        if (file.exists()) {
//            Logan.w("The directory [ " + filePath + " ] has already exists");
            return FLAG_EXISTS;
        }
        if (filePath.endsWith(File.separator)) {// 以 路径分隔符 结束，说明是文件夹
//            Logan.w("The file [ \" + filePath + \" ] can not be a directory");
            return FLAG_FAILED;
        }
        //判断父目录是否存在
        if (!file.getParentFile().exists()) {
            //父目录不存在 创建父目录
//            Logan.w("creating parent directory...");
            if (!file.getParentFile().mkdirs()) {
//                Logan.w("created parent directory failed...");
                return FLAG_FAILED;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {//创建文件成功
//                Logan.w("create file [ " + filePath + " ] success");
                return FLAG_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
//            Logan.w("create file [ " + filePath + " ] failed");
            return FLAG_FAILED;
        }
        return FLAG_FAILED;
    }

    /**
     * 创建文件夹
     */
    public static void createDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
//            Logan.w("dirPath is empty");
            return;
        }
        File dir = new File(dirPath);
        //文件夹是否已经存在
        if (dir.exists()) {
//            Logan.w("The directory [ " + dirPath + " ] has already exists");
        }
        //不是以路径分隔符"/"结束，则添加路径分隔符"/"
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }

        //判断父目录是否存在
        if (!dir.getParentFile().exists()) {
            //父目录不存在 创建父目录
//            Logan.w("creating parent directory...");
            if (!dir.getParentFile().mkdirs()) {
//                Logan.w("created parent directory failed...");
            }
        }
        //创建文件夹
        if (dir.mkdirs()) {
//            Logan.w("create directory [ " + dirPath + " ] success");
        }
//        Logan.w("create directory [ " + dirPath + " ] failed");
    }

    /**
     * @param bmp
     * @description 将图片保存到本地时进行压缩, 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是: File形式的图片确实被压缩了, 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     * 所谓的质量压缩，即为改变其图像的位深和每个像素的透明度，也就是说JPEG格式压缩后，原来图片中透明的元素将消失，所以这种格式很可能造成失真
     */
    public static File compressBmpToFile(Bitmap bmp) {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/shoppingMall/compressImgs/";
        String path = getInnerImgPath(MyApplication.getApplication()) + "/compressImg/";

        File file = new File(path + DateTool.INSTANCE.getServerTimestamp() + ".jpg");
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 700) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
//            copyExif(srcPathStr,file.getAbsolutePath());
//            return file.getAbsolutePath();
            return file;

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }





}

