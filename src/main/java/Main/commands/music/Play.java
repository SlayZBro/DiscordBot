package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.*;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Play extends ListenerAdapter {

    private static YouTube youTube;
    public static AudioManager audioManager;

    public static Message message;

    public Play() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("DiscordBot")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent e) {
        if(e.getMessage().getContentRaw().split(" ")[0].equals("#play")){
            String input = e.getMessage().getContentRaw().replace("#play ","");

            if(e.getChannelType().isMessage()) {
                if (play(input, e.getMember(), (TextChannel) e.getChannel(), e.getGuild())) {
                    sendMessage( e.getChannel().asTextChannel(), e.getGuild());
                    e.getMessage().delete().queue();
                }
            }

        }


    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent e) {
        if(e.getName().equals("play")){
            String input = e.getOption("song").getAsString();
            if(input == null){
                return;
            }

            if(play(input,e.getMember(), (TextChannel) e.getChannel(),e.getGuild()) ){


                if(message == null) {
                    e.deferReply().queue();
                    sendMessage(e.getHook(), e.getGuild());
                }
                else{
                    e.reply("Your song has been added to the queue").setEphemeral(true).queue();
                }
            }
        }
    }

    private static void sendingMessage(TextChannel channel, InteractionHook interactionHook, Guild g){
        if(message != null){
            message.delete().queue();
            message = null;
        }

        if(message == null) {

            AudioTrack track = PlayerManager.getInstance().getMusicManager(g).player.getPlayingTrack();
            GuildMusicManager manager = PlayerManager.getInstance().getMusicManager(g);

            while (track == null) {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    track = PlayerManager.getInstance().getMusicManager(g).player.getPlayingTrack();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            if(interactionHook != null) {
                message = interactionHook.sendMessage("Now Playing: `" + track.getInfo().title + "`   (Volume: "+manager.player.getVolume()+")")
                        .addActionRow(
                                manager.player.isPaused() ? Button.primary("resume", " ").withEmoji(Emoji.fromUnicode("U+25B6")) :
                                        Button.primary("pause", " ").withEmoji(Emoji.fromUnicode("U+23F8"))
                                , Button.primary("skip", " ").withEmoji(Emoji.fromUnicode("U+23E9"))
                                , Button.primary("queue", "Queue")
                                , Button.primary("repeat", " ").withEmoji(manager
                                        .scheduler.replay ? Emoji.fromUnicode("U+1F502") : Emoji.fromUnicode("U+1F501"))
                                , Button.link(track.getInfo().uri, " ").withEmoji(Emoji.fromCustom("YouTube",892003898733264947L,false))).complete();
            }else{
                message = channel.sendMessage("Now Playing: `" + track.getInfo().title + "`   (Volume: "+manager.player.getVolume()+")")
                        .setActionRow(
                                PlayerManager.getInstance().getMusicManager(g).player.isPaused() ? Button.primary("resume", " ").withEmoji(Emoji.fromUnicode("U+25B6")) :
                                        Button.primary("pause", " ").withEmoji(Emoji.fromUnicode("U+23F8"))
                                , Button.primary("skip", " ").withEmoji(Emoji.fromUnicode("U+23E9"))
                                , Button.primary("queue", "Queue")
                                , Button.primary("repeat", " ").withEmoji(PlayerManager.getInstance().getMusicManager(g)
                                        .scheduler.replay ? Emoji.fromUnicode("U+1F502") : Emoji.fromUnicode("U+1F501"))
                                , Button.link(track.getInfo().uri, " ").withEmoji(Emoji.fromCustom("YouTube",892003898733264947L,false))).complete();
            }
        }


    }

    public static void sendMessage(InteractionHook hook, Guild g){
        sendingMessage(null, hook, g);
    }
    public static void sendMessage(TextChannel channel, Guild g){
        sendingMessage(channel, null, g);
    }



    public static boolean play(String input, Member m, TextChannel c, Guild g){
        if(!m.getVoiceState().inAudioChannel()){
            c.sendMessage("You have to be in a voice channel first").queue();
            return false;
        }


        if(!isUrl(input)){
            String ytSearch = searchYoutube(input);

            if(ytSearch == null){
                c.sendMessage("null").queue();
                return false;
            }

            input = ytSearch;
        }


        audioManager = g.getAudioManager();
        AudioChannel vc =m.getVoiceState().getChannel();

        audioManager.openAudioConnection(vc);



        PlayerManager.getInstance().loadAndPlay(c,input);
        PlayerManager.getInstance().getMusicManager(g).player.setVolume(60);
        return true;
    }






    private static boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        } catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    public static String searchYoutube(String input) {
        String youtubeKey = "AIzaSyC9sWizw7QVOqV27mM14qscP2QsCc0sYZ0";

        try {
            List<SearchResult> results = youTube.search()
                    .list("id,snippet")
                    .setQ(input)
                    .setMaxResults(10L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(youtubeKey)
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }



}
