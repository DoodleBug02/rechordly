package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.wearable.view.WatchViewStub;

public class EditNavActivity extends Activity {

    private ViewPager viewPager;
    private Context t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nav);
        t = this;
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(new CustomPagerAdapter(t));
            }
        });
    }
}
