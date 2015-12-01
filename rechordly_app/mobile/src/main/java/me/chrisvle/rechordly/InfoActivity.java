package me.chrisvle.rechordly;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class InfoActivity extends AppCompatActivity {

    private static MediaPlayer mp;
    private static int pos = 0;

    ToggleButton t;

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

        t = (ToggleButton) findViewById(R.id.playback);
        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is play
                    play();
                } else {
                    // The toggle is pause
                    pause();
                }
            }
        });


    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }
//
//    private Uri load(File file) {
//        Uri uri = Uri.fromFile(file);
//        return uri;
//    }

}
