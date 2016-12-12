package com.acsia.server.thrift;


import com.acsia.server.support.ServerApplication;
import com.acsia.server.thrift.AudioService;

import org.apache.thrift.TException;

/**
 * Created by Acsia on 12/9/2016.
 */

public class AudioHandler implements AudioService.Iface {

    @Override
    public int getMaxAudioLevel() throws TException {
        return ServerApplication.getMaxAudioLevel();
    }

    @Override
    public int getAudioLevel() throws TException {
        return ServerApplication.getAudioLevel();
    }

    @Override
    public boolean setAudioLevel(int volumeLevel, boolean headUnit) throws TException {
        return ServerApplication.setAudioLevel(volumeLevel, headUnit);
    }

    @Override
    public boolean isMute() throws TException {
        return ServerApplication.isMute();
    }

    @Override
    public boolean muteAudio(boolean mute) throws TException {
        return ServerApplication.muteAudio(mute);
    }


}
