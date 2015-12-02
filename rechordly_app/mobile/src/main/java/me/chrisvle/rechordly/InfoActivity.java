package me.chrisvle.rechordly;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class InfoActivity extends AppCompatActivity {

    ToggleButton t;
    private static MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        ImageView iv = (ImageView)findViewById(R.id.info);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lyrics = new Intent(getBaseContext(), TranscribingActivity.class);
                startActivity(lyrics);

            }
        });
        mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.completed);
        Button b = (Button) findViewById(R.id.playback);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });


    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }


}


