package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class PlaybackService extends Service {

    private static MediaPlayer mp;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

    public PlaybackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String command = intent.getStringExtra("Command");
//                if (command.equals("play'")) {
//                    play();
//                }
//                else {
//                    pause();
//                }
//            }
//        };

        String command = intent.getStringExtra("Command");

        Log.d("THE COMMAND: ", command);

        filter = new IntentFilter();
        filter.addAction("Playback");
        registerReceiver(mReceiver, filter);

//        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.orchestra);

        if (command.equals("play")) {
            play();
        }
        else {
            pause();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }
}
