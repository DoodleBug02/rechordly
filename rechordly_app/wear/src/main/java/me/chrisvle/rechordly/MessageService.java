package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.ChannelApi;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.File;

public class MessageService extends Service implements GoogleApiClient.ConnectionCallbacks,  ChannelApi.ChannelListener {
    private GoogleApiClient mApiClient;
    private BroadcastReceiver myReceiver;
    private File audioFile;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HII", "We in this");
        /* Initialize the googleAPIClient for message passing */
        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        mApiClient.connect();
        IntentFilter filter = new IntentFilter();
        filter.addAction("/new_recording");


        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MessageService", "Received");
                String message = intent.getStringExtra("message");
                Log.d("DIR", String.valueOf(getFilesDir()));
                Log.d("MESSAGE", message);
                Log.d("externalPath", Environment.getExternalStorageDirectory().getPath());
                audioFile = new File(message);
                Log.d("file length", String.valueOf(audioFile.length()));
                sendFile("/new_recording");
            }
        };
        registerReceiver(myReceiver, filter);
    }


    public MessageService() {

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
                    channel.sendFile(mApiClient, Uri.fromFile(audioFile));

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
        Log.d("SS", "Atempting message send");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("SS", "Atempting message send2");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for (Node node : nodes.getNodes()) {
                    Log.d("NODE FOUND!", node.toString());
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), "/instructions", "test".getBytes()).await();

                }
            }
        }).start();


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

