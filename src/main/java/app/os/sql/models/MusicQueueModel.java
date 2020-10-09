package app.os.sql.models;

import app.os.discord.music.player.GetTrack;
import app.os.sql.drivers.InsertData;
import app.os.sql.drivers.SQLDriver;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MusicQueueModel extends Model {
    private final String googleApiKey;

    public MusicQueueModel(String googleApiKey) {
        super(new String[]{"guildID", "name", "tracks"}, "musicQueueModel");
        this.googleApiKey = googleApiKey;
    }

    public List<String> getTracksUrl(SQLDriver driver, String tableName, Guild guild, String queueName) {
        List<String> result = new ArrayList<>();
        Map<String, List<Object>> dbData = driver.getValuesFromTable(tableName, columnsName);

        System.out.println(dbData.toString());

        int index = 0;
        for (Object guildID : dbData.get("guildID")) {
            if (((String) guildID).equalsIgnoreCase(guild.getId())) {
                for (String trackUrl : ((String) dbData.get("tracks").get(index)).split(",")) {
                    System.out.println(queueName);
                    System.out.println((String) dbData.get("name").get(index));
                    if (((String) dbData.get("name").get(index)).equalsIgnoreCase(queueName)) {
                        result.add(GetTrack.getLink(trackUrl, googleApiKey));
                    }
                }

                index++;
            }
        }

        return result;
    }

    public void saveTracks(SQLDriver driver, String tableName, List<AudioTrack> tracks, Guild guild, String
            queueName) {
        StringBuilder parsedTracks = new StringBuilder();
        tracks.forEach(track -> parsedTracks.append("https://www.youtube.com/watch?v=").append(track.getIdentifier()).append(","));
        parsedTracks.substring(0, parsedTracks.length() - 1);

        driver.insertDataToTable(tableName, new InsertData(columnsName, new String[]{guild.getId(), queueName, parsedTracks.toString()}));
    }
}
