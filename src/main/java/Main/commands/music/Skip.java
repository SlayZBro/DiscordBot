package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Skip extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equals("#skip")) {

            if(!e.getMember().getVoiceState().inAudioChannel()){
                e.getChannel().sendMessage("You have to be in a voice channel first").queue();
                return;
            }
            Skip(e.getGuild());

        }
    }

    public static void Skip(Guild g){
        if(Play.audioManager.isConnected()) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(g);
            musicManager.scheduler.nextTrack(Play.audioManager);



        }
    }
}
