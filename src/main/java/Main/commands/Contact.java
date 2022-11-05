package Main.commands;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import javax.annotation.Nonnull;

public class Contact extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (event.getName().equals("המפקד")) {
            TextInput email = TextInput.create("name", "שם", TextInputStyle.SHORT)
                    .setPlaceholder("שם פרטי")
                    .setRequiredRange(2,10)
                    .setRequired(true)
                    .build();


            TextInput body = TextInput.create("body", "פתק פנייה", TextInputStyle.PARAGRAPH)
                    .setPlaceholder("מה את/ה צריך?")
                    .setRequired(true)
                    .setRequiredRange(10, 200)
                    .build();

            Modal modal = Modal.create("המפקד", "פנייה למפקד השרת")
                    .addActionRows(ActionRow.of(email), ActionRow.of(body))
                    .build();

            event.replyModal(modal).queue();
        }
    }
}
