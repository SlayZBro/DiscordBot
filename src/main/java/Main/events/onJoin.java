package Main.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class onJoin extends ListenerAdapter {


    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        drawAvatarOnPicture(event.getUser(),event.getGuild().getTextChannelById(501428564407877686L));

    }

    public static void drawAvatarOnPicture(User user, TextChannel textChannel) {
        try {
            URL avatarUrl = new URL(user.getAvatarUrl());
            BufferedImage userAvatar = ImageIO.read(avatarUrl);
            BufferedImage localPicture = ImageIO.read(new File("C:\\Users\\danie\\OneDrive\\Desktop\\Untitled.png"));
            BufferedImage combinedImage = new BufferedImage(localPicture.getWidth(), localPicture.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = combinedImage.getGraphics();

            g.drawImage(localPicture, 0, 0, null);
            g.drawImage(userAvatar, 382, 413,663,409, null);

            g.setFont(new Font("SansSerif", Font.BOLD, 80));
            g.setColor(Color.BLACK);
            g.drawString(user.getName(), 207, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(combinedImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            textChannel.sendFiles(FileUpload.fromData(imageBytes,"combined_image.png")).queue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
