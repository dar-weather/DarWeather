package c0defather.darweather.helpers;

import android.content.Context;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import c0defather.darweather.models.CacheObject;
import c0defather.darweather.models.City;

/**
 * Created by c0defather on 4/24/18.
 *
 * CacheHelper class implements cache functionality to store retrieved data
 * for user queries and related list of cities as well as weather data.
 * It also stores last user input.
 */

public class CacheHelper {

    //=============== Constants ===============//

    private static final String FILE_PLACES = "FILE_PLACES";
    private static final String FILE_USER_INPUT = "FILE_USER_INPUT";

    //=============== Properties ===============//

    private static CacheHelper instance;
    private Context context;
    private Map<String, CacheObject<List<City>>> map;
    private CacheObject<String> userInput;

    //=============== Methods ===============//

    /**
     * Initializer for CacheHelper. Must be called once.
     * @param context Application context
     * @return singletone instance.
     * @throws IOException if cache files fail.
     * @throws ClassNotFoundException if a cache file was violated.
     */
    public static CacheHelper init(Context context) throws IOException, ClassNotFoundException {
        if (instance == null)
            instance = new CacheHelper(context);
        return instance;
    }

    /**
     * Returns mapping between city query and city names retrieved from Google Places API
     * and saved in local cache.
     * @return map
     */
    public static Map<String, CacheObject<List<City>>> getMap() {
        return instance.map;
    }

    /**
     * Returns last user input saved in local cache.
     * @return
     */
    public static CacheObject<String> getUserInput() {
        return instance != null ? instance.userInput : null;
    }

    /**
     * Default constructor.
     * @param context Application context
     * @throws IOException if cache files fail.
     * @throws ClassNotFoundException if a cache file violated.
     */
    private CacheHelper(Context context) throws IOException, ClassNotFoundException {
        this.context = context;
        installPlaces();
        installUserInput();
    }

    /**
     * Installs cache to store cities.
     * @throws IOException if cache files fails.
     * @throws ClassNotFoundException if cache files violated.
     */
    private void installPlaces() throws IOException, ClassNotFoundException {
        File directory = context.getFilesDir();
        File file = new File(directory, FILE_PLACES);
        if (file.exists()){
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            map = (Map<String, CacheObject<List<City>>>) ois.readObject();
            fis.close();
        } else {
            map = new HashMap<>();
        }
    }

    /**
     * Installs cache to store last user input.
     * @throws IOException if cache files fails.
     * @throws ClassNotFoundException if cache files violated.
     */
    private void installUserInput() throws IOException, ClassNotFoundException {
        File directory = context.getFilesDir();
        File file = new File(directory, FILE_USER_INPUT);
        if (file.exists()){
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userInput = (CacheObject<String>) ois.readObject();
            fis.close();
        } else {
            userInput = new CacheObject<>(System.currentTimeMillis(), "");
        }
    }

    /**
     * Flush data to filesystem.
     * @param destroy if instance must be set to NULL
     * @throws IOException if cache files fail.
     */
    public static void flushData(boolean destroy) throws IOException {
        instance.flushPlaces();
        instance.flushUserInput();
        if (destroy)
            instance = null;
    }

    /**
     * Flush cities data to filesystem.
     * @throws IOException if cache files fail.
     */
    private void flushPlaces() throws IOException {
        File directory = context.getFilesDir();
        File file = new File(directory, FILE_PLACES);
        FileOutputStream fos = new FileOutputStream(file, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(map);
        oos.close();
    }

    /**
     * Flush last user input to filesystem.
     * @throws IOException if cache files fail.
     */
    private void flushUserInput() throws IOException {
        File directory = context.getFilesDir();
        File file = new File(directory, FILE_USER_INPUT);
        FileOutputStream fos = new FileOutputStream(file, false);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(userInput);
        oos.close();
    }
}
