package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CropBackActivity extends Activity {

    private TextView time;
    private CropSliderViewBack slider;
    private RelativeLayout rel_circular;
    private ImageButton doneButton;
    private String time_s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_back);
        time_s = getIntent().getStringExtra("time");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                time = (TextView) findViewById(R.id.timeB);
                doneButton = (ImageButton) findViewById(R.id.crop_b_done);
                slider = (CropSliderViewBack) findViewById(R.id.crop_b_slider);
                rel_circular = (RelativeLayout) findViewById(R.id.rel_crop_b_circular);

                String boldfontPath = "fonts/Mission_Gothic_Regular.otf";
                Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
                time.setTypeface(tf);
                time.setText(slider.getTime());

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendCropF(v);
                        Intent intent = new Intent(v.getContext(), CropActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.putExtra("time", time_s);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    public void sendCropF(View view) {
        String t = slider.getTime();
        Log.d("Done", "Clicked: crop time is " + t);
         //Currently t is a string in MM:SS format
        //FIXME Jeremy's code to send cropTimeF to phone here
    }
}
