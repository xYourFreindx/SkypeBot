package io.mazenmc.skypebot.modules;

import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.mazenmc.skypebot.utils.Utils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class FuckingWeather implements Module {

    //Imperial because fuck Celsius right?
    private static String url = "http://api.openweathermap.org/data/2.5/weather?units=imperial&q=";

    @Command(name = "fuckingweather")
    public static String cmdFuckingWeather(String message, String location) {
        try {
            return getWeather(location);
        } catch (Exception e) {
            return "THE FUCKING WEATHER MODULE FAILED FUCK! (" + Utils.upload(ExceptionUtils.getStackTrace(e)) + ")";
        }
    }

    public static String getWeather(String location) throws JSONException, IOException {
        String call = (url + location).replace(' ', '+');
        JSONObject json = new JSONObject(sendGet(call));

        if (json.getInt("cod") != 200) {
            return "I CAN'T GET THE FUCKING WEATHER!";
        }

        String str;
        double temp = Math.round(json.getJSONObject("main").getDouble("temp"));
        double metric = Math.round((temp - 32) / 1.8000);

        if (temp <= 32) {
            str = "ITS FUCKING FREEZING!";
        } else if (temp >= 33 && temp <= 60) {
            str = "ITS FUCKING COLD!";
        } else if (temp >= 61 && temp <= 75) {
            str = "ITS FUCKING NICE!";
        } else {
            str = "ITS FUCKING HOT";
        }

        return str + " THE FUCKING WEATHER IN " + location.toUpperCase() + " IS " + temp + "F | " + metric + "C";
    }

    public static String sendGet(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString().trim();
    }

}
