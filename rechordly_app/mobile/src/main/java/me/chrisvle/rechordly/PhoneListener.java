package me.chrisvle.rechordly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PhoneListener extends WearableListenerService {

    private static final String new_recording = "/new_recording";
    public File file;
    public GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        Log.d("OK", "OK");
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {

                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
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

            file = new File(this.getFilesDir(), "file2.wav");
            try {
                file.createNewFile();
                Log.d("PhoneListener", "Trying to receive file");

                channel.receiveFile(mApiClient, Uri.fromFile(file), false);
                Log.d("PhoneListener", "DONE");
            } catch (IOException e) {
                //handle error
            } finally {
                channel.close(mApiClient);
                Log.d("PhoneListener", "Channel Closed");
            }

        }

    }

    @Override
    public void onInputClosed(Channel channel, int int0, int int1) {
        Log.d("PhoneListener", "File Received!!");
        Log.d("PhoneListener", "Channel Closed!");
        Log.d("PATH", file.getAbsolutePath());
        Intent play = new Intent(this, InfoActivity.class);
        play.putExtra("path", file.getAbsolutePath());
        play.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(play);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }


}
