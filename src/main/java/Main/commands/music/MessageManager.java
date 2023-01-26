package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageManager {

    public static Message message = null;
    public static  ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);;



    public static void sendMessage(TextChannel channel, AudioPlayer player) {
        if(message == null) {
            AudioTrack track = player.getPlayingTrack();
            while (track == null) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    track = player.getPlayingTrack();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            StringBuilder messageBuilder = getMessage(track);
            message = channel.sendMessage(messageBuilder)
                    .setActionRow(getButtons(channel.getGuild(), track)).complete();


            scheduler.scheduleAtFixedRate(new MessageThread(track), 0, 1, TimeUnit.SECONDS);
        }

    }


    public static StringBuilder getMessage(AudioTrack track){
        //🔴-------------------|
        StringBuilder builder = new StringBuilder("Now Playing: `" + track.getInfo().title + "`\n");
        long current = track.getPosition()/1000;
        long duration = track.getDuration()/1000;
        builder.append("`["+timeFormat(current)+"/"+timeFormat(duration)+"]`\n");
        builder.append("|");
        for(long i=0; i<current*20/duration; i++){
            builder.append("-");
        }
        builder.append("🔴");

        for(long i = current*20/duration; i<20; i++){
            builder.append("-");
        }
        builder.append("|");

        return builder;

    }


    public static void updateMessage(Guild g){
        if(message != null) {


            AudioTrack track = getTrack(g);

            message.delete().queueAfter(400,TimeUnit.MILLISECONDS);
            StringBuilder builder = getMessage(track);

            message = message.reply(builder)
                    .setActionRow(getButtons(g, track)).complete();


        }
    }





    private static ArrayList<Button> getButtons(Guild g, AudioTrack track){
        ArrayList<Button> arrayList = new ArrayList<>();

        arrayList.add(PlayerManager.getInstance().getMusicManager(g).player.isPaused() ? Button.primary("resume", " ").withEmoji(Emoji.fromUnicode("U+25B6")) :
        Button.primary("pause", " ").withEmoji(Emoji.fromUnicode("U+23F8")));
        arrayList.add(Button.primary("skip", " ").withEmoji(Emoji.fromUnicode("U+23E9")));
        arrayList.add(Button.primary("queue", "Queue"));
        arrayList.add(Button.primary("repeat", " ").withEmoji(PlayerManager.getInstance().getMusicManager(g)
                .scheduler.replay ? Emoji.fromUnicode("U+1F502") : Emoji.fromUnicode("U+1F501")));
        arrayList.add(Button.link(track.getInfo().uri, " ").withEmoji(Emoji.fromCustom("YouTube",892003898733264947L,false)));

        return arrayList;
    }

    private static AudioTrack getTrack(Guild g){
        AudioPlayer player = PlayerManager.getInstance().getMusicManager(g).player;
        AudioTrack track = player.getPlayingTrack();

        while (track == null) {
            try {
                TimeUnit.SECONDS.sleep(2);
                track = player.getPlayingTrack();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return track;

    }

    private static String timeFormat(long time){
        String a = "";

        a += (time/60 < 10) ? "0"+(time/60) : time/60;
        a += ":";
        a += (time%60 < 10) ? "0"+(time%60) : time%60;

        return a;
    }


}
