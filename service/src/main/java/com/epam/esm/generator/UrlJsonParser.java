package com.epam.esm.generator;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class UrlJsonParser {
    private UrlJsonParser() {}
    public static JsonObject readJsonFromUrl(String url) {
        try {
            URL con = new URL(url);
            URLConnection request = con.openConnection();
            request.connect();
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            return root.getAsJsonObject();
        } catch (IOException e) {
            return null;
        }
    }

    public static String readStringFromJson(JsonObject object,String name) {
        if(object == null) {
            return null;
        }
        return object.get(name).getAsString();
    }

}
