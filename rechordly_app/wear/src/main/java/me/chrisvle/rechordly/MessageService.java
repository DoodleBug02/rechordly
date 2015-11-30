package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

public class MessageService extends Service {
    private GoogleApiClient mApiClient;
    private BroadcastReceiver myReceiver;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("HII", "We in this");
        /* Initialize the googleAPIClient for message passing */
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        /* Successfully connected */
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        /* Connection was interrupted */
                    }
                })
                .build();
        mApiClient.connect();
        IntentFilter filter = new IntentFilter();
        filter.addAction("/new_recording");


        myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("MessageService", "Received");
                String message  = intent.getStringExtra("message");
                sendMessage("/new_recording", message);
            }
        };
        registerReceiver(myReceiver, filter);
    }


    public MessageService() {

    }

    private void sendMessage( final String path, final String text ) {
        Log.d("SS", "Atempting message send");
        new Thread( new Runnable() {
            @Override
            public void run() {
                Log.d("SS", "Atempting message send2");
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    Log.d("NODE FOUND!", node.toString());
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        mApiClient.disconnect();


    }
}
