package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.musicg.wave.Wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

    String filepath;
    Wave myAudio;

//    private Equalizer eq;
//    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        InputStream audio = getResources().openRawResource(R.raw.orchestra);
//        File f = createFileFromInputStream(audio);
//        double[] left = null;
//        double[] d = openWav(f, left);
//        double[] filtered = PassFilters.fourierPassFilter(d, 4000, 8000, "low");


//        setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//        mp = new MediaPlayer();
//        mp = MediaPlayer.create(this, R.raw.completed);
//
//        eq = new Equalizer(0, mp.getAudioSessionId());
//        eq.setEnabled(true);

//        setupEq();






//        mReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Bundle extras = intent.getExtras();
//                filepath = extras.getString("File");
//            }
//        };
//
//        filter = new IntentFilter();
//        filter.addAction("Edit");
//        registerReceiver(mReceiver, filter);
//
//        myAudio = load(filepath);
    }

//    private void setupEq() {
//        short numOfBands = eq.getNumberOfBands();
//        final short min = eq.getBandLevelRange()[0];
////        final short max = eq.getBandLevelRange()[1];
//        for (short i = 0; i < numOfBands; i++) {
//            eq.setBandLevel(i, (short) (5 + min));
//        }
//    }


//

//


//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mReceiver);
//
//    }

    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("my_file_name");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }

    // Returns left and right double arrays. 'right' will be null if sound is mono.
    public double[] openWav(File file, double[] input) {
        byte[] wav = new byte[0];
        try {
            wav = getBytesFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get past all the other sub chunks to get to the data subchunk:
        int pos = 12;   // First Subchunk ID from 12 to 16

        // Keep iterating until we find the data chunk (i.e. 64 61 74 61 ...... (i.e. 100 97 116 97 in decimal))
        while(!(wav[pos]==100 && wav[pos+1]==97 && wav[pos+2]==116 && wav[pos+3]==97)) {
            pos += 4;
            int chunkSize = wav[pos] + wav[pos + 1] * 256 + wav[pos + 2] * 65536 + wav[pos + 3] * 16777216;
            pos += 4 + chunkSize;
        }
        pos += 8;

        // Pos is now positioned to start of actual sound data.
        int samples = (wav.length - pos)/2;     // 2 bytes per sample (16 bit sound mono)

        // Allocate memory (right will be null if only mono sound)
        input = new double[samples];;

        // Write to double array/s:
        int i=0;
        while (pos < wav.length) {
            input[i] = bytesToDouble(wav[pos], wav[pos + 1]);
            pos += 2;
            i++;
        }
        return input;
    }

    public static byte[] getBytesFromFile(File file) throws IOException {
        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
            throw new IOException("File is too large!");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;

        InputStream is = new FileInputStream(file);
        try {
            while (offset < bytes.length
                    && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }
        } finally {
            is.close();
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        return bytes;
    }

    // convert two bytes to one double in the range -1 to 1
    static double bytesToDouble(byte firstByte, byte secondByte) {
        // convert two bytes to one short (little endian)
        short s = (short) ((secondByte << 8) | firstByte);
        // convert to range from -1 to (just below) 1
        return s / 32768.0;
    }


}
