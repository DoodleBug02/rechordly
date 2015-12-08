package me.chrisvle.rechordly;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class LyricActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyric);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        Typeface tf = Typeface.createFromAsset(getAssets(), "font/Mission_Gothic_Bold.ttf");

        Intent add_lyrics = getIntent();
        String text = add_lyrics.getStringExtra("text");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackground(getDrawable(R.drawable.blue_gradient));

        name = (TextView) toolbar.findViewById(R.id.mytext);
        name.setText("Lyrics");
        name.setTypeface(tf);

        TextView t = (TextView) findViewById(R.id.myLyrics);
        t.setText(text);
        t.setTypeface(tf);
    }

}

