package Main.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    private final AudioPlayerSendHandler sendHandler;


    public GuildMusicManager(AudioPlayerManager player) {
        this.player = player.createPlayer();
        this.scheduler = new TrackScheduler(this.player);
        this.player.addListener(this.scheduler);

        this.sendHandler = new AudioPlayerSendHandler(this.player);

    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }
}
