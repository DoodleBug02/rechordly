package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class EditActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

    String filepath;
    Wave myAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                filepath = extras.getString("File");
            }
        };

        filter = new IntentFilter();
        filter.addAction("Edit");
        registerReceiver(mReceiver, filter);

        myAudio = load(filepath);
    }

    private static void trim(Wave wave, int left, int right) {
        wave.leftTrim(left);
        wave.rightTrim(right);
    }

    private static void save(String path, Wave wave) {
        File f = new File(path);
        f.delete();
        WaveFileManager waveFileManager = new WaveFileManager(wave);
        waveFileManager.saveWaveAsFile(path);
    }

    private static Wave load(String path) {
        File f_path = new File(path);
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(f_path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new Wave(input);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

    }

}
