package me.chrisvle.rechordly;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;


public class EditActivity extends AppCompatActivity {

    private static String outFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outFolder = "out";
    }

    private static void trim(Wave wave) {
        wave.leftTrim(1);
        wave.rightTrim(0.5F);
    }

    private static void save(Wave wave) {
        WaveFileManager waveFileManager = new WaveFileManager(wave);
        waveFileManager.saveWaveAsFile(outFolder + "/out.wav");
    }

}
