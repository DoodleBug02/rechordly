package me.chrisvle.rechordly;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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

    private String lyrics;
    private String lyrics_bool;

    private String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        Intent intent = getIntent();
        fName = intent.getStringExtra("file_name");
        final String shownName = intent.getStringExtra("shown_name");
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Mission_Gothic_Bold.ttf");

        duration = save_data.getDuration(fName);
        lyrics = save_data.getLyrics(fName);
        if (lyrics.equals("None")) {
            lyrics_bool = "false";
        }
        else {
            lyrics_bool = "true";
        }


        ImageButton b = (ImageButton) findViewById(R.id.edit);
        b.setImageResource(R.drawable.edit_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ON CLICK", "EDIT");
                Log.d("FILEPATH", fName);
                Intent edit = new Intent("/edit");
                edit.putExtra("filePath", fName);
                sendBroadcast(edit);

                Intent openMain = new Intent(v.getContext(), MessageSender.class);
                openMain.putExtra("START", "edit");
                openMain.putExtra("Duration", duration);
                openMain.putExtra("Lyrics", lyrics_bool);
                openMain.putExtra("path", shownName);
                startService(openMain);
            }
        });



        ImageButton b2 = (ImageButton) findViewById(R.id.lyric);

        if (lyrics_bool.equals("false")) {
            b2.setImageResource(R.drawable.add_lyrics);
        }
        else {
            b2.setImageResource(R.drawable.lyrics_button);
        }


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lyrics.equals("None")) {
                    Intent openMain = new Intent(v.getContext(), MessageSender.class);
                    openMain.putExtra("START", "lyrics");
                    openMain.putExtra("Duration", duration);
                    openMain.putExtra("Lyrics", lyrics_bool);
                    startService(openMain);
                } else {
                    Intent lyric_screen = new Intent(getBaseContext(), LyricActivity.class);
                    lyric_screen.putExtra("text", lyrics);
                    startActivity(lyric_screen);
                }
            }
        });


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        dur.setText(duration);
        dur.setTypeface(tf);

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
    }
}


