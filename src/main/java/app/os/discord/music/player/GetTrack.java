package app.os.discord.music.player;

import app.os.json.JSONReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public class GetTrack {
    private static final Logger logger = LoggerFactory.getLogger("GetTrack");

    public static String getLink(String input, String googleApiKey) {
        String link = "";

        for (int i = 0; i < 3; i++) {
            try {
                new URL(input);
                link = input;
            } catch (MalformedURLException malformedURLException) {
                String apiQuery = String.format("https://www.googleapis.com/youtube/v3/search?part=id,snippet&q=%s&type=video&maxResults=1&key=%s",
                        new String(input.getBytes(), Charset.defaultCharset()),
                        googleApiKey);
                try {
                    JSONObject allData = JSONReader.readJsonFromUrl(apiQuery);
                    JSONArray videos = allData.getJSONArray("items");

                    link = "https://www.youtube.com/watch?v=" + videos.getJSONObject(0).getJSONObject("id").getString("videoId");
                    break;
                } catch (Exception exception) {
                    logger.error(exception.getMessage());
                    return "Ошибка при загрузке трека! ``" + exception.getMessage() + "``.";
                }
            }
        }

        return link;
    }
}
