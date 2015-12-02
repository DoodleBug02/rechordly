package me.chrisvle.rechordly;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends Activity {


    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    private static final long COUNT_DOWN_MS = TimeUnit.SECONDS.toMillis(10);
    private static final long MILLIS_IN_SECOND = TimeUnit.SECONDS.toMillis(1);
    private static final String VOICE_FILE_NAME = "audiorecord.pcm";
    private MediaPlayer mMediaPlayer;
    private AppState mState = AppState.READY;
    private SoundRecorder mSoundRecorder;

    private ProgressBar mProgressBar;
    private CountDownTimer mCountDownTimer;

    enum AppState {
        READY, PLAYING_VOICE, PLAYING_MUSIC, RECORDING
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

            }
        });
    }


    public void startRecording(View v) {
        Log.d("string", "HIIII");
        mState = AppState.RECORDING;
        mSoundRecorder.startRecording();
        Intent intent = new Intent(this, StopActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void stopRecording(View v) {
        Log.d("string", "HIIII");
        mState = AppState.READY;
        mSoundRecorder.stopRecording();
        File file = new File(this.getFilesDir(), "audiorecord.pcm");
        Log.d("Long", String.valueOf(file.length()));
        Log.d("dir", String.valueOf(this.getFilesDir()));
        Intent intent = new Intent("/new_recording");
        intent.putExtra("message", VOICE_FILE_NAME);
        sendBroadcast(intent);
    }


    /**
     * Checks the permission that this app needs and if it has not been granted, it will
     * prompt the user to grant it, otherwise it shuts down the app.
     */
    private void checkPermissions() {
        boolean recordAudioPermissionGranted =
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED;

        if (recordAudioPermissionGranted) {
            start();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_REQUEST_CODE);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                // Permission has been denied before. At this point we should show a dialog to
                // user and explain why this permission is needed and direct him to go to the
                // Permissions settings for the app in the System settings. For this sample, we
                // simply exit to get to the important part.
                Toast.makeText(this, R.string.exiting_for_permissions, Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * Starts the main flow of the application.
     */
    private void start() {
        mSoundRecorder = new SoundRecorder(this, VOICE_FILE_NAME);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermissions();
    }

    @Override
    protected void onStop() {
        if (mSoundRecorder != null) {
            mSoundRecorder.cleanup();
            mSoundRecorder = null;
        }
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onStop();
    }

}
