package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class SaveActivity extends Activity {

    private TextView save_txt2;
    private Context t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        t = this;
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                save_txt2 = (TextView) stub.findViewById(R.id.saving_txt2);

                String boldfontPath = "fonts/Mission_Gothic_Bold.otf";
                Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
                save_txt2.setTypeface(tf);

                new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        Intent saveIntent = new Intent("/save");
                        sendBroadcast(saveIntent);
                        Intent intent = new Intent(t, Main2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);

                    }
                }.start();
            }
        });


    }
}
