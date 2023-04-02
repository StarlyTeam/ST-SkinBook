package net.starly.skinbook;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.skinbook.command.SkinBookCmd;
import net.starly.skinbook.command.tabcomplete.SkinBookTab;
import net.starly.skinbook.event.InventoryClickListener;
import net.starly.skinbook.event.InventoryCloseListener;
import net.starly.skinbook.event.PlayerInteractListener;
import org.bukkit.plugin.java.JavaPlugin;

import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

public class SkinBookMain extends JavaPlugin {
    private static JavaPlugin plugin;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (!isPluginEnabled("net.starly.core.StarlyCore")) {
            getServer().getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getServer().getLogger().warning("[" + getName() + "] 다운로드 링크 : &fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17539);

        // CONFIG
        config = new Config("config", plugin);
        config.setPrefix("prefix");
        config.loadDefaultConfig();

        // COMMAND
        getServer().getPluginCommand("skin-book").setExecutor(new SkinBookCmd());
        getServer().getPluginCommand("skin-book").setTabCompleter(new SkinBookTab());

        // EVENT
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
    }

    @Override
    public void onDisable() {
        skinBookOpenMap.forEach((player, data) -> {
            player.closeInventory();
        });
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    private boolean isPluginEnabled(String path) {
        try {
            Class.forName(path);
            return true;
        } catch (NoClassDefFoundError ignored) {
        } catch (Exception ex) { ex.printStackTrace(); }
        return false;
    }
}