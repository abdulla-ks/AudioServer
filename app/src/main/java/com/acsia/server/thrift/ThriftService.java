package com.acsia.server.thrift;

/**
 * Created by Acsia on 12/2/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class ThriftService extends Service {
    private static final String TAG = ThriftService.class.getSimpleName();
    public static AudioHandler handler;

    public static AudioService.Processor processor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    interface ad {
        /**
         * FunctionName: setBrightness
         * Description: Sets the brightness level to specified level
         *
         * @param brightnessLevel (brightness level)
         * @return boolean value representing the success or failure
         */
        boolean setBrightness(int brightnessLevel);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // here you can add whatever you want this service to do
        try {
            Log.i(TAG, "Started service @ " + SystemClock.elapsedRealtime());
            handler = new AudioHandler();
            processor = new AudioService.Processor(handler);

            Runnable simple = new Runnable() {
                public void run() {
                    try {


                        TServerTransport serverTransport = new TServerSocket(9090);
                        TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

                        System.out.println("Starting the simple server...");
                        server.serve();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            new Thread(simple).start();
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

}