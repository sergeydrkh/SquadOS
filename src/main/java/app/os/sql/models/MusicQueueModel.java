package app.os.sql.models;

public class MusicQueueModel extends Model {
    public MusicQueueModel() {
        super(new String[]{"queueID", "guildID", "name", "tracks"}, "musicQueueModel");
    }
}
