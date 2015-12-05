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

import java.io.InputStream;

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

}
