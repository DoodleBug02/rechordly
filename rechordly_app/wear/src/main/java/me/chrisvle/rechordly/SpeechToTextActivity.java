package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextActivity extends Activity {

    private TextView mTextView;
    private String text;
    private String from;
    private String totalTime;
    private boolean oldEdit;
    private boolean oldLyric;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            }
        });
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        totalTime = getIntent().getStringExtra("time");
        oldEdit = getIntent().getBooleanExtra("oldEdit", false);
        oldLyric = getIntent().getBooleanExtra("oldLyric", false);
        startSpeech();
    }

    public void startSpeech() {
        Log.d("LyricFragment", "Start Speech");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (from.equalsIgnoreCase("/lyrics")) {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "Dictate Lyrics");
        } else {
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                    "\n \n \n Name Recording");
        }
        try {
            super.startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    "Dictation Not Supported.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Receiving speech input
     * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("SpeechToText", "Result reached");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100:
                if (null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text = result.get(0);

                } else{
                    text = "None";
                }
                break;
            default:
                text = "None";
            }

        if (from.equalsIgnoreCase("/lyrics")) {
            Intent intent = new Intent("/lyrics");
            if (text != null) {
                intent.putExtra("lyrics", text);
            } else {
                intent.putExtra("lyrics", "");
            }
            sendBroadcast(intent);
            Log.d("SpeechToText", "Lyrics Sent");
            Intent intent2;
            if (oldEdit) {
                intent2 = new Intent(getBaseContext(), OldEditActivity.class);
                intent2.putExtra("start", 1);
                intent2.putExtra("time", totalTime);
                intent2.putExtra("lyrics", true);
            } else {
                intent2 = new Intent(getBaseContext(), SliderNavActivity.class);
                intent2.putExtra("start", 2);
                intent2.putExtra("start2", 1);
                intent2.putExtra("time", totalTime);
                intent.putExtra("lyrics", true);
            }
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
        } else if (from.equalsIgnoreCase("/name")) {
            Intent intent = new Intent("/name");
            if (text != null) {
                intent.putExtra("name", text);
            } else {
                intent.putExtra("name", "");
            }
            intent.putExtra("name", text);
            sendBroadcast(intent);
            Log.d("SpeechToText", "Name Sent");
            Intent intent2 = new Intent(this, SaveActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent2);
        }
    }
}
