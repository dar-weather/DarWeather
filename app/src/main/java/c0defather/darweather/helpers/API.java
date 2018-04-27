package c0defather.darweather.helpers;

import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import c0defather.darweather.models.CacheObject;
import c0defather.darweather.models.City;

/**
 * Util class used for API calls.
 */

public class API {
    private static final String URL_CITIES = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private static final String URL_WEATHER = "http://api.openweathermap.org/data/2.5/weather";

    /**
     * Get list of cities using Google Autocomplete API.
     * @param input prefix of city name.
     * @return list of possible cities.
     * @throws JSONException if response is not in JSON format.
     */
    public static List<City> getCities(String input) throws JSONException {
        if (CacheHelper.getMap().containsKey(input)){
            CacheObject<List<City>> val = CacheHelper.getMap().get(input);
            if (System.currentTimeMillis() - val.getDate() > 60 * 60 * 1000) {
                CacheHelper.getMap().remove(input);
            } else {
                return val.getValue();
            }
        }
        List<City> cities = new ArrayList<>();

        StringBuilder builder = new StringBuilder(URL_CITIES);
        builder.append("?key=AIzaSyA6c2qBhhxMdJw1-uJYlZS5Xv4D1txSkeQ");
        builder.append("&components=country:kz&type=(cities)");
        builder.append("&language=ru&input=");
        builder.append(input);
        String response = makeGetRequest(builder.toString());
        if (response == null)
            return cities;
        JSONObject body = new JSONObject(response);
        if (!body.getString("status").equals("OK")) {
            return cities;
        }
        JSONArray predictions = body.getJSONArray("predictions");
        for (int i = 0; i < predictions.length(); i++) {
            JSONObject cityJSON = predictions.getJSONObject(i).getJSONObject("structured_formatting");
            City city = new City()
                    .setName(cityJSON.getString("main_text"));
            cities.add(city);
        }

        CacheHelper.getMap().put(input, new CacheObject(System.currentTimeMillis(),cities));
        return cities;
    }

    /**
     * Get weather for a given city using OpenWeather API.
     * @param city city contains name.
     * @return city with updated weather.
     * @throws JSONException if response is not in JSON format.
     */
    public static City getWeather(City city) throws JSONException {
        if (city.getWeather() != null)
            return city;
        if (city.getName() == null)
            throw new IllegalArgumentException("City name cannot be null");
        StringBuilder builder = new StringBuilder(URL_WEATHER);
        builder.append("?APPID=06f5d0a2897bb6ff29791c3f0ce85188");
        builder.append("&lang=ru&q=");
        builder.append(city.getName().replaceAll(" ", "%20"));
        String response = makeGetRequest(builder.toString());
        if (response == null)
            return city;

        JSONObject body = new JSONObject(response);
        if (!body.has("cod") || body.getInt("cod") != 200) {
            return city;
        }
        if (!body.getJSONObject("sys").getString("country").equals("KZ")) {
            return city;
        }
        double temp = body.getJSONObject("main").getDouble("temp");
        Log.e("ASD",body.toString());
        city.setWeather(String.valueOf((int)(temp - 273.15)) + "°C");
        return city;
    }

    /**
     * Make HTTP GET request.
     * @param url url of request.
     * @return return response of request.
     */
    private static String makeGetRequest(String url){
        HttpURLConnection connection = null;
        String response;
        InputStream is = null;
        try {
            URL getURL = new URL(url);
            connection = (HttpURLConnection) getURL.openConnection();
            connection.setRequestMethod("GET");
            connection.setDefaultUseCaches(true);
            connection.connect();
            //responseCode = connection.getResponseCode();
            is = connection.getInputStream();
            response = readIt(is);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (is != null) {
                    connection.disconnect();
                    is.close();
                }
            } catch (Exception e) {
                return null;
            }
        }
        return response;
    }

    /**
     * Read from input stream.
     * @param iStream input stream of http.
     * @return String from input stream.
     * @throws Exception if stream fails.
     */
    @Nullable
    private static String readIt(InputStream iStream) throws Exception {
        String singleLine;
        StringBuilder totalLines = new StringBuilder(iStream.available());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(iStream));

            while ((singleLine = reader.readLine()) != null) {
                totalLines.append(singleLine);
            }
        } catch (Exception e) {
            return null;
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
        return totalLines.toString();
    }
}
