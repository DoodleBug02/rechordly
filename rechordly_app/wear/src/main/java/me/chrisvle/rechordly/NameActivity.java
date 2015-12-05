package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class NameActivity extends Activity {

    private TextView mTextView;
    private ImageButton check;
    private ImageButton cross;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                check = (ImageButton) stub.findViewById(R.id.check_btn);
                cross = (ImageButton) stub.findViewById(R.id.cross_btn);
                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //FIXME call google voice @Jeremy
                    }
                });

                cross.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View v) {
                        Intent intent = new Intent(getBaseContext(), SaveActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}
