package net.starly.skinbook.event;

import net.starly.core.data.util.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static net.starly.skinbook.SkinBookMain.config;
import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

@SuppressWarnings("all")
public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!skinBookOpenMap.containsKey(player)) return;
        if (event.getClickedInventory() == player.getInventory()) return;

        if (event.getSlot() != config.getInt("menu.slots.item")) event.setCancelled(true);

        if (event.getSlot() == config.getInt("menu.slots.execute")) {
            Tuple<Material, Integer> data = skinBookOpenMap.get(player);

            ItemStack targetItem = event.getClickedInventory().getItem(config.getInt("menu.slots.item"));
            if (targetItem == null) {
                player.sendMessage(config.getMessage("messages.menu.noItem"));
                player.closeInventory();
                return;
            }

            if (targetItem.getType() != data.getA()) {
                player.sendMessage(config.getMessage("messages.menu.wrongMaterial")
                        .replace("{material}", data.getA().name())
                        .replace("{targetMaterial}", targetItem.getType().name()));
                player.closeInventory();
                return;
            }


            skinBookOpenMap.remove(player);
            player.closeInventory();

            int customModelData = data.getB();

            ItemMeta itemMeta = targetItem.getItemMeta();
            itemMeta.setCustomModelData(customModelData);
            targetItem.setItemMeta(itemMeta);

            player.sendMessage(config.getMessage("messages.menu.success"));
            player.getInventory().addItem(targetItem);
        }
    }
}
