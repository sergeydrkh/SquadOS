package app.os.discord.music.utils;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class TrackInfo {
    public static class Duration {
        public static String getStringRange(AudioTrack track) {
            long endSeconds = track.getInfo().length / 1000;
            long nowSeconds = track.getPosition() / 1000;

            return String.format("%ss - %ss", nowSeconds, endSeconds);
        }

        public static long getLength(AudioTrack track) {
            return track.getDuration() / 1000;
        }
    }
}
