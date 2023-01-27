package net.starly.skinbook.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import static net.starly.skinbook.SkinBookMain.config;

@SuppressWarnings("all")
public class SkinBookMenu {
    private Material material;
    private int customModelData;

    public SkinBookMenu(Material material, int customModelData) {
        this.material = material;
        this.customModelData = customModelData;
    }

    public void openInventory(Player player) {
        Inventory inv = config.getInventory("menu.inv");
        player.openInventory(inv);
    }
}
