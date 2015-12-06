package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;



public class PlaybackService extends Service {

    String path = "";
    private BroadcastReceiver playbackReceiver;
    MediaPlayer player;
    final String PLAY = "/play";
    final String PAUSE = "/pause";
    final String NEW_PLAYBACK_FILE = "/playback_file";
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
                if (intent.getAction().equals(PLAY)) {
                    Log.d("PlaybackService", "Attemping play");
                    String newPath = intent.getStringExtra("path");
                    player  = new MediaPlayer();

                    path = newPath;
                    player.stop();
                    player.reset();
                    try {
                        Log.d("PlaybackService", newPath);
                        player.setDataSource(newPath);
                        player.prepare();
                    } catch (IOException e) {
                        Log.d("PlaybackService", "FILE COULD NOT BE FOUND TO PLAY");
                    }
                    player.start();
                    Log.d("PlaybackService", String.valueOf(player.getCurrentPosition()));
                } else if (intent.getAction().equals(PAUSE)) {
                    Log.d("PlaybackService", "Pause Requested");
                    player.stop();
                    Log.d("PlaybackService", "Playback Paused");

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
