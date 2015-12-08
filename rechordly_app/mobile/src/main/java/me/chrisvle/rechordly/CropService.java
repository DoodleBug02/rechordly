package me.chrisvle.rechordly;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javazoom.jl.converter.WaveFile;

public class CropService extends Service {

    private BroadcastReceiver broadcastReceiver;
    final String TRIM = "/trim";

    public CropService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("CropService", "Started");
        IntentFilter filter = new IntentFilter();
        filter.addAction("/trim");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(TRIM)) {
                    String file = intent.getStringExtra("file");
                    Double startTime = intent.getDoubleExtra("startTime", 1)/2;
                    Double endTime = intent.getDoubleExtra("endTime", 1)/2;

                    Log.d("THE FILE PATH: ", file);
                    Log.d("START TIME: ", startTime.toString());
                    Log.d("END TIME: ", endTime.toString());

                    TrimToSelection(file, startTime, endTime);
                    Log.d("CropService", "Trim Started");
                }
            }
        };
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    public void TrimToSelection(String path, double startTime, double endTime) {
        InputStream wavStream = null; // InputStream to stream the wav to trim
        File trimmedSample = null;  // File to contain the trimmed down sample
//        File sampleFile = f; // File pointer to the current wav sample
        InputStream f = null;
        try {
            f = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // If the sample file exists, try to trim it
        Log.d("F NUll?", String.valueOf((f != null)));
        if (f != null) {
            trimmedSample = new File(path);

            // Trim the sample down and write it to file
            try {
                wavStream = new BufferedInputStream(f);
                // Javazoom WaveFile class is used to write the wav
                WaveFile waveFile = new WaveFile();
                int sample_rate = 8000;
                short sample_size = 16;
                short num_channels = 1;
                waveFile.OpenForWrite(trimmedSample.getAbsolutePath(), sample_rate, sample_size, num_channels);
                // The number of bytes of wav data to trim off the beginning
                long startOffset = (long) (startTime * sample_rate) * sample_size / 4;
                // The number of bytes to copy
                long length = ((long) (endTime * sample_rate) * sample_size / 4) - startOffset;
                wavStream.skip(44); // Skip the header
                wavStream.skip(startOffset);
                byte[] buffer = new byte[1024];
                int i = 0;
                while (i < length) {
                    if (length - i >= buffer.length) {
                        wavStream.read(buffer);
                    } else { // Write the remaining number of bytes
                        buffer = new byte[(int) length - i];
                        wavStream.read(buffer);
                    }
                    short[] shorts = new short[buffer.length / 2];
                    ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
                    waveFile.WriteData(shorts, shorts.length);
                    i += buffer.length;
                }
                waveFile.Close(); // Complete writing the wave file
                wavStream.close(); // Close the input stream
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (wavStream != null) wavStream.close();
                    Log.d("TRIM IS DONE", "DONE");
                    Intent crop_done = new Intent("/crop_done");
                    sendBroadcast(crop_done);
                } catch (IOException e) {
                }
            }
        }
    }
}
