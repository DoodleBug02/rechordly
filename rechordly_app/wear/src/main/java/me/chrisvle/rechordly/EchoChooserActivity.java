package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EchoChooserActivity extends Activity {


    private TextView amount;
    private EchoSliderView slider;
    private RelativeLayout rel_circular;
    private Button doneButton;
    private String from;
    private String totalTime;
    private boolean oldEdit;
    private boolean oldLyric;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_echo_chooser);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
                amount = (TextView) findViewById(R.id.echo);
                doneButton = (Button) findViewById(R.id.echo_done);
                slider = (EchoSliderView) findViewById(R.id.circular);
                rel_circular = (RelativeLayout) findViewById(R.id.rel_circular);

                String boldfontPath = "fonts/Mission_Gothic_Bold.otf";
                Typeface tf = Typeface.createFromAsset(getAssets(), boldfontPath);
                amount.setTypeface(tf);

                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEcho(v);
                    }
                });
        totalTime = getIntent().getStringExtra("time");
        oldEdit = getIntent().getBooleanExtra("oldEdit", false);
        oldLyric = getIntent().getBooleanExtra("oldLyric", false);


    }

    public void sendEcho(View view) {
        int echo = slider.getEcho();
        Intent intent2;
        if (oldEdit) {
            intent2 = new Intent(getBaseContext(), OldEditActivity.class);
            intent2.putExtra("start", 4);
            intent2.putExtra("time", totalTime);
            intent2.putExtra("lyrics", oldLyric);
        } else {
            intent2 = new Intent(getBaseContext(), SliderNavActivity.class);
            intent2.putExtra("start", 2);
            intent2.putExtra("start2", 4);
            intent2.putExtra("time", totalTime);
        }
        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent2);

        Intent intent = new Intent(from);
        intent.putExtra("amount",Integer.toString(echo));
        sendBroadcast(intent);


    }
}
