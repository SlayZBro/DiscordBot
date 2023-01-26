package Main.commands.music;

import Main.lavaplayer.GuildMusicManager;
import Main.lavaplayer.PlayerManager;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
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
                if (play(input, e.getMember(), e.getChannel().asTextChannel(), e.getGuild())) {
                    AudioPlayer player = PlayerManager.getInstance().getMusicManager(e.getGuild()).player;

                    MessageManager.sendMessage(e.getChannel().asTextChannel(), player);

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
                    e.getHook().deleteOriginal().queue();

                    AudioPlayer player = PlayerManager.getInstance().getMusicManager(e.getGuild()).player;

                    MessageManager.sendMessage(e.getChannel().asTextChannel(),player);


                }
                else{
                    e.reply("Your song has been added to the queue").setEphemeral(true).queue();
                }
            }
        }
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
