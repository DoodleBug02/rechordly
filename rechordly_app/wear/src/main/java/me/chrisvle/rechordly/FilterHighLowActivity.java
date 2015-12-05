package me.chrisvle.rechordly;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;

public class FilterHighLowActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_high_low);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);

    }
}
