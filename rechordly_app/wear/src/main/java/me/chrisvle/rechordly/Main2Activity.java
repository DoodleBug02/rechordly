package me.chrisvle.rechordly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main2Activity extends Activity {
    private static final int RECORDER_BPP = 16;
    private static final String AUDIO_RECORDER_FILE_EXT_WAV = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.raw";
    private static  int RECORDER_SAMPLERATE = 0;
    private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_STEREO;
    private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord recorder = null;
    private int bufferSize = 0;
    private Thread recordingThread = null;
    private boolean isRecording = false;
    private String fileName;
    private String filePath;

    private ImageButton startBtn;
    private ImageButton stopBtn;
    private ImageButton retryBtn;
    private RelativeLayout parentView;
    private ImageView sliders;
    private TextView text;
    private Chronometer time;

    private GestureDetector mDetector;
    private GestureDetector tapDetector;
    private ImageButton mImageButton;
    private Context t;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        t=this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                startBtn = (ImageButton) findViewById(R.id.btnStart);
                stopBtn = (ImageButton) findViewById(R.id.btnStop);
                retryBtn = (ImageButton) findViewById(R.id.retry_record);
                parentView = (RelativeLayout) findViewById(R.id.record_screen);
                sliders = (ImageView) findViewById(R.id.sliders);
                text = (TextView) findViewById(R.id.record_text);
                time = (Chronometer) findViewById(R.id.record_time);


                startBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        if (tapDetector.onTouchEvent(e)) {
                            return true;
                        } else {
                            return mDetector.onTouchEvent(e);
                        }
                    }
                });

                stopBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        if (tapDetector.onTouchEvent(e)) {
                            return true;
                        } else {
                            return mDetector.onTouchEvent(e);
                        }
                    }
                });

                retryBtn.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent e) {
                        if (tapDetector.onTouchEvent(e)) {
                            return true;
                        } else {
                            return mDetector.onTouchEvent(e);
                        }
                    }
                });
            }


        });

        //Configure single tap detector
        tapDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                return true;
            }
        });

        // Configure a gesture detector
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {


            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                if(sliders.getVisibility() == View.INVISIBLE) {
                    return true;
                }
                if (distanceX > 5.0) {
                    Intent intent = new Intent(getBaseContext(), doneActivity.class);
                    intent.putExtra("time", time.getText());
                    startActivity(intent);
                }
                if (distanceX < -5.0) {
                    Intent intent = new Intent(t, PlaybackActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                }
                return true;
            }
        });
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
            return mDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getStringExtra("swipe").equals("left")) {
            overridePendingTransition(android.R.anim.slide_in_left, 0);
        }
    }



    /** Recording Logic starts here */


    private String getFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);
        Log.d("FilePath", filepath);

        if(!file.exists()){
            file.mkdirs();
        }
        Log.d("main2path", file.getAbsolutePath());
        Log.d("filename",file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
        filePath = file.getAbsolutePath();

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + AUDIO_RECORDER_FILE_EXT_WAV);
    }

    private String getTempFilename(){
        String filepath = Environment.getExternalStorageDirectory().getPath();
        Log.d("Path", Environment.getExternalStorageDirectory().getPath());
        File file = new File(filepath,AUDIO_RECORDER_FOLDER);

        if(!file.exists()){
            Log.d("HI", "HI");
            file.mkdirs();
        }

        File tempFile = new File(filepath, AUDIO_RECORDER_TEMP_FILE);
        if(tempFile.exists())
            tempFile.delete();
        Log.d("main2path", file.getAbsolutePath());
        Log.d("filename", file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE );
        return (file.getAbsolutePath() + "/" + AUDIO_RECORDER_TEMP_FILE);
    }

    private void startRecording(){
        recorder = findAudioRecord();

        recorder.startRecording();

        isRecording = true;

        recordingThread = new Thread(new Runnable() {

            @Override
            public void run() {
                writeAudioDataToFile();
            }
        },"AudioRecorder Thread");

        recordingThread.start();
    }

    private void writeAudioDataToFile(){
        byte data[] = new byte[bufferSize];
        String filename = getTempFilename();
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int read = 0;

        if(null != os){
            while(isRecording){
                read = recorder.read(data, 0, bufferSize);

                if(AudioRecord.ERROR_INVALID_OPERATION != read){
                    try {
                        os.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void stopRecording(){
        if(null != recorder){
            isRecording = false;

            recorder.stop();
            recorder.release();

            recorder = null;
            recordingThread = null;
        }
        fileName = getFilename();
        Log.d("FILENAME FINAL", fileName);
        copyWaveFile(getTempFilename(), fileName);
        deleteTempFile();
    }

    private void deleteTempFile() {
        File file = new File(getTempFilename());

        file.delete();
    }

    private void copyWaveFile(String inFilename,String outFilename){
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = RECORDER_SAMPLERATE;
        int channels = 1;
        long byteRate = RECORDER_BPP * RECORDER_SAMPLERATE * channels/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(inFilename);
            out = new FileOutputStream(outFilename);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;

            Log.d("String", "File size: " + totalDataLen);

            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R';  // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f';  // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16;  // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1;  // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (2 * 16 / 8);  // block align
        header[33] = 0;
        header[34] = RECORDER_BPP;  // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }

    public void btnClick(View v) {
        switch(v.getId()){
            case R.id.btnStart:{
                stopBtn.bringToFront();
                sliders.setVisibility(View.INVISIBLE);
                text.setText("Stop");
                time.setBase(SystemClock.elapsedRealtime());
                time.start();
                parentView.invalidate();
                startRecording();
                break;
                }
            case R.id.btnStop:{
                retryBtn.bringToFront();
                sliders.setVisibility(View.VISIBLE);
                text.setText("Retry");
                time.stop();
                parentView.invalidate();
                stopRecording();
                Intent intent = new Intent("/new_recording");
                File f = new File(filePath);

                intent.putExtra("message", fileName);
                intent.putExtra("Path", filePath );
                sendBroadcast(intent);
                File fe = new File(fileName);
                Log.d("FILEPATH", fileName);
                Log.d("FILE?", String.valueOf(fe.length()));
                Log.d("EXISTS?", String.valueOf((fe.exists())));
                break;
                }
            case R.id.retry_record:{
                sliders.setVisibility(View.INVISIBLE);
                text.setText("Record");
                time.setBase(SystemClock.elapsedRealtime());
                startBtn.bringToFront();
                parentView.invalidate();

                //FIXME code for redoing the recording goes here
                break;

            }
            }
        }

    private static int[] mSampleRates = new int[] { 8000, 44100, 22050, 8000 };
    public AudioRecord findAudioRecord() {
        Log.d("STEREONUMBER", String.valueOf(AudioFormat.CHANNEL_IN_STEREO));
        for (int rate : mSampleRates) {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT }) {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO }) {
                    try {
                        Log.d("Attempt", "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                                + channelConfig);
                         bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                RECORDER_SAMPLERATE = rate;
                                Log.d("RATE", String.valueOf(rate));
                                return recorder;
                        }
                    } catch (Exception e) {
                        Log.e("Attempt", rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }

}