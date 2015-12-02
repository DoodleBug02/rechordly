package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class PlaybackActivity extends AppCompatActivity {

    private static MediaPlayer mp;
    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

//    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getStringExtra("Command");
                if (command.equals("play'")) {
                    play();
                }
                else {
                    pause();
                }
            }
        };
//
        filter = new IntentFilter();
        filter.addAction("Playback");
        registerReceiver(mReceiver, filter);

<<<<<<< HEAD
//        Intent play = getIntent();
//        filepath = play.getStringExtra("Playback");
//        File f = new File(filepath);

        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.completed2);
=======
        Intent play = getIntent();
        filepath = play.getStringExtra("Path");
        Log.d("PATH", filepath);
        File f = new File(filepath);
        Log.d("fileLen", String.valueOf(f.length()));

        mp = new MediaPlayer();
        //mp = MediaPlayer.create(this, Uri.fromFile(f));
>>>>>>> c766e309623b1c92fa0fc3e9e539cfe4547831f5
    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

}
