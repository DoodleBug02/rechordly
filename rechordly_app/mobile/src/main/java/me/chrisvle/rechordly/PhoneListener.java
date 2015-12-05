package me.chrisvle.rechordly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PhoneListener extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, ChannelApi.ChannelListener {

    private static final String PLAY = "/play";
    private static final String PAUSE = "/pause";
    public File file;
    public GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        Log.d("PhoneListener", "OK");
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();

        mApiClient.connect();


    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(PLAY)) {
            Log.d("PhoneListener", "Play Request");

            Intent intent = new Intent("/play");
            if (file.exists()) {
                intent.putExtra("path", file.getAbsolutePath());
                sendBroadcast(intent);
            }

        } else if (messageEvent.getPath().equalsIgnoreCase(PAUSE)){
            Log.d("PhoneListener", "Pause Request");
            Intent intent = new Intent("/pause");
            if (file.exists()) {
                intent.putExtra("path", file.getAbsolutePath());
                sendBroadcast(intent);
            }

        }
    }

    @Override
    public void onChannelOpened(Channel channel) {
        Log.d("PhoneListener", "Channel established");
        if (channel.getPath().equals("/new_recording")) {

            file = new File(Environment.getExternalStorageDirectory().getPath(), "currentFile.wav");
            Log.d("this", String.valueOf(this.getFilesDir()));
            try {
                file.createNewFile();
            } catch (IOException e) {
                //handle error
            }
            Log.d("PhoneListener", "Trying to receive file");

            channel.receiveFile(mApiClient, Uri.fromFile(file), false);
        }

    }

    @Override
    public void onInputClosed(Channel channel, int int0, int int1) {
        Log.d("PhoneListener", "File Received!!");

    }

    @Override
    public void onChannelClosed(Channel channel, int i0, int i1) {
        Log.d("PhoneListener", "Channel Closed!");
    }



    @Override
    public void onConnected(final Bundle connectionHint) {
        Wearable.ChannelApi.addListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i0) {
        Wearable.ChannelApi.removeListener(mApiClient, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }


}
