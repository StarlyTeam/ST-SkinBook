package net.starly.skinbook.listener;

import net.starly.core.jb.util.Pair;
import net.starly.skinbook.SkinBookMain;
import net.starly.skinbook.gui.SkinBookMenu;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        if (mainHandItem.getType() != Material.WRITTEN_BOOK) return;


        // CHECK PDC
        PersistentDataContainer pdc = mainHandItem.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey(SkinBookMain.getPlugin(), "material"), PersistentDataType.STRING)) return;

        Material material = Material.valueOf(pdc.get(new NamespacedKey(SkinBookMain.getPlugin(), "material"), PersistentDataType.STRING));
        int customModelData = Integer.parseInt(pdc.get(new NamespacedKey(SkinBookMain.getPlugin(), "customModelData"), PersistentDataType.STRING));

        event.setCancelled(true);
        new SkinBookMenu(material, customModelData).openInventory(player);
        skinBookOpenMap.put(player , new Pair<>(material, customModelData));

        player.playSound(player.getLocation(), "minecraft:item.book.page_turn", 1, 1);
    }
}
