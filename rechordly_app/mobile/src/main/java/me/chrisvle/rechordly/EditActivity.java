package me.chrisvle.rechordly;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.musicg.wave.Wave;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javazoom.jl.converter.WaveFile;

public class EditActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;
    private IntentFilter filter;

    MediaPlayer mp;


    String filepath;
    Wave myAudio;

    byte[] b;
    double[] d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File music = new File(path, "curr.wav");

        try {
            b = getBytesFromFile(music);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Echo.echoFilter(b, music);

//        try {
//            b = getBytesFromFile(music);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        d = PassFilters.calculateFFT(b, 1, "low");

//        byte[] done = toByteArray(d);
//        InputStream i = new ByteArrayInputStream(done);
//        saveFile(i);

//        File myPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
//        File myFilter = new File(myPath, "filter.wav");

        mp = new MediaPlayer();
        try {
            mp.setDataSource(music.getAbsolutePath());
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button button = (Button) findViewById(R.id.crop);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Play", "playing the song");
                mp.start();
            }
        });


    }

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
    
    public static byte[] toByteArray(double[] doubleArray){
        int times = Double.SIZE / Byte.SIZE;
        byte[] bytes = new byte[doubleArray.length * times];
        for(int i=0;i<doubleArray.length;i++){
            ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
        }
        return bytes;
    }

    public void saveFile(InputStream f) {
        InputStream wavStream = null; // InputStream to stream the wav to trim
        File trimmedSample = null;  // File to contain the trimmed down sample
//        File sampleFile = f; // File pointer to the current wav sample

        // If the sample file exists, try to trim it
        if (f != null) {
            Log.d("File", "Orchestra is an actual file!!");
            trimmedSample = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "filter.wav");
            if (trimmedSample.isFile()) {
                Log.d("Deleting", "Deleting because it already exists");
                trimmedSample.delete();
            }

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
                // The number of bytes to copy
                long length = ((long) (50 * sample_rate) * sample_size / 4);
                wavStream.skip(44); // Skip the header
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
                } catch (IOException e) {
                }
            }
        }
    }


}
