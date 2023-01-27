package net.starly.skinbook.data;

import net.starly.core.data.util.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class SkinBookOpenMap {
    public static Map<Player, Tuple<Material, Integer>> skinBookOpenMap = new HashMap<>();
}
