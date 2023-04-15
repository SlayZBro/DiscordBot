package Main.events;

import Main.commands.music.*;
import Main.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;

public class ButtonPressed extends ListenerAdapter {


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent e) {

        if(e.getMember().getId().equals("915182298901643265")){
            e.deferReply().queue();
            e.getHook().deleteOriginal().queue();
            return;

        }

        if(Play.audioManager != null && Play.audioManager.getConnectedChannel().getMembers().contains(e.getMember())) {



            if (e.getButton().getId().equals("skip")) {

                Skip.Skip(e.getGuild());
                if (PlayerManager.getInstance().getMusicManager(e.getGuild()).scheduler.queue.size() != 0) {

                    e.deferReply().queue();
                    e.getHook().deleteOriginal().queue();


                }
                return;


            } else if (e.getButton().getId().equals("pause")) {
                e.deferReply().queue();
                e.getHook().deleteOriginal().queue();
                Pause.pause(e.getGuild());
                MessageManager.updateMessage(e.getGuild());
                return;
            } else if (e.getButton().getId().equals("resume")) {
                e.deferReply().queue();
                e.getHook().deleteOriginal().queue();
                Resume.resume(e.getGuild());
                MessageManager.updateMessage(e.getGuild());
                return;
            }

            else if(e.getButton().getId().equals("repeat")){
                e.deferReply().queue();
                e.getHook().deleteOriginal().queue();

                Replay.repeat(e.getGuild());
                MessageManager.updateMessage(e.getGuild());
                return;
            }

        }

        if(e.getButton().getId().equals("queue")){
            e.deferReply().queue();

            Queue.queue(e.getChannel().asTextChannel(), e.getHook());
        }

        else if(e.getButton().getId().equals("tluna")){
            modal(e);
        }


        else{
            e.reply("You have to be in the same voice channel as me").setEphemeral(true).queue();
        }
    }


    private void modal(ButtonInteractionEvent event){
        TextInput name = TextInput.create("name", "שם", TextInputStyle.SHORT)
                .setPlaceholder("שם פרטי")
                .setRequiredRange(2,10)
                .setRequired(true)
                .build();


        TextInput target = TextInput.create("target", "שם הנאשם", TextInputStyle.SHORT)
                .setPlaceholder("שם הפרטי של הנאשם")
                .setRequiredRange(2,10)
                .setRequired(true)
                .build();

        TextInput body = TextInput.create("body", "תיאור", TextInputStyle.PARAGRAPH)
                .setPlaceholder("תיאור התלונה כולל הוכחות")
                .setRequiredRange(20,300)
                .setRequired(true)
                .build();

        net.dv8tion.jda.api.interactions.components.buttons.Button.primary("test","test");




        Modal modal = Modal.create("tluna", "פתיחת תלונה")
                .addActionRows(ActionRow.of(name), ActionRow.of(target), ActionRow.of(body))
                .build();

        event.replyModal(modal).queue();
    }


}
