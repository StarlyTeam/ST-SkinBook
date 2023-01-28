package net.starly.skinbook.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

@SuppressWarnings("all")
public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        skinBookOpenMap.remove(player);
    }
}
