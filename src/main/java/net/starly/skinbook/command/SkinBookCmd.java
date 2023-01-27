package net.starly.skinbook.command;

import net.starly.core.util.InventoryUtil;
import net.starly.skinbook.SkinBookMain;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.starly.skinbook.SkinBookMain.config;

@SuppressWarnings("all")
public class SkinBookCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(config.getMessage("messages.root"));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "도움말":
            case "help":
            case "?": {
                if (!player.hasPermission("starly.skinbook.help")) {
                    player.sendMessage(config.getMessage("messages.noPermissions"));
                    return true;
                }

                if (args.length != 1) {
                    player.sendMessage(config.getMessage("messages.wrongCommand"));
                    return true;
                }

                config.getMessages("messages.help").forEach(player::sendMessage);
                return true;
            }

            case "생성":
            case "create": {
                if (!player.hasPermission("starly.skinbook.create")) {
                    player.sendMessage(config.getMessage("messages.noPermissions"));
                    return true;
                }


                // CHECK ARGS
                if (args.length != 2) {
                    player.sendMessage(config.getMessage("messages.wrongCommand"));
                    return true;
                }


                // GET MATERIAL
                Material material = player.getInventory().getItemInMainHand().getType();
                if (material == Material.AIR) {
                    player.sendMessage(config.getMessage("messages.create.noItemInHand"));
                    return true;
                }


                // GET CUSTOMMODELDATA
                int customModelData;
                try {
                    customModelData = Integer.parseInt(args[1]);
                } catch (Exception ignored) {
                    player.sendMessage(config.getMessage("messages.create.invalidCustomModelData"));
                    return true;
                }
                if (customModelData < 0) {
                    player.sendMessage(config.getMessage("messages.create.invalidCustomModelData"));
                    return true;
                }


                // GET ITEMSTACK & REPLACE LORE
                ItemStack skinBook = config.getItemStack("skinBook");

                List<String> lore = skinBook.getItemMeta().getLore();
                if (lore == null) lore = new ArrayList<>();

                List<String> newLore = lore.stream().map(s -> s
                        .replace("{material}", material.name())
                        .replace("{customModelData}", customModelData + ""))
                        .collect(Collectors.toList());

                ItemMeta itemMeta = skinBook.getItemMeta();

                itemMeta.setLore(newLore);


                // SET PDC
                PersistentDataContainer pdc = itemMeta.getPersistentDataContainer();
                pdc.set(new NamespacedKey(SkinBookMain.getPlugin(), "skinBook"), PersistentDataType.STRING, "true");
                pdc.set(new NamespacedKey(SkinBookMain.getPlugin(), "material"), PersistentDataType.STRING, material.name());
                pdc.set(new NamespacedKey(SkinBookMain.getPlugin(), "customModelData"), PersistentDataType.STRING, customModelData + "");

                skinBook.setItemMeta(itemMeta);


                // GIVE SKINBOOK
                if (new InventoryUtil().getSpace(player.getInventory()) - 5 == 0) {
                    player.sendMessage(config.getMessage("messages.create.noSpaceInInventory"));
                    return true;
                }

                ItemStack mainHandItem = player.getInventory().getItemInMainHand();
                player.getInventory().setItemInMainHand(skinBook);
                player.getInventory().addItem(mainHandItem);


                // SEND MESSAGE
                player.sendMessage(config.getMessage("messages.create.success")
                        .replace("{material}", material.name())
                        .replace("{customModelData}", customModelData + ""));
                return true;
            }

            case "리로드":
            case "reload": {
                if (!player.hasPermission("starly.skinbook.reload")) {
                    player.sendMessage(config.getMessage("messages.noPermissions"));
                    return true;
                }

                if (args.length != 1) {
                    player.sendMessage(config.getMessage("messages.wrongCommand"));
                    return true;
                }

                config.reloadConfig();
                player.sendMessage(config.getMessage("messages.reloaded"));
                return true;
            }
        }

        player.sendMessage(config.getMessage("messages.wrongCommand"));
        return false;
    }
}
