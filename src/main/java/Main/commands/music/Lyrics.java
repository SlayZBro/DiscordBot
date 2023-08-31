package Main.commands.music;


import core.GLA;

import java.io.IOException;

public class Lyrics {


    public static String getLyrics(String songName) throws IOException {
        songName = removeContentInBrackets(songName);
        System.out.println("searching lyrics for "+songName);
        return new GLA().search(songName).getHits().getFirst().fetchLyrics();
    }

    public static String removeContentInBrackets(String input) {
        String pattern = "\\([^()]*\\)|\\[[^\\[\\]]*\\]";
        return input.replaceAll(pattern, "").trim();
    }


}
