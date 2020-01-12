package dev.coreequip.bookfinder;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.Base64;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import static dev.coreequip.bookfinder.I18n.$;

public class Bookfinder extends JavaPlugin {

    private static final String MSGPREFIX = ChatColor.AQUA + "[BF] " + ChatColor.RESET;
    private static final String COMMAND = "findbook";
    private static final String LOOKPREFIX = "LOOK_";

    @Override
    public void onEnable() {
        log("Plugin BookFinder enabled.");
    }

    @Override
    public void onDisable() {
        log("Plugin BookFinder disabled.");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MSGPREFIX + $("NOPLAYER", null));
            return false;
        }
        Player player = (Player) sender;
        Locale locale = I18n.getPlayerLocale(player);

        if (args.length > 0 && args[0].startsWith(LOOKPREFIX)) {
            try {
                String vectorStr = null;
                vectorStr = new String(Base64.getUrlDecoder().decode(args[0].substring(LOOKPREFIX.length())));

                String[] coords = vectorStr.split(",");
                if (3 != coords.length) return true;

                makePlayerLookAt(player, new Vector(
                        Double.parseDouble(coords[0]),
                        Double.parseDouble(coords[1]),
                        Double.parseDouble(coords[2])
                ));

            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }

            return true;
        }

        String seachStr = (args.length > 0 ? args[0] : "").toLowerCase(java.util.Locale.ENGLISH);
        if (seachStr.length() < 3) {
            sender.sendMessage(MSGPREFIX + $("THREECHARS", player));
            return true;
        }

        boolean found = false;

        int chunkX = player.getLocation().getChunk().getX();
        int chunkZ = player.getLocation().getChunk().getZ();

        double closestDistance = Double.MAX_VALUE;
        Vector closestPosition = null;

        HashSet<Location> locset = new HashSet<>();
        HashSet<Chunk> chunks = new HashSet<>();

        // get chunks around the player
        for (int x = chunkX - 1; x < chunkX + 2; x++)
            for (int z = chunkZ - 1; z < chunkZ + 2; z++)
                chunks.add(player.getWorld().getChunkAt(x, z));

        for (Chunk currentChunk : chunks) {
            for (BlockState block : currentChunk.getTileEntities()) {
                if (!block.getType().equals(Material.CHEST)) continue;

                Chest chest = (Chest) block.getBlock().getState();

                Inventory chestInventory = chest.getInventory();
                if (!chestInventory.contains(Material.ENCHANTED_BOOK)) continue;

                Location chestLocation = chest.getLocation().add(.5, .5, .5);

                if (chestInventory instanceof DoubleChestInventory) {
                    Location loc = ((DoubleChest) chestInventory.getHolder()).getLocation();
                    if (locset.contains(loc)) {
                        continue;
                    }
                    locset.add(loc);
                    chestLocation = ((DoubleChest) chestInventory.getHolder()).getLocation().add(.5, .5, .5);
                }

                for (int i = 0; i < chestInventory.getSize(); i++) {
                    ItemStack item = chestInventory.getItem(i);
                    if (null == item) continue;

                    if (!item.getType().equals(Material.ENCHANTED_BOOK)) continue;
                    EnchantmentStorageMeta esm = (EnchantmentStorageMeta) item.getItemMeta();
                    if (null == esm) continue;

                    int row = Math.floorDiv(i, 9) + 1;
                    int col = i % 9 + 1;

                    for (Map.Entry<Enchantment, Integer> entry : esm.getStoredEnchants().entrySet()) {

                        String enchantmentName = ReadableEnchantment.getEnchantmentName(entry.getKey(), entry.getValue(),
                                locale);
                        String enchantmentSearch = (enchantmentName + ReadableEnchantment.getEnchantmentName(entry.getKey(),
                                entry.getValue(), Locale.ENGLISH == locale ? Locale.GERMAN : Locale.ENGLISH))
                                .toLowerCase(locale);

                        if (!enchantmentSearch.contains(seachStr)) continue;

                        String lookVector = Base64.getUrlEncoder().withoutPadding().encodeToString(
                                chestLocation.toVector().toString().getBytes());

                        double distance = player.getLocation().distance(chestLocation);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestPosition = chestLocation.toVector();
                        }

                        String verbalPos = String.format(locale, $("DISTANCE", player), distance);

                        found = true;
                        TextComponent message = new TextComponent(String.format(locale, $("POSITION", player),
                                MSGPREFIX, ReadableEnchantment.getEnchantmentName(entry.getKey(), entry.getValue(), locale),
                                verbalPos, row, col));
                        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                "/" + COMMAND + " " + LOOKPREFIX + lookVector));
                        player.spigot().sendMessage(message);
                    }
                }
            }
        }

        if (found && null != closestPosition) {
            makePlayerLookAt(player, closestPosition);
        } else {
            player.sendMessage(MSGPREFIX + $("NOTHINGHERE", player));
        }

        return true;
    }

    // teleport is needed to change the head/eye direction
    private void makePlayerLookAt(Player player, Vector lookAt) {
        player.teleport(
                // set direction to a vector
                player.getLocation().setDirection(
                        // calculate view direction vector
                        lookAt.subtract(player.getEyeLocation().toVector())
                )
        );
    }

    private void log(String msg) {
        getLogger().log(Level.INFO, msg);
    }
}
