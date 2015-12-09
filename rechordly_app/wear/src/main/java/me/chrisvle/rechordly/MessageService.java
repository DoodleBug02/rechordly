package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.io.File;

public class MessageService extends Service implements GoogleApiClient.ConnectionCallbacks,  ChannelApi.ChannelListener {
    private GoogleApiClient mApiClient;
    private BroadcastReceiver myReceiver;
    private File audioFile;
    static private String crop_front_value;
    static private String crop_back_value;
    static private String lyrics;
    static private String gain_value;
    static private String echo_value;
    static private String old_name_value;
    static private String new_name_value;
    static private String retry;
    static private String save;
    static private Boolean edit = false;
    static private String path;


    @Override
    public void onCreate() {
        super.onCreate();
        clearVars();
        Log.d("MessageService", "Started");
        /* Initialize the googleAPIClient for message passing */
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        mApiClient.connect();
        IntentFilter filter = new IntentFilter();
        filter.addAction("/new_recording");
        filter.addAction("/play");
        filter.addAction("/pause");
        filter.addAction("/crop_front");
        filter.addAction("/crop_back");
        filter.addAction("/lyrics");
        filter.addAction("/gain");
        filter.addAction("/echo");
        filter.addAction(("/edit"));
        filter.addAction("/name");
        filter.addAction("/retry");
        filter.addAction("/save");



        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MessageService", "Message Received");
                Log.d("MessageService",intent.getAction() );
                if (intent.getAction().equals("/new_recording")) {
                    Log.d("MessageService", "New Rechording");
                    String message = intent.getStringExtra("message");
                    audioFile = new File(message);
                    Log.d("file length", String.valueOf(audioFile.length()));
                    sendFile("/new_recording");
                } else if (intent.getAction().equals("/play")) {
                    Log.d("MessageService", "Play Requested");
                    sendMessage("/play", "");
                } else if (intent.getAction().equals("/pause")) {
                    Log.d("MessageService", "Pause Requested");
                    sendMessage("/pause", "");

                } else if (intent.getAction().equals("/crop_front")) {
                    Log.d("MessageService", "Crop Front Requested");
                    crop_front_value = intent.getStringExtra("time");

                } else if (intent.getAction().equals("/crop_back")) {
                    Log.d("MessageService", "Crop Back Requested");
                    crop_back_value = intent.getStringExtra("time");


                } else if (intent.getAction().equals("/lyrics")) {
                    Log.d("MessageService", "New Lyrics Requested");
                    lyrics = intent.getStringExtra("lyrics");

                } else if (intent.getAction().equals("/gain")) {
                    Log.d("MessageService", "Gain Requested and is" + intent.getStringExtra("amount"));
                    gain_value = intent.getStringExtra("amount");

                } else if (intent.getAction().equals("/echo")) {
                    Log.d("MessageService", "Echo Requested");
                    echo_value = intent.getStringExtra("amount");


                } else if (intent.getAction().equals("/name")) {
                    Log.d("MessageService", "Name Change Requested");
                    new_name_value = intent.getStringExtra("name");

                } else if (intent.getAction().equals("/save")) {
                    Log.d("MessageService", "Save Requested");
                    checkStrings();

                    String message = new_name_value +
                                    "|" + crop_front_value +
                                    "|" + crop_back_value +
                                    "|" + gain_value +
                                    "|" + echo_value +
                                    "|" + lyrics;
                    if (edit) {
                        message = message.concat("|" + path);
                        edit = false;
                    }
                    sendMessage("/save", message);
                    clearVars();


                } else if (intent.getAction().equals("/retry")) {
                    Log.d("MessageService", "Retry Requested");
                    clearVars();


                } else if (intent.getAction().equals("/edit")) {
                    edit = true;
                    path = intent.getStringExtra("path");
                }

            }
        };
        registerReceiver(myReceiver, filter);
    }


    public MessageService() {

    }

    private void checkStrings() {
        Log.d("MessageSender", "CheckStrings");
        if (new_name_value.equals("") ) {
            new_name_value = "None";
        }
        if (crop_front_value.equals("")) {
            crop_front_value = "None";
        }
        if (crop_back_value.equals("")) {
            crop_back_value = "None";
        }
        if (gain_value.equals("")) {
            gain_value = "None";
        }
        if (echo_value.equals("")) {
            echo_value = "None";
        }
        if (lyrics.equals("")) {
            lyrics = "None";
        }

        Log.d("echovalue", echo_value);

    }

    private void clearVars() {
        crop_front_value = "";
        crop_back_value = "";
        lyrics= "";
        gain_value= "";
        echo_value= "";
        old_name_value= "";
        new_name_value= "";
        retry= "";
        save= "";


    }



    private void sendFile(final String path) {
        Log.d("SS", "Atempting message send");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("SS", "Atempting message send2");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Log.d("NODE FOUND!", node.toString());
                    ChannelApi.OpenChannelResult fileResult = Wearable.ChannelApi.openChannel(mApiClient, node.getId(), "/new_recording").await();
                    Channel channel = fileResult.getChannel();
                    channel.sendFile(mApiClient,  Uri.fromFile(audioFile));
                    Log.d("SS", "File Sent! (Probably)");
                }
            }
        }).start();
    }

    private void sendMessage(String messageType, String message) {
        //protocal:
        // Message could be the following things
        // /play (asking to play the current file
        // /pause (asking to pause the current file)
        // /retry (empty the current file)
        // /save (we want to keep this file)
            //if it's save, the following parameters may be attached, separated by |
            //1. new_name (may be the same as the old name in the case of an edit)
            //2. crop_front value
            //3. crop_back value
            //3. gain value
            //4. echo value
            //5. lyrics
            //If a parameter was not included, it's space in the string will be None
            //Example Message |My new Recording|None|00:15|None|25|I love this song

        Log.d("SS", "Atempting message send");
        final String message_path = messageType;
        final String final_message = message;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("SS", "Atempting message send2");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Log.d("NODE FOUND!", node.toString());
                    Log.d("MessageService", "Sent Message");
                    Log.d("MessageService", final_message);
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), message_path, final_message.getBytes()).await();

                }
            }
        }).start();

    }

    @Override
    public void onInputClosed(Channel channel, int int0, int int1) {
        Log.d("MessageService", "Input Closed");

    }

    @Override
    public void onOutputClosed(Channel channel, int int0, int int1) {
        Log.d("MessageService", "Output Closed");
        channel.close(mApiClient);
    }

    @Override
    public void onChannelOpened(Channel channel) {
        Log.d("MessageService", "Output Opened");

    }

    @Override
    public void onChannelClosed(Channel channel, int int0, int int1) {
        Log.d("MessageService", "channel Closed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
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
        unregisterReceiver(myReceiver);
        mApiClient.disconnect();
        Wearable.ChannelApi.removeListener(mApiClient, this);
    }

}

