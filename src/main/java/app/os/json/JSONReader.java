package app.os.json;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONReader {
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String urlString) throws IOException {
        if(urlString.contains(" "))
            urlString = urlString.replace(" ", "%20");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader((new URL(urlString)).openConnection().getInputStream(), StandardCharsets.UTF_8));) {
            String jsonText = readAll(reader);
            return new JSONObject(jsonText);
        }
    }
}