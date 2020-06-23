package dev.coreequip.bookfinder;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;

public class I18n {
    private static final String[] I18N_TEXTS = new String[]{
            "NOPLAYER| |You must be a player to use this command.",
            "THREECHARS|Bitte mindestens 3 Zeichen im Suchstring angeben.|Please enter at least 3 chars.",
            "DISTANCE|ist \u00A7n%.1fm entfernt\u00A7r|is \u00A7n%.1fm away\u00A7r",
            "POSITION|%s%s %s an Position Reihe %d Spalte %d|%s%s %s on position row %d column %d",
            "NOTHINGHERE|Leider nichts in diesem Chunk gefunden.|Sorry, nothing found in this chunk."
    };

    public static String $(String key, Player player) {
        Locale loc = getPlayerLocale(player);
        String result = I18N_MAP.get(loc).get(key.toUpperCase());
        return null == result ? "" : result;
    }

    public static Locale getPlayerLocale(Player player) {
        return null == player ? Locale.ENGLISH : (player.getLocale().startsWith("de") ? Locale.GERMAN : Locale.ENGLISH);
    }

    private static final HashMap<Locale, HashMap<String, String>> I18N_MAP = new HashMap<>(2);

    static {
        HashMap<String, String> textsDe = new HashMap<>(I18N_TEXTS.length);
        HashMap<String, String> textsEn = new HashMap<>(I18N_TEXTS.length);

        I18N_MAP.put(Locale.GERMAN, textsDe);
        I18N_MAP.put(Locale.ENGLISH, textsEn);

        for (String str : I18N_TEXTS) {
            StringTokenizer st = new StringTokenizer(str, "|");

            String key = st.nextToken().toUpperCase();

            textsDe.put(key, st.nextToken().trim());
            textsEn.put(key, st.nextToken().trim());
        }
    }
}
