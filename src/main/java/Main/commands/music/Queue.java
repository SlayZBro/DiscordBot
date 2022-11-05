package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Queue extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if (e.getMessage().getContentRaw().split(" ")[0].equals("#queue")) {
            if(e.getChannelType().isMessage())
                queue(e.getChannel().asTextChannel(), null);

        }
    }


    public static void queue(TextChannel channel, InteractionHook hook){
        if(Play.audioManager.isConnected()) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(channel.getGuild());
            int trackCount = Math.min(musicManager.scheduler.queue.size(),20);

            List<AudioTrack> trackList = new ArrayList<>(musicManager.scheduler.queue);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.CYAN);
            embedBuilder.setTitle("Queue ("+trackCount+")");

            String message = "";
            for(int i=0;i<trackCount;i++){
                AudioTrack track = trackList.get(i);
                message+="#"+(i+1)+" "+track.getInfo().title+"\n";
            }

            embedBuilder.setDescription(message);

            if(hook == null) {
                channel.sendMessageEmbeds(embedBuilder.build()).queue(message1 -> {
                    message1.delete().queueAfter(5, TimeUnit.SECONDS);
                });
            }
            else{
                hook.sendMessageEmbeds(embedBuilder.build()).queue(message1 -> {
                    message1.delete().queueAfter(5,TimeUnit.SECONDS);
                });
            }
        }
    }
}
