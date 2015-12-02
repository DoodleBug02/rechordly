package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageButton;

public class WatchMain extends Activity {

    private DismissOverlayView mDismissOverlay;
    private GestureDetector mDetector;
    private ImageButton music_btn;
    private ImageButton lyric_btn;

    private static final String DEBUG_TAG = "Gestures";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // Obtain the DismissOverlayView element
//                mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
//                mDismissOverlay.setIntroText(R.string.long_press_intro);
//                mDismissOverlay.showIntroIfNecessary();

                music_btn = (ImageButton) findViewById(R.id.music_btn);
            }
        });
        Intent i = new Intent(this, MessageService.class);
        startService(i);
    }

    public void recordLyrics(View v) {
        Log.d("Event: ", "recording lyrics");
        Intent intent = new Intent(getBaseContext(), RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void recordMusic(View v) {
        Log.d("Event: ", "recording music");
        Intent intent = new Intent(getBaseContext(), RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

}
