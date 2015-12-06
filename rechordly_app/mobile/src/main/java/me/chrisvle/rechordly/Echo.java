package me.chrisvle.rechordly;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by Chris on 12/5/15.
 */
public class Echo {

    public static void echoFilter (byte[] b, File music, Double level) {
        byte[] temp = b.clone();
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(music, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            randomAccessFile.seek(44);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int N = 8000 / 8;
        level = level/100;
        for (int n = N + 1; n < b.length; n++) {
            b[n] = (byte) (temp[n] + level * temp[n - N]);
        }
        try {
            randomAccessFile.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
