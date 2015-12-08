package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.chrisvle.rechordly.dummy.DummyContent;

public class PhoneListener extends WearableListenerService implements GoogleApiClient.ConnectionCallbacks, ChannelApi.ChannelListener {

    private static final String PLAY = "/play";
    private static final String PAUSE = "/pause";
    private static final String SAVE = "/save";
    private static final String RETRY = "/retry";
    private static final String EDIT = "/edit";
    public static File file;
    private SavedDataList saves = SavedDataList.getInstance();
    public GoogleApiClient mApiClient;
    private BroadcastReceiver broadcastReceiver;

    private static Boolean edit_mode = false;


    @Override
    public void onCreate() {
        Log.d("PhoneListener", "OK");
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();

        mApiClient.connect();

        IntentFilter filter = new IntentFilter();
        filter.addAction("/edit");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(EDIT)) {
                    String name = intent.getStringExtra("filePath");
                    String path = saves.getPath(name);
                    Log.d("PATH", path);
                    file = new File(path);
                    edit_mode = true;
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
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
            Intent cropservice = new Intent(this, CropService.class);
            startService(cropservice);
            Intent gainservice = new Intent(this, GainService.class);
            startService(gainservice);
            Intent echoservice = new Intent(this, EchoService.class);
            startService(echoservice);

            Log.d("PhoneListener", "Save Request");
            String all = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d("Message", all);
            String[] edits = all.split("\\|");
            Log.d("EDIts0", edits[0]);

            Log.d("EDIts1", edits[1]);

            Log.d("EDIts2", edits[2]);
            Log.d("ALL LENGTH",String.valueOf(edits.length));
            if (edits.length == 7) {
                String name = edits[6];
                Log.d("NAME", name);
                String path = saves.getPath(name);
                Log.d("PATH", path);

                file = new File(path);
                Log.d("FILE", file.getName());
                Log.d("FILE LENGTH", String.valueOf(file.length()));
                edit_mode = true;
            }


            if (!edits[0].equals("None")) {
                Log.d("message file name", edits[0]);
                Log.d("saved file name", file.getName());
                if (!edits[0].equals(file.getName())) {
                    File newfile = new File(Environment.getExternalStorageDirectory().getPath(), edits[0] + ".wav");
                    try {
                        copy(file, newfile);
                    } catch (IOException e) {
                        Log.d("COPY", "COULD NOT BE COPIED");
                    }
                    file.delete();
                    Intent updateList = new Intent("/update_list");
                    sendBroadcast(updateList);
                    file = newfile;

                    Log.d("New filename after save", String.valueOf(file.getName()));
                    Log.d("file length", String.valueOf(file.length()));
                    Log.d("New filename after save", String.valueOf(this.getFilesDir()));
                }
            }
            Log.d("FIle name", file.getName());
            MediaPlayer song = MediaPlayer.create(this, Uri.fromFile(file));
            long durationSong = song.getDuration();
            song.release();

            // Handles all GAIN
            double gain_val = 1;
            if (!edits[3].equals("None") && (!edits[3].equals("0"))) {
                gain_val = Double.parseDouble(edits[3]);
                Intent gain = new Intent("/gain");
                gain.putExtra("filePath", file.getAbsolutePath());
                gain.putExtra("volume", gain_val);
                sendBroadcast(gain);
            }

            // Handles all ECHO
            double echo_val = 1;
            if (!edits[4].equals("None") && (!edits[4].equals("0"))) {
                echo_val = Double.parseDouble(edits[4]);
                Intent echo = new Intent("/echo");
                echo.putExtra("filePath", file.getAbsolutePath());
                echo.putExtra("level", echo_val);
                sendBroadcast(echo);
            }

            // Handles all TRIM
            double left = 0;
            double right = durationSong/1000;
            double originalDuration = right;
            Log.d("EDITS", edits[1]);

            Double leftValue = null;
            Double rightValue = null;

            if (!edits[1].equals("None")) {

                String[] time = edits[1].split(":");
                Double min = Double.parseDouble(time[0]) * 60;
                Double seconds = Double.parseDouble(time[1]);
                leftValue = min + seconds;

            }
            if (!edits[2].equals("None")) {
                String[] time = edits[2].split(":");
                Double min = Double.parseDouble(time[0]) * 60;
                Double seconds = Double.parseDouble(time[1]);
                rightValue = min + seconds;

//                right = Integer.parseInt(edits[2]);

            }

            if (leftValue == null) {
                leftValue = 0.0;
            }
            if (rightValue == null) {
                rightValue = right;
            }
            if ((rightValue == right && leftValue == 0.0)) {
                Log.d("TRIM", "NO need to trim!");
            } else {
                Log.d("FILENAME", file.getName());
                Intent trim = new Intent("/trim");
                trim.putExtra("file", file.getAbsolutePath());
                trim.putExtra("startTime", leftValue);
                trim.putExtra("endTime", rightValue);
                sendBroadcast(trim);
            }

            // Handles all TRANSCRIPTION
            if (!edits[5].equals("None")) {
                Intent transcription = new Intent("/transcription");
                sendBroadcast(transcription);
            }
            Log.d("SAVE", "Before Saving");
            Log.d("FILE", file.getName());
            Log.d("FILE", String.valueOf(file.length()));
            Log.d("ORIG", String.valueOf((originalDuration)));
            Log.d("LEFT", String.valueOf((leftValue)));
            Log.d("RIGHT", String.valueOf((rightValue)));
            double duration = originalDuration - leftValue - (originalDuration - rightValue);
            Log.d("DURATION", String.valueOf(duration));

            int minutes = (int) Math.floor(duration / 60);
            int seconds = (int) ((duration ) - (minutes * 60));
            String dur = minutes + ":" + String.format("%02d", seconds);

            Log.d("ASKLDJFLKAKLFJ", String.valueOf(seconds));

//            String dur = String.valueOf(duration);
            String gainStr;
            if (gain_val == 1) {
                gainStr = "0";
            } else {
                gainStr = String.valueOf(gain_val);
            }
            String echoStr;
            if (echo_val == 1) {
                echoStr = "0";
            } else {
                echoStr = String.valueOf(echo_val);
            }
            Log.d("EDIT MODE?", String.valueOf(edit_mode));
            if (edit_mode) {
                Log.d("EDIT MODE", "ON");
                edit_mode = false;
                if (edits[0].equals("None")) {
                    String fileName = file.getName().replaceAll("%20", " ");
                    Log.d("Gain", edits[3]);
                    if (!edits[3].equals("None") && !edits[3].equals("0")) {
                        saves.setGain(fileName, edits[3]);
                    }
                    Log.d("Echo", edits[4]);
                    if (!edits[4].equals("None") && !edits[4].equals("0")) {
                        saves.setEcho(fileName, edits[4]);
                    }
                    Log.d("Lyrics", edits[5]);
                    if (!edits[5].equals("None")) {
                        saves.setLyrics(fileName, edits[5]);
                    }
                    saves.setDuration(fileName, dur);

                } else {
                    saves.setName(file.getName(), edits[0]);
                    if (!edits[3].equals("None") && !edits[3].equals("0")) {
                        saves.setGain(edits[0], edits[3]);
                    }
                    if (!edits[4].equals("None") && !edits[4].equals("0")) {
                        saves.setEcho(edits[0], edits[4]);
                    }
                    if (!edits[5].equals("None")) {
                        saves.setLyrics(edits[0], edits[5]);
                    }
                    DummyContent.delete(file.getName());
                    saves.setDuration(edits[0], dur);
                    String displayName = edits[0];
                    displayName = displayName.substring(0, edits[0].lastIndexOf("."));
                    DummyContent.addItem(new DummyContent.DummyItem(displayName, dur, ""));
                }
                Intent updateList = new Intent("/update_list");
                sendBroadcast(updateList);
            } else {
                String displayName = file.getName();
                displayName = displayName.substring(0, displayName.lastIndexOf("."));
                saves.addSong(displayName, echoStr, gainStr, dur, edits[5], file.getAbsolutePath());
                saves.saveToDisk(getApplicationContext());
                DummyContent.addItem(new DummyContent.DummyItem(displayName, dur, ""));

                Intent updateList = new Intent("/update_list");
                sendBroadcast(updateList);
                Log.d("SAVE", "After Saving");
            }

         }
    }

    @Override
    public void onChannelOpened(Channel channel) {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("hh_mm_ss");
        String time_date = formatter.format(today);
        Log.d("PhoneListener", "Channel established");
        if (channel.getPath().equals("/new_recording")) {
            file = new File(Environment.getExternalStorageDirectory().getPath(), "new_" + time_date  + ".wav");
            Log.d("TEMP PATH", file.getAbsolutePath());
            Log.d("this", String.valueOf(this.getFilesDir()));
            try {
                file.createNewFile();
                Log.d("FIlename", file.getName());
            } catch (IOException e) {
                Log.d("ERROR", "FILE COULD NOT BE MADR");
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
        Log.d("FIlename", file.getName());

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
        unregisterReceiver(broadcastReceiver);
    }

    public String getTime() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

    public void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

}
