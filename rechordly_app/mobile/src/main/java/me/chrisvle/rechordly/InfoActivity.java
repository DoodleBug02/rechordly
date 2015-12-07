package me.chrisvle.rechordly;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import java.io.File;

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

        Button b2 = (Button) findViewById(R.id.lyric);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lyric = new Intent(getBaseContext(), LyricActivity.class);
                startActivity(lyric);

                Intent lyric_add = new Intent("/lyric");
                lyric_add.putExtra("filePath", "Put real file here");
                sendBroadcast(lyric_add);
            }
        });


        t = (ToggleButton) findViewById(R.id.playback);
        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent play = new Intent("/play");
                    File f = new File("Put real file here");
                    sendBroadcast(play);
                } else {
                    Intent pause = new Intent("/pause");
                    sendBroadcast(pause);
                }
            }
        });


    }


}


