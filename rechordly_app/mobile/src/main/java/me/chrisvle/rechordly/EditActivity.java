package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.musicg.wave.Wave;


public class EditActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

    String filepath;
    Wave myAudio;

//    private Equalizer eq;
//    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//        mp = new MediaPlayer();
//        mp = MediaPlayer.create(this, R.raw.completed);
//
//        eq = new Equalizer(0, mp.getAudioSessionId());
//        eq.setEnabled(true);

//        setupEq();






//        mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();
//                filepath = extras.getString("File");
//            }
//        };
//
//        filter = new IntentFilter();
//        filter.addAction("Edit");
//        registerReceiver(mReceiver, filter);
//
//        myAudio = load(filepath);
    }

//    private void setupEq() {
//        short numOfBands = eq.getNumberOfBands();
//        final short min = eq.getBandLevelRange()[0];
////        final short max = eq.getBandLevelRange()[1];
//        for (short i = 0; i < numOfBands; i++) {
//            eq.setBandLevel(i, (short) (5 + min));
//        }
//    }


//

//


//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mReceiver);
//
//    }



}
