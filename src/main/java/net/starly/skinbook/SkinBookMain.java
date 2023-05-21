package net.starly.skinbook;

import lombok.Getter;
import net.starly.core.bstats.Metrics;
import net.starly.core.data.Config;
import net.starly.skinbook.command.SkinBookCmd;
import net.starly.skinbook.command.tabcomplete.SkinBookTab;
import net.starly.skinbook.listener.InventoryListener;
import net.starly.skinbook.listener.PlayerInteractListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static net.starly.skinbook.data.SkinBookOpenMap.skinBookOpenMap;

public class SkinBookMain extends JavaPlugin {

    @Getter private static JavaPlugin instance;
    public static Config config;

    @Override
    public void onEnable() {
        // DEPENDENCY
        if (!isPluginEnabled("ST-Core")) {
            getLogger().warning("ST-Core 플러그인이 적용되지 않았습니다! 플러그인을 비활성화합니다.");
            getLogger().warning("다운로드 링크 : §fhttp://starly.kr/");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        new Metrics(this, 17539);

        // CONFIG
        // 이런짓은 하지 말아야 했는데...
        // 난 그 사실을 몰랐어...
        config = new Config("config", instance);
        config.setPrefix("prefix");
        config.loadDefaultConfig();

        // COMMAND
        getServer().getPluginCommand("skin-book").setExecutor(new SkinBookCmd());
        getServer().getPluginCommand("skin-book").setTabCompleter(new SkinBookTab());

        // EVENT
        getServer().getPluginManager().registerEvents(new InventoryListener(), instance);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), instance);
    }

    @Override
    public void onDisable() {
        skinBookOpenMap.forEach((player, data) -> player.closeInventory());
    }

    private boolean isPluginEnabled(String name) {
        Plugin plugin = getServer().getPluginManager().getPlugin(name);
        return plugin != null && plugin.isEnabled();
    }
}