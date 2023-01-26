package Main.commands.music;

import Main.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Pause extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equals("#pause")) {

            if(!e.getMember().getVoiceState().inAudioChannel()){
                e.getChannel().sendMessage("You have to be in a voice channel first").queue();
                return;
            }

            pause(e.getGuild());

        }
    }

    public static void pause(Guild g){
        if(Play.audioManager.isConnected()) {
            AudioPlayer player = PlayerManager.getInstance().getMusicManager(g).player;
            if (!player.isPaused()) {
                player.setPaused(true);
                MessageManager.scheduler.shutdown();

            }
        }
    }
}
