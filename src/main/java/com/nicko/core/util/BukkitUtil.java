package com.nicko.core.util;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class BukkitUtil {
    public static ImmutableMap<ChatColor, DyeColor> CHAT_DYE_COLOUR_MAP = ImmutableMap.<ChatColor, DyeColor> builder()
        .put(ChatColor.AQUA, DyeColor.LIGHT_BLUE)
        .put(ChatColor.BLACK, DyeColor.BLACK)
        .put(ChatColor.BLUE, DyeColor.LIGHT_BLUE)
        .put(ChatColor.DARK_AQUA, DyeColor.CYAN)
        .put(ChatColor.DARK_BLUE, DyeColor.BLUE)
        .put(ChatColor.DARK_GRAY, DyeColor.GRAY)
        .put(ChatColor.DARK_GREEN, DyeColor.GREEN)
        .put(ChatColor.DARK_PURPLE, DyeColor.PURPLE)
        .put(ChatColor.DARK_RED, DyeColor.RED)
        .put(ChatColor.GOLD, DyeColor.ORANGE)
        .put(ChatColor.GRAY, DyeColor.SILVER)
        .put(ChatColor.GREEN, DyeColor.LIME)
        .put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA)
        .put(ChatColor.RED, DyeColor.RED)
        .put(ChatColor.WHITE, DyeColor.WHITE)
        .put(ChatColor.YELLOW, DyeColor.YELLOW).build();

}
