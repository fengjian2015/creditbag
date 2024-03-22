package com.lucksoft.luckvoice.lib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Keep;

import com.lucksoft.luckvoice.lib.util.FileUtils;
import com.lucksoft.luckvoice.lib.util.VoiceCons;
import com.lucksoft.luckvoice.lib.util.VoiceTextTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import kotlin.jvm.functions.Function2;

/**
 * Time：2023/2/17
 * Author：feng
 * Description：
 */
@Keep
public class VoicePlay {
    public static final String FILE_PATH = "sound/%s.mp3";

    private ExecutorService mExecutorService;
    private Context mContext;

    private VoicePlay(Context context) {
        this.mContext = context;
        this.mExecutorService = Executors.newCachedThreadPool();
    }

    private volatile static VoicePlay mVoicePlay = null;

    /**
     * 单例
     *
     * @return
     */
    public static VoicePlay with(Context context) {
        if (mVoicePlay == null) {
            synchronized (VoicePlay.class) {
                if (mVoicePlay == null) {
                    mVoicePlay = new VoicePlay(context);
                }
            }
        }
        return mVoicePlay;
    }

    /**
     * 1.支持单独传入 {@link VoiceCons}
     * 2.支持单独传入数字字符串
     * 3.支持纯字母字符串
     * 4.支持混合传入1 2 3 点和汉字
     * 注意 未匹配到的自动忽略
     *
     * @param s
     * @param haveMoney 是否开启金额播报模式
     */
    public void play(ArrayList<String> s, boolean haveMoney) {
        ArrayList<String> input = new ArrayList<>();
        for (String s1 : s) {
            input.add(s1);
        }
        executeStart(input, haveMoney);
    }

    public void play(String s, boolean haveMoney) {
        ArrayList<String> input = new ArrayList<>();
        input.add(s);
        executeStart(input, haveMoney);
    }

    /**
     * 开启线程
     *
     * @param s
     */
    private void executeStart(ArrayList<String> s, boolean haveMoney) {
        if (s == null) {
            return;
        }
        mExecutorService.execute(() -> start(VoiceTextTemplate.getVoiceList(s, haveMoney)));
    }

    /**
     * 开始播报
     *
     * @param voicePlay
     */
    private void start(final List<String> voicePlay) {
        synchronized (VoicePlay.this) {
            final CountDownLatch mCountDownLatch = new CountDownLatch(1);
            if (voicePlay.isEmpty()) return;
            try {
                SoundPool soundPool;
                //实例化SoundPool
                //sdk版本21是SoundPool 的一个分水岭
                if (Build.VERSION.SDK_INT >= 21) {
                    SoundPool.Builder builder = new SoundPool.Builder();
                    //传入最多播放音频数量,
                    builder.setMaxStreams(2);
                    //AudioAttributes是一个封装音频各种属性的方法
                    AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
                    //设置音频流的合适的属性
                    attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
                    //加载一个AudioAttributes
                    builder.setAudioAttributes(attrBuilder.build());
                    soundPool = builder.build();
                } else {
                    /**
                     * 第一个参数：int maxStreams：SoundPool对象的最大并发流数
                     * 第二个参数：int streamType：AudioManager中描述的音频流类型
                     *第三个参数：int srcQuality：采样率转换器的质量。 目前没有效果。 使用0作为默认值。
                     */
                    soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
                }

                FileUtils.getAssetFileDescription(mContext, voicePlay, new Function2() {
                    @Override
                    public Object invoke(Object o, Object o2) {
                        AtomicBoolean isFistLoad = new AtomicBoolean(true);
                        ArrayList<Long> delayTimeList = (ArrayList<Long>) o2;
                        ArrayList<Integer> soundIdSet = new ArrayList<>();
                        for (AssetFileDescriptor assetFileDescriptor : (ArrayList<AssetFileDescriptor>) o) {
                            soundIdSet.add(soundPool.load(assetFileDescriptor, 1));
                        }
                        //异步需要等待加载完成，音频才能播放成功
                        soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> {
                            Log.d("VoicePlay", "status:"+status);
                            mExecutorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    //这里不要使用数量来对比是否初始化完毕，因为部分机型不按常理出牌
                                    if (status == 0) {
                                        Log.d("VoicePlay", "开始播放"+Thread.currentThread());
                                        //第一个参数soundID
                                        //第二个参数leftVolume为左侧音量值（范围= 0.0到1.0）
                                        //第三个参数rightVolume为右的音量值（范围= 0.0到1.0）
                                        //第四个参数priority 为流的优先级，值越大优先级高，影响当同时播放数量超出了最大支持数时SoundPool对该流的处理
                                        //第五个参数loop 为音频重复播放次数，0为值播放一次，-1为无限循环，其他值为播放loop+1次
                                        //第六个参数 rate为播放的速率，范围0.5-2.0(0.5为一半速率，1.0为正常速率，2.0为两倍速率)
                                        if (isFistLoad.get()){
                                            isFistLoad.set(false);
                                            try {
                                                Log.d("VoicePlay", "暂时休眠，避免音频未初始化完毕");
                                                //获取当前音频的时长
                                                Thread.sleep(100);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            for (int i=0 ;i<soundIdSet.size();i++){
                                                int soundId = soundIdSet.get(i);
                                                long delaytime = delayTimeList.get(i) ;

                                                soundPool1.play(soundId, 1, 1, 0, 0, 1);
                                                try {
                                                    Log.d("VoicePlay", "开始播放--- 休眠时长" + delaytime + "  "+Thread.currentThread());
                                                    //获取当前音频的时长
                                                    Thread.sleep(delaytime);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            soundPool1.release();
                                            mCountDownLatch.countDown();
                                        }
                                    }else{
                                        mCountDownLatch.countDown();
                                    }
                                }
                            });

                        });
                        return true;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                mCountDownLatch.countDown();
            } finally {

            }
            try {
                mCountDownLatch.await();
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
