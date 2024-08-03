package dev.coreequip.bookfinder;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

class ReadableEnchantment {

    private static final Map<Locale, Map<Enchantment, String>> ENCHANTMENTS;
    private final static TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap<>() {{
        put(1000, "M");
        put(900, "CM");
        put(500, "D");
        put(400, "CD");
        put(100, "C");
        put(90, "XC");
        put(50, "L");
        put(40, "XL");
        put(10, "X");
        put(9, "IX");
        put(5, "V");
        put(4, "IV");
        put(1, "I");
    }};

    static {
        ENCHANTMENTS = new HashMap<>();
        ENCHANTMENTS.put(Locale.GERMAN, new HashMap<>());
        ENCHANTMENTS.put(Locale.ENGLISH, new HashMap<>());

        try (var inputStream = ReadableEnchantment.class.getResourceAsStream("/enchantments.txt")) {
            assert inputStream != null;
            try (var reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

                reader.lines()
                    .map(line -> line.split("\\|"))
                    .forEach(parts -> {
                        Enchantment ench = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(parts[0]));
                        ENCHANTMENTS.get(Locale.GERMAN).put(ench, parts[1]);
                        ENCHANTMENTS.get(Locale.ENGLISH).put(ench, parts[2]);
                    });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load enchantments data", e);
        }
    }

    static String getEnchantmentName(Enchantment enchantment, Integer level, Locale locale) {
        if (enchantment == null || locale == null) return "";
        return ENCHANTMENTS.get(locale).get(enchantment) + " " + convertToRoman(level);
    }

    private static String convertToRoman(int number) {
        int l = ROMAN_NUMERALS.floorKey(number);
        return number == l ? ROMAN_NUMERALS.get(number) : ROMAN_NUMERALS.get(l) + convertToRoman(number - l);
    }

}