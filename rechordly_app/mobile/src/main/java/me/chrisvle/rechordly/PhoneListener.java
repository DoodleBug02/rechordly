package me.chrisvle.rechordly;

import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.nio.charset.StandardCharsets;

public class PhoneListener extends WearableListenerService {

    private static final String new_recording = "/new_recording";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(new_recording)) {
            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("PhoneListener", value);

        } else {
            Log.d("PhoneListener", "Case not matched");
            super.onMessageReceived(messageEvent);
        }
    }
}
