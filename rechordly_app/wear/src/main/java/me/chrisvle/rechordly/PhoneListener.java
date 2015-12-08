package me.chrisvle.rechordly;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Goshujin on 11/30/15.
 */
public class PhoneListener extends WearableListenerService {
    private static final String START_EDIT = "/edit";
    private static final String START_MAIN = "/start_main";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if ( messageEvent.getPath().equalsIgnoreCase( START_EDIT )) {
            //TODO: Implement this so it takes in a filename and opens the crop activity
//            Intent intent = new Intent(getBaseContext(), cropChooserActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);

            //duration called time
            // boolean lyrics
            // either crop or lyrics called start
            String all = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("Message", all);
            String[] message = all.split("\\|");
            Intent intent = new Intent(getBaseContext(), OldEditActivity.class);
            int start;
            if (message[0].equals("lyrics")) {
                start = 1;
            } else {
                start = 2;
            }
            intent.putExtra("start", start);
            intent.putExtra("lyrics", message[1]);
            intent.putExtra("time", message[2]);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase( START_MAIN )) {
            Intent intent = new Intent(getBaseContext(), Main2Activity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}