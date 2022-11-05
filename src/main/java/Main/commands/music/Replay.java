package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class Replay extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equals("#replay")) {

            repeat(e.getGuild());
            e.getChannel().sendMessage("Replay mode is now **"+ (PlayerManager.getInstance().getMusicManager(e.getGuild())
                    .scheduler.replay ? "on" : "off") +"**").queue();

        }
    }

    public static void repeat(Guild g){
        if(Play.audioManager.isConnected()) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(g);

            musicManager.scheduler.replay = !musicManager.scheduler.replay;

        }
    }

}
