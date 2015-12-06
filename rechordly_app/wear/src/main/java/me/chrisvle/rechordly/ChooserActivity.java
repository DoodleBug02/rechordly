package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChooserActivity extends Activity {


    private TextView amount;
    private VolumeSliderView slider;
    private RelativeLayout rel_circular;
    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_chooser);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                amount = (TextView) findViewById(R.id.Volume);
                doneButton = (Button) findViewById(R.id.volume_done);
                slider = (VolumeSliderView) findViewById(R.id.circular);
                rel_circular = (RelativeLayout) findViewById(R.id.rel_circular);

                String boldfontPath = "fonts/Mission_Gothic_Bold.otf";
                Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
                amount.setTypeface(tf);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendVolume(v);
                    }
                });
            }
        });
    }

    public void sendVolume(View view) {
        int volume = slider.getVolume();
        Log.d("Done", "Clicked: volume is " + volume);

        //FIXME @Jeremy code here

        Intent intent = new Intent(getBaseContext(), SliderNavActivity.class);
        intent.putExtra("start", 2);
        intent.putExtra("start2", 3);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);

    }
}
