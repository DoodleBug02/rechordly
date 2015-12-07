package me.chrisvle.rechordly;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.HashMap;

/**
 * Created by Goshujin on 12/6/15.
 */
public class SavedDataList  {
    private final String FILE_NAME = "audio_data";
    private HashMap<String, HashMap<String, String>> data;
    private static final SavedDataList holder = new SavedDataList();

    private SavedDataList() {
        //data = new HashMap<>();
    }

    public static SavedDataList getInstance() {return holder;}

    public void addSong(String[] songInfo, String dur) {
        if (data == null) {
            data = new HashMap<>();
        }
        if (!data.containsKey(songInfo[0])) {
            data.put(songInfo[0], new HashMap<String, String>());
        }
        data.get(songInfo[0]).put("duration", dur);
        data.get(songInfo[0]).put("echo", songInfo[3]);
        data.get(songInfo[0]).put("gain", songInfo[4]);
        data.get(songInfo[0]).put("lyrics", songInfo[5]);

    }

    public HashMap getData() {
        return data;
    }

    public String[] getNames() {
        return data.keySet().toArray(new String[data.keySet().size()]);

    }

    public String getDuration(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("duration");
        }
        return null;
    }

    public void saveToDisk(Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFromDisk(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(FILE_NAME);
            ObjectInputStream is = new ObjectInputStream(fis);
            data = (HashMap<String, HashMap<String, String>>) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            data = new HashMap<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

