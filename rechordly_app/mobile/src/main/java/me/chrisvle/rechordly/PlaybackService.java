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

public class PlaybackService extends Service {

    String path;
    private BroadcastReceiver playbackReceiver;

    public PlaybackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("PlaybackService", "Started");
        IntentFilter filter = new IntentFilter();
        filter.addAction("/playback_file");
        filter.addAction("/play");
        filter.addAction("/pause");
        playbackReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("/playback_file")) {
                    Log.d("PlaybackService", "New File Received");
                    path = intent.getStringExtra("path");
                } else if (intent.getAction().equals("/play")) {
                    Log.d("PlaybackService", "Play Requested");

                } else if (intent.getAction().equals("/pause")) {
                    Log.d("PlaybackService", "Pause Requested");
                }

            }
        };
        registerReceiver(playbackReceiver, filter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(playbackReceiver);
    }
}
