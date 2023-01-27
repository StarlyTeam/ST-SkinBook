package net.starly.skinbook.event;

import net.starly.core.util.InventoryUtil;
import net.starly.skinbook.SkinBookMain;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;

import static net.starly.skinbook.SkinBookMain.config;
import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

@SuppressWarnings("all")
public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (skinBookOpenMap.containsKey(player)) {
            giveSkinBook(skinBookOpenMap.get(player).getA(), skinBookOpenMap.get(player).getB(), player);
        }

        skinBookOpenMap.remove(player);
    }

    private boolean giveSkinBook(Material material, int customModelData, Player player) {
        // GET ITEMSTACK & REPLACE LORE
        ItemStack skinBook = config.getItemStack("skinBook");

        List<String> lore = skinBook.getItemMeta().getLore();
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
        if (new InventoryUtil().getSpace(player.getInventory()) - 4 == 0) {
            player.sendMessage(config.getMessage("messages.create.noSpaceInInventory"));
            return false;
        }

        player.getInventory().addItem(skinBook);

        return true;
    }
}
