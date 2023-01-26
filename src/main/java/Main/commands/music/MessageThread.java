package Main.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Message;

public class MessageThread implements Runnable{

    private AudioTrack track;

    public MessageThread(AudioTrack track){
        this.track = track;
    }


    @Override
    public void run() {
        System.out.println("test");
        MessageManager.message.editMessage(MessageManager.getMessage(track)).complete();
    }
}
