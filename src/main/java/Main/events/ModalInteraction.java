package Main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class ModalInteraction extends ListenerAdapter {

    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {
        if (event.getModalId().equals("המפקד")) {
            String name = event.getValue("name").getAsString();
            String body = event.getValue("body").getAsString();

            createSupportTicket(name, body, event.getUser(), event.getGuild().getJDA());

            event.reply("פנייתך נשלחה למפקד הגזרה").setEphemeral(true).queue();
        }

        else if (event.getModalId().equals("tluna")){
            String name = event.getValue("name").getAsString();
            String target = event.getValue("target").getAsString();
            String body = event.getValue("body").getAsString();

            event.replyEmbeds(getTluna(name,target,body,event.getUser()).build()).queue();
        }
    }

    public EmbedBuilder getTluna(String name, String target, String body, User s){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setTitle("התקבלה תלונה חדשה");
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.addField("שם",name,false);
        embedBuilder.addField("נאשם",target,false);
        embedBuilder.addField("תיאור",body,false);
        embedBuilder.setAuthor(s.getName());



        return embedBuilder;
    }


    public void createSupportTicket(String name, String body, User s, JDA jda){
        jda.getUserById(249489495647322112L).openPrivateChannel().queue(privateChannel -> {privateChannel.sendMessage(
                "פנייה מ: "+s.getName()+"\n"+
                        "שם: "+name+"\n"+
                        "פנייה: "+body).queue();
        });

    }
}
