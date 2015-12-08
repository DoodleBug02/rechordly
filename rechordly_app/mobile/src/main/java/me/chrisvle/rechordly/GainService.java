package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class GainService extends Service {

    private BroadcastReceiver broadcastReceiver;

    final String GAIN = "/gain";

    public GainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("GainService", "Started");
        IntentFilter filter = new IntentFilter();
        filter.addAction("/gain");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GAIN)) {
                    String path = intent.getStringExtra("filePath");
                    Double level = intent.getDoubleExtra("volume", 0);
                    File file = new File(path);

                    Log.d("GainService PATH", path);
                    Log.d("GainService VOLUME", level.toString());
                    wavIO w = new wavIO();
                    w.read(file);
                    byte[] b = w.myData;
                    b = Gain.adjustVolume(b, level);
                    w.myData = b;
                    File myGain = new File(path);
                    w.save(myGain);

                    Log.d("GainService", "Gain Effect Started");
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
