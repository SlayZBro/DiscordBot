package Main;

import Main.commands.Contact;
import Main.commands.Test;
import Main.commands.Tluna;
import Main.commands.music.*;
import Main.commands.shutdown;
import Main.events.ButtonPressed;
import Main.events.MessageSent;
import Main.events.ModalInteraction;
import Main.events.onReady;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {
    public static JDA jda;

    public static void main(String[] args) throws Exception {

        System.out.println(Math.sqrt(Math.pow(11.12,2) + Math.pow(8.88,2)));

        JDABuilder builder = JDABuilder.createDefault("NTkzMDgwMjIyMDMxNTQ0MzIx.GqH9hp.7R0DsQmqK3YhMO8tew5xk7oRqOxAnyK8PaO2P4");

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,GatewayIntent.MESSAGE_CONTENT);

        builder.setActivity(Activity.playing("נודר נדר סימולציה"));
        jda = builder.build();





        jda.addEventListener(new Play());
        jda.addEventListener(new Pause());
        jda.addEventListener(new Resume());
        jda.addEventListener(new Stop());
        jda.addEventListener(new Queue());
        jda.addEventListener(new Replay());
        jda.addEventListener(new Skip());
        jda.addEventListener(new Volume());



        jda.addEventListener(new ButtonPressed());

        jda.addEventListener(new shutdown());
        jda.addEventListener(new Contact());
        jda.addEventListener(new ModalInteraction());
        jda.addEventListener(new Tluna());
        jda.addEventListener(new Test());


        jda.addEventListener(new MessageSent());
        jda.addEventListener(new onReady());
        System.out.println("Bot loaded");
        jda.upsertCommand("song", "Song name or URL").addOption(OptionType.STRING,"song","Song name or URL").queue();
        jda.upsertCommand("skip", "Skipping the current track").queue();
        jda.upsertCommand("replay", "replaying the current track").queue();
        jda.upsertCommand("pause", "Pauses the playing track").queue();
        jda.upsertCommand("resume", "Resumes the paused track").queue();

        jda.upsertCommand("ban", "מביא באן").addOptions(
                new OptionData(OptionType.MENTIONABLE,"user","למי?"),
                new OptionData(OptionType.STRING,"reason","למה?")).queue();






    }



}
