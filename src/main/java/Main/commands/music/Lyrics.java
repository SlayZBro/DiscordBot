package Main.commands.music;


import core.GLA;

import java.io.IOException;

public class Lyrics {


    public static String getLyrics(String songName) throws IOException {
        return new GLA().search(removeContentInBrackets(songName)).getHits().getFirst().fetchLyrics();
    }

    public static String removeContentInBrackets(String input) {
        String pattern = "\\([^()]*\\)|\\[[^\\[\\]]*\\]";
        return input.replaceAll(pattern, "").trim();
    }


}
