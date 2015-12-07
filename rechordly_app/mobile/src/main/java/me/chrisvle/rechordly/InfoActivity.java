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
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;

public class InfoActivity extends AppCompatActivity {

    ToggleButton t;
    private static MediaPlayer mp;
    private TextView name;
    private Toolbar toolbar;
    private long whenTimeStopped;
    private SavedDataList save_data = SavedDataList.getInstance();
    private String fName;

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

//        iv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent lyrics = new Intent(getBaseContext(), CropActivity.class);
//                startActivity(lyrics);
//
//            }
//        });

//        Button b = (Button) findViewById(R.id.edit);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent edit = new Intent("/edit");
//                edit.putExtra("filePath", "Put real file here");
//                sendBroadcast(edit);
//            }
//        });
        Intent intent = getIntent();
        fName = intent.getStringExtra("file_name");
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Mission_Gothic_Bold.ttf");
//
//        Button b2 = (Button) findViewById(R.id.lyric);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent lyric = new Intent(getBaseContext(), LyricActivity.class);
//                startActivity(lyric);
//
//                Intent lyric_add = new Intent("/lyric");
//                lyric_add.putExtra("filePath", "Put real file here");
//                sendBroadcast(lyric_add);
//            }
//        });


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

        TextView echo = (TextView) findViewById(R.id.echo_val);
        echo.setText("Echo: " + save_data.getEcho(fName));
        echo.setTypeface(tf);

        TextView vol = (TextView) findViewById(R.id.vol_val);
        vol.setText("Gain: " + save_data.getGain(fName));
        vol.setTypeface(tf);

        TextView dur = (TextView) findViewById(R.id.duration);
        dur.setText(save_data.getDuration(fName));
        dur.setTypeface(tf);


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


        t = (ToggleButton) findViewById(R.id.playback);
        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent play = new Intent("/play");
                    File f = new File(save_data.getPath(fName));
                    play.putExtra("path", save_data.getPath(fName));
                    sendBroadcast(play);
                } else {
                    Intent pause = new Intent("/pause");
                    sendBroadcast(pause);
                }
            }
        });


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

//    private void play() {
//        mp.start();
//    }
//
//    private void pause() {
//        mp.pause();
//    }



}


