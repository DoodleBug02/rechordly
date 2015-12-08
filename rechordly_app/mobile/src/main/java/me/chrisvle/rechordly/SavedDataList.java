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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Goshujin on 12/6/15.
 */
public class SavedDataList  {
    private final String FILE_NAME = "audio_data";
    private HashMap<String, HashMap<String, String>> data;
    private ArrayList<String> order;
    private static final SavedDataList holder = new SavedDataList();

    private SavedDataList() {
        //data = new HashMap<>();
    }

    public static SavedDataList getInstance() {return holder;}

    public void addSong(String name, String echo, String gain, String dur, String lyrics, String uri) {
        if (data == null) {
            data = new HashMap<>();
            order = new ArrayList<>();
        }
        if (!data.containsKey(name)) {
            data.put(name, new HashMap<String, String>());
            order.add(0, name);
        }
        data.get(name).put("duration", dur);
        data.get(name).put("echo", echo); //songInfo[3]);
        data.get(name).put("gain", gain); //songInfo[4]);
        data.get(name).put("lyrics", lyrics); //songInfo[5]);
        data.get(name).put("path", uri);

    }

    public HashMap getData() {
        return data;
    }

    public void setLyrics(String name, String lyrics) {
        if (data.containsKey(name)) {
            data.get(name).put("lyrics", lyrics);
        }
    }

    public void delete(String name) {
        if (data.containsKey(name)) {
            data.remove(name);
            order.remove(order.indexOf(name));
        }
    }

    public String[] getNames() {
//        return data.keySet().toArray(new String[data.keySet().size()]);
        return order.toArray(new String[order.size()]);
    }

    public String getDuration(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("duration");
        }
        return null;
    }

    public String getPath(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("path");
        }
        return null;
    }

    public String getGain(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("gain");
        }
        return null;
    }

    public String getEcho(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("echo");
        }
        return null;
    }

    public String getLyrics(String name) {
        if (data.containsKey(name)) {
            return data.get(name).get("lyrics");
        }
        return null;
    }

    public void saveToDisk(Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(data);
            os.writeObject(order);
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
            order = (ArrayList<String>) is.readObject();
            is.close();
            fis.close();
        } catch (FileNotFoundException e) {
//            e.printStackTrace();
            data = new HashMap<>();
            order = new ArrayList<>();
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

