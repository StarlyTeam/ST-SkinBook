package net.starly.skinbook;

import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.skinbook.command.SkinBookCmd;
import net.starly.skinbook.command.tabcomplete.SkinBookTab;
import net.starly.skinbook.event.InventoryClickListener;
import net.starly.skinbook.event.InventoryCloseListener;
import net.starly.skinbook.event.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public class SkinBookMain extends JavaPlugin {
    private static JavaPlugin plugin;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (Bukkit.getPluginManager().getPlugin("ST-Core") == null) {
            Bukkit.getLogger().warning("[" + getName() + "] ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            Bukkit.getLogger().warning("[" + getName() + "] 다운로드 링크 : &fhttp://starly.kr/discord");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        plugin = this;
        new Metrics(this, 17539);

        // CONFIG
        config = new Config("config", plugin);
        config.setPrefix("prefix");
        config.loadDefaultConfig();

        // COMMAND
        Bukkit.getPluginCommand("skinbook").setExecutor(new SkinBookCmd());
        Bukkit.getPluginCommand("skinbook").setTabCompleter(new SkinBookTab());

        // EVENT
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}