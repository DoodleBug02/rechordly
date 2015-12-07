package me.chrisvle.rechordly;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GainChooserActivity extends Activity {

    private TextView amount;
    private GainSliderView slider;
    private RelativeLayout rel_circular;
    private Button doneButton;
    private String from;
    private String totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gain_chooser);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        amount = (TextView) findViewById(R.id.gain);
        doneButton = (Button) findViewById(R.id.gain_done);
        slider = (GainSliderView) findViewById(R.id.circular_gain);
        rel_circular = (RelativeLayout) findViewById(R.id.rel_circular_gain);


        String boldfontPath = "fonts/Mission_Gothic_Bold.otf";
        Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
        amount.setTypeface(tf);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGain(v);
            }
        });

        totalTime = getIntent().getStringExtra("time");


    }

    public void sendGain(View view) {
        int gain = slider.getGain();
        Log.d("Done", "Clicked: gain is " + gain);

        Intent intent = new Intent(from);
        intent.putExtra("amount", Integer.toString(gain));
        sendBroadcast(intent);
        Intent intent2 = new Intent(getBaseContext(), SliderNavActivity.class);
        intent2.putExtra("start", 2);
        intent2.putExtra("start2", 3);
        intent2.putExtra("time", totalTime);
        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent2);
    }

}
