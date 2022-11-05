package Main.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class shutdown extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        if(event.getMessage().getContentRaw().equals("shutdown") && event.getMember().getUser().getId().equals("249489495647322112")){
            event.getMessage().delete().queue();
            event.getGuild().getJDA().shutdown();
        }
    }
}
