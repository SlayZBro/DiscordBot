package Main.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MessageThread implements Runnable{

    private AudioTrack track;

    public MessageThread(AudioTrack track){
        this.track = track;
    }


    @Override
    public void run() {
        MessageManager.message.editMessage(MessageManager.getMessage(track)).complete();
    }
}
