package app.os.discord.music.player;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private boolean repeat;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void clearQueue() {
        queue.forEach(queue::remove);
    }

    public List<AudioTrack> getTracksInQueue() {
        return new ArrayList<>(queue);
    }

    public AudioTrack removeTrack(String partOfName) {
        if (queue.isEmpty())
            return null;

        AudioTrack toRemove = null;
        for (AudioTrack temp : queue) {
            if (temp.getInfo().title.toLowerCase().contains(partOfName.toLowerCase()))
                toRemove = temp;
        }

        if (toRemove != null)
            queue.remove(toRemove);

        return toRemove;
    }

    public AudioTrack nextTrack() {
        AudioTrack next = queue.poll();
        player.startTrack(next, false);

        return next;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            nextTrack();
    }
}