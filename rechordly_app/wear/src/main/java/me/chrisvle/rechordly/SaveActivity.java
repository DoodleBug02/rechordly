package me.chrisvle.rechordly;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.TextView;

public class SaveActivity extends Activity {

    private TextView save_txt;
    private TextView save_txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                save_txt = (TextView) stub.findViewById(R.id.saving_txt);
                save_txt2 = (TextView) stub.findViewById(R.id.saving_txt2);

                String boldfontPath = "fonts/Mission_Gothic_Bold.otf";
                Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
                save_txt.setTypeface(tf);
                save_txt2.setTypeface(tf);
            }
        });
    }
}
