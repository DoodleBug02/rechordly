package me.chrisvle.rechordly;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.musicg.wave.Wave;
import com.musicg.wave.WaveFileManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javazoom.jl.converter.WaveFile;

public class CropActivity extends AppCompatActivity {

    private static MediaPlayer mp;

    ToggleButton t;
    EditText left;
    EditText right;

    int leftValue;
    int rightValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        ImageView iv = (ImageView)findViewById(R.id.info);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        left = (EditText) findViewById(R.id.left);

        right = (EditText) findViewById(R.id.left);


        Button b = (Button) findViewById(R.id.crop);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                leftValue = Integer.parseInt(left.getText().toString());
//                rightValue = Integer.parseInt(right.getText().toString());
//                String uri = "android.resource://" + getPackageName() + "/"+ R.raw.completed;
//                Wave w = load(uri);
//
//                trim(w, leftValue, rightValue);
//                save(uri, w);

//                Intent play = new Intent("Playback");
//                play.putExtra("Command", "play");
//                sendBroadcast(play);
                Uri p = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/myfile.wav");
                mp = new MediaPlayer();
                mp = MediaPlayer.create(getBaseContext(), p);
                play();
            }
        });

        String path = "android.resource://" + getPackageName() + "/raw/orchestra";
        Wave w = load(path);
        w = trim(w, 5, 5);
        save(Environment.getExternalStorageDirectory().getPath()+"/myfile.wav", w);



//        Intent i = getIntent();
//        String path = i.getStringExtra("Path");

    }

    private static Wave trim(Wave wave, int left, int right) {
        wave.leftTrim(left);
        wave.rightTrim(right);
        return wave;
    }

    private Wave load(String path) {
        InputStream audio = getResources().openRawResource(R.raw.orchestra);
        return new Wave(audio);
    }

    private static void save(String path, Wave wave) {
        WaveFileManager wfm = new WaveFileManager(wave);
        wfm.saveWaveAsFile(path);
    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }

    public void TrimToSelection(double startTime, double endTime){
        InputStream wavStream = null; // InputStream to stream the wav to trim
        File trimmedSample = null;  // File to contain the trimmed down sample
        File sampleFile = new File(samplePath); // File pointer to the current wav sample

        // If the sample file exists, try to trim it
        if (sampleFile.isFile()){
            trimmedSample = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "trimmedSample.wav");
            if (trimmedSample.isFile()) trimmedSample.delete();

            // Trim the sample down and write it to file
            try {
                wavStream = new BufferedInputStream(new FileInputStream(sampleFile));
                // Javazoom WaveFile class is used to write the wav
                WaveFile waveFile = new WaveFile();
                waveFile.OpenForWrite(trimmedSample.getAbsolutePath(), (int)audioFormat.getSampleRate(), (short)audioFormat.getSampleSizeInBits(), (short)audioFormat.getChannels());
                // The number of bytes of wav data to trim off the beginning
                long startOffset = (long)(startTime * audioFormat.getSampleRate()) * audioFormat.getSampleSizeInBits() / 4;
                // The number of bytes to copy
                long length = ((long)(endTime * audioFormat.getSampleRate()) * audioFormat.getSampleSizeInBits() / 4) - startOffset;
                wavStream.skip(44); // Skip the header
                wavStream.skip(startOffset);
                byte[] buffer = new byte[1024];
                int i = 0;
                while (i < length){
                    if (length - i >= buffer.length) {
                        wavStream.read(buffer);
                    }
                    else { // Write the remaining number of bytes
                        buffer = new byte[(int)length - i];
                        wavStream.read(buffer);
                    }
                    short[] shorts = new short[buffer.length / 2];
                    ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                    waveFile.WriteData(shorts, shorts.length);
                    i += buffer.length;
                }
                waveFile.Close(); // Complete writing the wave file
                wavStream.close(); // Close the input stream
            } catch (IOException e) {e.printStackTrace();}
            finally {
                try {if (wavStream != null) wavStream.close();} catch (IOException e){}
            }
        }

}
