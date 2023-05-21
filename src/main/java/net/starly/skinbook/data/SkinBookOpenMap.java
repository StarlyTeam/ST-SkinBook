package net.starly.skinbook.data;

import net.starly.core.jb.util.Pair;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SkinBookOpenMap {

    // 이런짓은 하지 말아야 했는데...
    // 난 그 사실을 몰랐어...
    public static Map<Player, Pair<Material, Integer>> skinBookOpenMap = new HashMap<>();
}
