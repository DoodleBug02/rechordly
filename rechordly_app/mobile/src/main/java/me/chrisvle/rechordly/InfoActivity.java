package me.chrisvle.rechordly;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.ToggleButton;

public class InfoActivity extends AppCompatActivity {

    ToggleButton t;
    private static MediaPlayer mp;
    private TextView name;
    private Toolbar toolbar;
    private Chronometer time;
    private long whenTimeStopped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

//        ImageView iv = (ImageView)findViewById(R.id.info);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        Intent intent = getIntent();
        String fName = intent.getStringExtra("file_name");
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Mission_Gothic_Bold.ttf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        toolbar.setBackground(getDrawable(R.drawable.blue_gradient));

        name = (TextView) toolbar.findViewById(R.id.mytext);
        name.setText(fName);
        name.setTypeface(tf);

        time = (Chronometer) findViewById(R.id.chronometer);
        time.setTypeface(tf);
        time.setText("Chrono");


//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent lyrics = new Intent(getBaseContext(), CropActivity.class);
//                startActivity(lyrics);
//
//            }
//        });
//        Intent intent = getIntent();
//        String path = intent.getStringExtra("path");
//        mp = new MediaPlayer();
//        Uri uri = Uri.parse(path);
        mp = MediaPlayer.create(this, R.raw.orchestra);
//        Button b = (Button) findViewById(R.id.playback);

//        t = (ToggleButton) findViewById(R.id.playback);
//        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    time.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
//                    time.start();
//                    play();
//                } else {
//                    timeWhenStopped = time.getBase() - SystemClock.elapsedRealtime();
//                    time.stop();
//                    pause();
//                }
//            }
//        });


    }

    private void play() {
        mp.start();
    }

    private void pause() {
        mp.pause();
    }



}


