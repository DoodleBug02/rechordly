package me.chrisvle.rechordly;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Created by jeremypress on 11/30/15.
 */


public class SoundRecorder {


        private static final String TAG = "SoundRecorder";
        private static final int RECORDING_RATE = 8000; // can go up to 44K, if needed
        private static final int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
        private static final int CHANNELS_OUT = AudioFormat.CHANNEL_OUT_MONO;
        private static final int FORMAT = AudioFormat.ENCODING_PCM_16BIT;
        private static int BUFFER_SIZE = AudioRecord
                .getMinBufferSize(RECORDING_RATE, CHANNEL_IN, FORMAT);

        private final String mOutputFileName;
        private final AudioManager mAudioManager;
        private final Handler mHandler;
        private final Context mContext;
        private State mState = State.IDLE;

        private AsyncTask<Void, Void, Void> mRecordingAsyncTask;
        private AsyncTask<Void, Void, Void> mPlayingAsyncTask;

        enum State {
            IDLE, RECORDING, PLAYING
        }

        public SoundRecorder(Context context, String outputFileName
        ) {
            mOutputFileName = outputFileName;
            mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            mHandler = new Handler(Looper.getMainLooper());
            mContext = context;
        }

        /**
         * Starts recording from the MIC.
         */
        public void startRecording() {
            if (mState != State.IDLE) {
                Log.w(TAG, "Requesting to start recording while state was not IDLE");
                return;
            }


            mRecordingAsyncTask = new AsyncTask<Void, Void, Void>() {

                private AudioRecord mAudioRecord;

                @Override
                protected void onPreExecute() {
                    mState = State.RECORDING;
                }

                @Override
                protected Void doInBackground(Void... params) {
                    mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            RECORDING_RATE, CHANNEL_IN, FORMAT, BUFFER_SIZE * 3);
                    BufferedOutputStream bufferedOutputStream = null;
                    try {
                        bufferedOutputStream = new BufferedOutputStream(
                                mContext.openFileOutput(mOutputFileName, Context.MODE_PRIVATE));
                        byte[] buffer = new byte[BUFFER_SIZE];
                        mAudioRecord.startRecording();
                        while (!isCancelled()) {
                            int read = mAudioRecord.read(buffer, 0, buffer.length);
                            bufferedOutputStream.write(buffer, 0, read);
                        }
                    } catch (IOException | NullPointerException | IndexOutOfBoundsException e) {
                        Log.e(TAG, "Failed to record data: " + e);
                    } finally {
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (IOException e) {
                                // ignore
                            }
                        }
                        mAudioRecord.release();
                        mAudioRecord = null;
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    mState = State.IDLE;
                    mRecordingAsyncTask = null;
                }

                @Override
                protected void onCancelled() {
                    if (mState == State.RECORDING) {
                        Log.d(TAG, "Stopping the recording ...");
                        mState = State.IDLE;
                    } else {
                        Log.w(TAG, "Requesting to stop recording while state was not RECORDING");
                    }
                    mRecordingAsyncTask = null;
                }
            };

            mRecordingAsyncTask.execute();
        }

        public void stopRecording() {
            if (mRecordingAsyncTask != null) {
                mRecordingAsyncTask.cancel(true);
            }
        }


        /**
         * Cleans up some resources related to {@link AudioTrack} and {@link AudioRecord}
         */
        public void cleanup() {
            Log.d(TAG, "cleanup() is called");
            stopRecording();
        }
    }


