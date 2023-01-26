package Main.events;

import Main.commands.music.MessageManager;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class onReady extends ListenerAdapter {


    @Override
    public void onReady(@NotNull ReadyEvent event) {


    }


    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        if(MessageManager.message != null)
            MessageManager.message.delete().queue();
    }


}
