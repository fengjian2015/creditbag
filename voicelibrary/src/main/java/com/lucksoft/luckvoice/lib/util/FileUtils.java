package com.lucksoft.luckvoice.lib.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.lucksoft.luckvoice.lib.VoicePlay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import kotlin.jvm.functions.Function2;

/**
 * Time：2023/2/17
 * Author：feng
 * Description：
 */
public class FileUtils {

    public static AssetFileDescriptor getAssetFileDescription(Context context, final List<String> voicePlay, int[] counter) throws IOException {
        if (counter[0] >= voicePlay.size())return null;
        AssetFileDescriptor assetFileDescription = getAssetFileDescription(context, String.format(VoicePlay.FILE_PATH, voicePlay.get(counter[0])));
        if (assetFileDescription == null) {
            counter[0]++;
            return getAssetFileDescription(context, voicePlay, counter);
        }
        return assetFileDescription;
    }

    public static void getAssetFileDescription(Context context, final List<String> voicePlay, Function2 function) throws IOException {
        if (voicePlay == null){
            function.invoke(new ArrayList<AssetFileDescriptor>(),new ArrayList<Long>());
            return;
        }
        ArrayList<Long> arrayList = new ArrayList();
        ArrayList<AssetFileDescriptor> assetFileDescriptors = new ArrayList<>();
        for (String s : voicePlay){
            AssetFileDescriptor assetFileDescription = getAssetFileDescription(context, String.format(VoicePlay.FILE_PATH, s));
            if (assetFileDescription !=null){
                assetFileDescriptors.add(assetFileDescription);
                arrayList.add(VoiceTextTemplate.getDelayTimeList().get(s));
            }
        }
        function.invoke(assetFileDescriptors,arrayList);
    }

    /**
     * Assets获取资源
     *
     * @param context
     * @param filename
     * @return
     * @throws IOException
     */
    public static AssetFileDescriptor getAssetFileDescription(Context context, String filename) throws IOException {
        AssetManager manager = context.getApplicationContext().getAssets();
        try {
            return manager.openFd(filename);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
