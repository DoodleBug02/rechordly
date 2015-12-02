package me.chrisvle.rechordly;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;

public class PlaybackActivity extends AppCompatActivity {

    private static MediaPlayer mp;
//    private BroadcastReceiver mReceiver;
//    private IntentFilter filter;

    String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



//        mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();
//                filepath = extras.getString("File");
//            }
//        };
//
//        filter = new IntentFilter();
//        filter.addAction("Playback");
//        registerReceiver(mReceiver, filter);

        Intent play = getIntent();
        filepath = play.getStringExtra("Path");
        Log.d("PATH", filepath);
        File f = new File(filepath);
        Log.d("fileLen", String.valueOf(f.length()));

        mp = new MediaPlayer();
        //mp = MediaPlayer.create(this, Uri.fromFile(f));
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
//        unregisterReceiver(mReceiver);
    }

}
