package me.chrisvle.rechordly;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {

    ToggleButton t;
    private static MediaPlayer mp;
    private TextView name;
    private Toolbar toolbar;
    private long whenTimeStopped;
    private SavedDataList save_data = SavedDataList.getInstance();
    private String fName;

    private String text;

    private String lyrics;


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

        Button b = (Button) findViewById(R.id.edit);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent("/edit");
                edit.putExtra("filePath", "Put real file here");
                sendBroadcast(edit);
            }
        });

        Intent intent = getIntent();
        fName = intent.getStringExtra("file_name");
        String shownName = intent.getStringExtra("shown_name");
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Mission_Gothic_Bold.ttf");

        Button b2 = (Button) findViewById(R.id.lyric);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyrics.equals("")) {
                    startSpeech();
                } else {
                    Intent lyric_screen = new Intent(getBaseContext(), LyricActivity.class);
                    lyric_screen.putExtra("text", text);
                    startActivity(lyric_screen);
                }
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.action_bar_custom);
        toolbar.setBackground(getDrawable(R.drawable.blue_gradient));

        name = (TextView) toolbar.findViewById(R.id.mytext);
        name.setText(shownName);
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

    public void startSpeech() {
        Log.d("LyricFragment", "Start Speech");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Dictate Lyrics");
        try {
            super.startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    "Dictation Not Supported.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SpeechToText", "Result reached");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text = result.get(0);

                }
                break;
            }

        }


    }
}


