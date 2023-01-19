package dev.coreequip.bookfinder;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TreeMap;

class ReadableEnchantment {

    // Source: https://minecraft.gamepedia.com/Java_Edition_data_values#Enchantments
    private static final String[] ENCHANTMENT_TRANSLATION = new String[]{
            "aqua_affinity|Wasseraffinität|Aqua Affinity",
            "bane_of_arthropods|Nemesis der Gliederfüßer|Bane of Arthropods",
            "binding_curse|Fluch der Bindung|Curse of Binding",
            "blast_protection|Explosionsschutz|Blast Protection",
            "channeling|Entladung|Channeling",
            "depth_strider|Wasserläufer|Depth Strider",
            "efficiency|Effizienz|Efficiency",
            "feather_falling|Federfall|Feather Falling",
            "fire_aspect|Verbrennung|Fire Aspect",
            "fire_protection|Feuerschutz|Fire Protection",
            "flame|Flamme|Flame",
            "fortune|Glück|Fortune",
            "frost_walker|Eisläufer|Frost Walker",
            "impaling|Harpune|Impaling",
            "infinity|Unendlichkeit|Infinity",
            "knockback|Rückstoß|Knockback",
            "looting|Plünderung|Looting",
            "loyalty|Treue|Loyalty",
            "luck_of_the_sea|Glück des Meeres|Luck of the Sea",
            "lure|Köder|Lure",
            "mending|Reparatur|Mending",
            "multishot|Mehrfachschuss|Multishot",
            "piercing|Durchschuss|Piercing",
            "power|Stärke|Power",
            "projectile_protection|Schusssicher|Projectile Protection",
            "protection|Schutz|Protection",
            "punch|Schlag|Punch",
            "quick_charge|Schnellladen|Quick Charge",
            "respiration|Atmung|Respiration",
            "riptide|Sog|Riptide",
            "sharpness|Schärfe|Sharpness",
            "silk_touch|Behutsamkeit|Silk Touch",
            "smite|Bann|Smite",
            "soul_speed|Seelenläufer|Soul Speed",
            "sweeping|Schwungkraft|Sweeping Edge",
            "swift_sneak|Huschen|Swift Sneak",
            "thorns|Dornen|Thorns",
            "unbreaking|Haltbarkeit|Unbreaking",
            "vanishig_curse|Fluch des Verschwindens|Curse of Vanishing"
    };

    private static final HashMap<Locale, HashMap> ENCHANTMENTS;

    static {
        ENCHANTMENTS = new HashMap<>(2);

        HashMap<Enchantment, String> enchantmentsDe = new HashMap<>(ENCHANTMENT_TRANSLATION.length);
        HashMap<Enchantment, String> enchantmentsEn = new HashMap<>(ENCHANTMENT_TRANSLATION.length);

        ENCHANTMENTS.put(Locale.GERMAN, enchantmentsDe);
        ENCHANTMENTS.put(Locale.ENGLISH, enchantmentsEn);

        for (String str : ENCHANTMENT_TRANSLATION) {
            StringTokenizer st = new StringTokenizer(str, "|");

            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(st.nextToken()));

            enchantmentsDe.put(ench, st.nextToken());
            enchantmentsEn.put(ench, st.nextToken());
        }
    }

    static String getEnchantmentName(Enchantment enchantment, Integer level, Locale locale) {
        if (null == enchantment || null == locale) return "";
        return ENCHANTMENTS.get(locale).get(enchantment) + " " + convertToRoman(level);
    }


    private final static TreeMap<Integer, String> map = new TreeMap<>();

    static {
        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");
    }

    private static String convertToRoman(int number) {
        int l = map.floorKey(number);
        if (number == l) {
            return map.get(number);
        }
        return map.get(l) + convertToRoman(number - l);
    }

}