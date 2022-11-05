package Main.lavaplayer;

import Main.commands.music.Play;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    public final BlockingQueue<AudioTrack> queue;
    public boolean replay = false;

    public TrackScheduler(AudioPlayer player){
        this.player = player;
        this.queue = new LinkedBlockingDeque<>();
    }

    public void queue(AudioTrack track){
        if(!player.startTrack(track,true)){
            this.queue.offer(track);
        }
    }

    public void nextTrack(AudioManager manager){
        TextChannel textChannel = null;

        if(Play.message != null){
            textChannel = Play.message.getChannel().asTextChannel();
            Play.message.delete().queue();
            Play.message = null;
        }

        if(queue.size() == 0 ){

            player.stopTrack();
            manager.closeAudioConnection();

            return;
        }
        this.player.startTrack(this.queue.poll(),false);
        if(textChannel != null){
            Play.message = textChannel.sendMessage("Now Playing: `" + player.getPlayingTrack().getInfo().title + "`")
                    .setActionRow(PlayerManager.getInstance().getMusicManager(
                            manager.getGuild()).player.isPaused() ? Button.primary("resume", " ").withEmoji(Emoji.fromUnicode("U+25B6")) :
                                    Button.primary("pause", " ").withEmoji(Emoji.fromUnicode("U+23F8"))
                            , Button.primary("skip"," ").withEmoji(Emoji.fromUnicode("U+23E9"))
                            , Button.primary("queue","Queue")
                            ,Button.link(player.getPlayingTrack().getInfo().uri, " ").withEmoji(Emoji.fromCustom("YouTube",892003898733264947L,false)))
                    .complete();

        }





    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if(endReason.mayStartNext){
            if(replay){
                this.player.startTrack(track.makeClone(),false);
                return;
            }
            nextTrack(Play.audioManager);
        }


    }
}
