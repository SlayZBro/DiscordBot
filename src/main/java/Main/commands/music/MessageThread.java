package Main.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MessageThread implements Runnable{

    private AudioPlayer player;

    public MessageThread(AudioPlayer player){
        this.player = player;
    }


    @Override
    public void run() {
        if(MessageManager.message != null && player.getPlayingTrack()!=null)
            MessageManager.message.editMessage(MessageManager.getMessage(player.getPlayingTrack())).complete();
    }
}
