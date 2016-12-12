package com.acsia.server.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.acsia.server.IAudioService;
import com.acsia.server.support.ServerApplication;

/**
 * Created by Acsia on 12/9/2016.
 */

public class AudioHandler extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Return the interface
        return mBinder;
    }

    private final IAudioService.Stub mBinder = new IAudioService.Stub() {

        @Override
        public int getMaxAudioLevel() throws RemoteException {
            return ServerApplication.getMaxAudioLevel();
        }

        @Override
        public int getAudioLevel() throws RemoteException {
            return ServerApplication.getAudioLevel();
        }

        @Override
        public boolean setAudioLevel(int volumeLevel, boolean headUnit) throws RemoteException {
            return ServerApplication.setAudioLevel(volumeLevel, headUnit);
        }

        @Override
        public boolean isMute() throws RemoteException {
            return ServerApplication.isMute();
        }

        @Override
        public boolean muteAudio(boolean mute) throws RemoteException {
            return ServerApplication.muteAudio(mute);
        }
    };
}
