package com.acsia.server.support;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.acsia.server.aidl.AudioService;
import com.acsia.server.thrift.ThriftService;
import com.acsia.server.ui.MainActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Acsia on 12/9/2016.
 */

public class ServerApplication extends Application {
    private static final String TAG = ServerApplication.class.getSimpleName();
    static ServerApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static ServerApplication getInstance() {
        return instance;
    }

    private void startClientApplication() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.acsia.client", "com.acsia.client.ui.SplashActivity"));
        if (getPackageManager().resolveActivity(intent, 0) != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            Toast.makeText(this, "HMI not installed", Toast.LENGTH_SHORT).show();
        }
    }

    public void startServer() {
        startThriftService();
        startAidlService();
        startClientApplication();
    }

    private void startAidlService() {
        Intent serviceIntent = new Intent(this, AudioService.class);
        startService(serviceIntent);
    }

    private void startThriftService() {
        Intent serviceIntent = new Intent(this, ThriftService.class);
        startService(serviceIntent);
    }

    public static int getMaxAudioLevel() {
        int maxVolume = -1;
        try {
            AudioManager audioManager = (AudioManager) getInstance().getSystemService(Context.AUDIO_SERVICE);
            maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getMaxAudioLevel::" + maxVolume);
        return maxVolume;
    }

    public static int getAudioLevel() {
        int volume = -1;
        try {
            AudioManager audioManager = (AudioManager) getInstance().getSystemService(Context.AUDIO_SERVICE);
            volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getAudioLevel::" + volume);
        return volume;
    }

    public static boolean setAudioLevel(int volumeLevel, boolean headUnit) {
        boolean success = false;
        try {
            AudioManager audioManager = (AudioManager) getInstance().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeLevel, 0);
            if (headUnit) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
            }

            success = true && playSound();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "setAudioLevel::" + success);
        return success;
    }

    static MediaPlayer mMediaPlayer;

    private static boolean playSound() {
        boolean success = false;
        try {
            if (mMediaPlayer == null) {
                mMediaPlayer = getMediaPlayer();// new MediaPlayer();//
            }
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                AssetFileDescriptor afd = getInstance().getAssets().openFd("test.mp3");
                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                afd.close();
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
            success = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    public static boolean isMute() {
        boolean mute = false;
        try {
            AudioManager audioManager = (AudioManager) getInstance().getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (currentVolume == 0) {
                mute = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mute;
    }


    public static boolean muteAudio(boolean mute) {
        boolean success = false;
        try {
            AudioManager audioManager = (AudioManager) getInstance().getSystemService(Context.AUDIO_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            } else {
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC, mute);
            }
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    static MediaPlayer getMediaPlayer() {

        MediaPlayer mediaplayer = new MediaPlayer();

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }

        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");

            Constructor constructor = cSubtitleController.getConstructor(new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});

            Object subtitleInstance = constructor.newInstance(getInstance(), null, null);

            Field f = cSubtitleController.getDeclaredField("mHandler");

            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }

            Method setSubtitleAnchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);

            setSubtitleAnchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {
        }

        return mediaplayer;
    }


}
