package me.chrisvle.rechordly;

import android.content.Intent;
import android.media.MediaPlayer;
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

import me.chrisvle.rechordly.dummy.DummyContent;

public class PhoneListener extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, ChannelApi.ChannelListener {

    private static final String PLAY = "/play";
    private static final String PAUSE = "/pause";
    private static final String SAVE = "/save";
    private static final String RETRY = "/retry";
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
                intent.putExtra("path", file.getAbsolutePath());
                sendBroadcast(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(PAUSE)) {
            Log.d("PhoneListener", "Pause Request");
            Intent intent = new Intent("/pause");
            intent.putExtra("path", file.getAbsolutePath());
            sendBroadcast(intent);
        } else if (messageEvent.getPath().equalsIgnoreCase(RETRY)){
            Log.d("PhoneListener", "Retry Request");
            Intent intent = new Intent("/retry");
            file.delete();
        } else if (messageEvent.getPath().equalsIgnoreCase(SAVE)){
            Log.d("PhoneListener", "Save Request");
            String all = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            String[] edits = all.split("|");

            // Handles all FILENAMING
            if (!edits[0].equals("None")) {
                //FIXME This wont work, also please replace edits[0] with final filename (There should not be None)
                file.delete();
                file = new File(Environment.getExternalStorageDirectory().getPath(), edits[0] + ".wav");
                Log.d("New filename after save", String.valueOf(this.getFilesDir()));
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    //handle error
                }
            }

            // Handles all TRIM
            double left = 0;
            double right = 0;
            if (!edits[1].equals("None")) {
                left = Integer.parseInt(edits[1]);
            }
            if (!edits[2].equals("None")) {
                right = Integer.parseInt(edits[2]);
            }
            Intent trim = new Intent("/trim");
            trim.putExtra("path", file.getAbsolutePath());
            trim.putExtra("left", left);
            trim.putExtra("right", right);
            sendBroadcast(trim);

            // Handles all ECHO
            double echo_val = 1;
            if (!edits[3].equals("None")) {
                echo_val = Integer.parseInt(edits[3]);
            }
            Intent echo = new Intent("/echo");
            echo.putExtra("path", file.getAbsolutePath());
            echo.putExtra("value", echo_val);
            sendBroadcast(echo);

            // Handles all GAIN
            double gain_val = 1;
            if (!edits[4].equals("None")) {
                gain_val = Integer.parseInt(edits[4]);
            }
            Intent gain = new Intent("/gain");
            gain.putExtra("path", file.getAbsolutePath());
            gain.putExtra("value", gain_val);
            sendBroadcast(gain);

            // Handles all TRANSCRIPTION
            if (!edits[5].equals("None")) {
                Intent transcription = new Intent("/transcription");
                sendBroadcast(transcription);
            }
            Log.d("SAVE", "Before Saving");
            MediaPlayer mp = MediaPlayer.create(this, Uri.fromFile(file));
            int duration = mp.getDuration();
            mp.release();
            String dur = String.valueOf(duration);

            SavedDataList saves = SavedDataList.getInstance();
            saves.addSong(edits, dur);
            saves.saveToDisk(getApplicationContext());
            DummyContent.addItem(new DummyContent.DummyItem(edits[0], dur, ""));
            Log.d("SAVE", "After Saving");

         }
    }

    @Override
    public void onChannelOpened(Channel channel) {

        Log.d("PhoneListener", "Channel established");
        if (channel.getPath().equals("/new_recording")) {
            file = new File(Environment.getExternalStorageDirectory().getPath(), getTime() + ".wav");
            Log.d("this", String.valueOf(this.getFilesDir()));
            try {
                file.createNewFile();
            } catch (IOException e) {
                //handle error
            }
            Log.d("PhoneListener", "Trying to receive file");

            channel.receiveFile(mApiClient, Uri.fromFile(file), false);
        }
        else if (channel.getPath().equals("/edit_recording")) {

        }
        else if (channel.getPath().equals("/playback")) {

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

    public String getTime() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

}
