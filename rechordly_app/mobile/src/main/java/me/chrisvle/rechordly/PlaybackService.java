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

public class PlayBackService extends Service {

    String path = "";
    private BroadcastReceiver playbackReceiver;
    MediaPlayer player;
    final String PLAY = "/play";
    final String PAUSE = "/pause";
    final String NEW_PLAYBACK_FILE = "/playback_file";
    public PlayBackService() {
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
        player  = new MediaPlayer();
        filter.addAction("/pause");
        playbackReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(PLAY)) {
                    String newPath = intent.getStringExtra("path");
                    if (newPath.equalsIgnoreCase(path)) {
                        if (!player.isPlaying()) {
                            player.start();
                            Log.d("PlaybackService", "Playback Started");
                        }
                    } else {
                        path = newPath;
                        player.stop();
                        player.reset();
                        try {
                            player.setDataSource(newPath);
                            player.prepare();
                        } catch (IOException e) {
                            Log.d("PlaybackService", "FILE COULD NOT BE FOUND TO PLAY");
                        }
                        player.start();

                    }
                } else if (intent.getAction().equals(PAUSE)) {
                    Log.d("PlaybackService", "Pause Requested");
                    player.pause();
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