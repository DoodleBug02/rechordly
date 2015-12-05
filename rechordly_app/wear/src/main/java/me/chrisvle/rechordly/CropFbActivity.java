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
import android.widget.TextView;

public class CropFbActivity extends Activity {

    private TextView mTextView;
    private ImageButton mImageButton;
    private GestureDetector mDetector;
    private static final String DEBUG_TAG = "Gestures";

    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_fb);

        time = getIntent().getStringExtra("time");
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mImageButton = (ImageButton) stub.findViewById(R.id.front_btn);
                mImageButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (mDetector.onTouchEvent(event)) {
                            Log.d("Event: ", "onTouchEvent Fired!");
//                            finish();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                Log.d(DEBUG_TAG, "onScroll: Distance: " + String.valueOf(distanceX) + ", " + String.valueOf(distanceY));
                if (distanceX > 5.0) {
                    Log.d("Event: ", "onScrollEvent Fired!");
                    Intent intent = new Intent(getBaseContext(), VolumeChooserActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
                if (distanceX < -5.0) {
                    Log.d("Event: ", "onScrollEvent Fired!");
                    Intent intent = new Intent(getBaseContext(), WatchMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                }
                Log.d(DEBUG_TAG, " onScroll: " + e1.toString()+e2.toString());
                return false;
            }
        });
    }

    // Capture long presses / scrolls
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    public void front_click(View v) {
        Intent intent = new Intent(this, CropFrontActivity.class);
    //    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("time", time);
        startActivity(intent);
        //FIXME
    }

    public void back_click(View v) {
        //FIXME
        Intent intent = new Intent(this, CropBackActivity.class);
    //    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("time", time);
        startActivity(intent);
    }
}
