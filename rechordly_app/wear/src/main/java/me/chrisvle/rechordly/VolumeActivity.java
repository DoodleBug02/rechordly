package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class VolumeActivity extends Activity {

    private ImageButton mImageButton;
    private GestureDetector mDetector;
    private GestureDetector tapDetector;

    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume);

        Intent i = getIntent();
        time = i.getStringExtra("time");

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mImageButton = (ImageButton) stub.findViewById(R.id.volume_btn);
                mImageButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        if (tapDetector.onTouchEvent(e)) {
                            // single tap
                            return true;
                        } else {
                            return mDetector.onTouchEvent(e);
                        }
                    }
                });

            }
        });

        //Configure single tap detector
        tapDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                return true;
            }
        });

        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                if (distanceX > 5.0) {
                    Log.d("Event: ", "onScrollEvent Fired!");
                    Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                    intent.putExtra("time", time);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
                if (distanceX < -5.0) {
                    Log.d("Event: ", "onScrollEvent Fired!");
                    Intent intent = new Intent(getBaseContext(), CropActivity.class);
                    intent.putExtra("time", time);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, 0);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), VolumeChooserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
