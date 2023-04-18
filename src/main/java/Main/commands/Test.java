package Main.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.*;
import org.jetbrains.annotations.NotNull;


public class Test extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().split(" ")[0].equals("!test")){
            EntitySelectMenu s = EntitySelectMenu.create("test", EntitySelectMenu.SelectTarget.USER).setMaxValues(25).build();
            event.getChannel().asTextChannel().sendMessage("test").addActionRow(s).queue();
        }
    }
}
