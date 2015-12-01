package me.chrisvle.rechordly;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class PhoneListener extends WearableListenerService {

    private static final String new_recording = "/new_recording";
    public File file;
    public GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .build();

        mApiClient.connect();


    }

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

    @Override
    public void onChannelOpened(Channel channel) {
        Log.d("PhoneListener", "Channel established");
        if (channel.getPath().equals("/new_recording")) {

            file = new File(this.getFilesDir(), "file2.pcm");
            try {
                file.createNewFile();
            } catch (IOException e) {
                //handle error
            }
            Log.d("PhoneListener", "Trying to receive file");
            channel.receiveFile(mApiClient, Uri.fromFile(file), false);
            Log.d("PhoneListener", "DONE");
        }

    }

    @Override
    public void onInputClosed(Channel channel, int int0, int int1) {
        Log.d("PhoneListener", "File Received!!");
        channel.close(mApiClient);
        Log.d("PhoneListener", String.valueOf(file.length()));
        Log.d("PhoneListener", "Channel Closed!");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }
}
