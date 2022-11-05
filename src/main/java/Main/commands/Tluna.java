package Main.commands;

import Main.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class Tluna extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getMessage().getContentRaw().equals("#tluna") && event.getMember().getId().equals("249489495647322112")){

            event.getChannel().sendMessage("לפתיחת תלונה תלחצו על הכפתור").setActionRow(Button.primary("tluna","פתיחת תלונה")).queue();
            event.getMessage().delete().queue();
        }
    }
}
