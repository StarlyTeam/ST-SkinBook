package net.starly.skinbook.command.tabcomplete;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SkinBookTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("도움말", "생성");
        } else if (args.length == 2) {
            if (Arrays.asList("생성", "create").contains(args[0])) return Arrays.asList("1", "2", "3", "4", "5");
        }

        return Collections.emptyList();
    }
}
