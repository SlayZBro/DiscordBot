package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Stop extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equals("#stop")) {

            if(!e.getMember().getVoiceState().inAudioChannel()){
                e.getChannel().sendMessage("You have to be in a voice channel first").queue();
                return;
            }

            if(Play.audioManager.isConnected()) {


                GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(e.getGuild());
                musicManager.player.stopTrack();
                musicManager.scheduler.queue.clear();
                Play.audioManager.closeAudioConnection();
            }
        }
    }
}
