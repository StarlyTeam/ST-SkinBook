package net.starly.skinbook.event;

import net.starly.core.data.util.Tuple;
import net.starly.core.util.InventoryUtil;
import net.starly.skinbook.SkinBookMain;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import static net.starly.skinbook.SkinBookMain.config;
import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

@SuppressWarnings("all")
public class InventoryClickListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (!skinBookOpenMap.containsKey(player)) return;
        if (event.getCurrentItem() == null) return;

        if (event
                        .getCurrentItem()
                        .getItemMeta()
                        .getPersistentDataContainer()
                        .has(
                                new NamespacedKey(SkinBookMain.getPlugin(),
                                        "skinBook"),
                                PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }
        if (event.getClickedInventory() == player.getInventory()) return;

        if (event.getSlot() != config.getInt("menu.slots.item")) event.setCancelled(true);

        if (event.getSlot() == config.getInt("menu.slots.execute")) {
            Tuple<Material, Integer> data = skinBookOpenMap.get(player);

            ItemStack targetItem = event.getClickedInventory().getItem(config.getInt("menu.slots.item"));
            if (targetItem == null) {
                player.sendMessage(config.getMessage("messages.menu.noItem"));
                return;
            }

            if (targetItem.getType() != data.getA()) {
                player.sendMessage(config.getMessage("messages.menu.wrongMaterial")
                        .replace("{material}", data.getA().name())
                        .replace("{targetMaterial}", targetItem.getType().name()));
                return;
            }

            if (targetItem.getAmount() != 1) {
                player.sendMessage(config.getMessage("messages.menu.wrongAmount"));
                return;
            }

            player.closeInventory();
            if (player.getInventory().getItemInMainHand().getAmount() == 1) {
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            } else {
                player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            }

            player.sendMessage(config.getMessage("messages.menu.success"));

            int customModelData = data.getB();

            ItemMeta itemMeta = targetItem.getItemMeta();
            itemMeta.setCustomModelData(customModelData);
            targetItem.setItemMeta(itemMeta);

            if (InventoryUtil.getSpace(player.getInventory()) - 5 < 1) {
                player.getWorld().dropItem(player.getLocation(), targetItem);
            } else {
                player.getInventory().addItem(targetItem);
            }
        }
    }
}
