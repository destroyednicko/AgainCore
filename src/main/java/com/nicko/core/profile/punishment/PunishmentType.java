package com.nicko.core.profile.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.ChatColor;

@Getter
@AllArgsConstructor
public enum PunishmentType {

    BLACKLIST("Blacklist", "blacklistado", "unblacklistado", true, true, new PunishmentTypeData("Blacklists", ChatColor.DARK_RED, 14), 0),
    BAN("Ban", "banido", "desbanido", true, true, new PunishmentTypeData("Bans", ChatColor.GOLD, 1), 1),
    TEMP_BAN("Temp Ban", "banido", "desbanido", true, true, new PunishmentTypeData("TempBans", ChatColor.GOLD, 1), 2),
    MUTE("Mute", "silenciado", "dessilenciado", false, true, new PunishmentTypeData("Mutes", ChatColor.YELLOW, 4), 3),
    WARN("Warning", "warned", null, false, false, new PunishmentTypeData("Warnings", ChatColor.GREEN, 13), 4),
    KICK("Kick", "expulsou", null, false, false, new PunishmentTypeData("Kicks", ChatColor.GRAY, 7), 5);

    private String readable;
    private String context;
    private String undoContext;
    private boolean ban;
    private boolean removable;
    private PunishmentTypeData typeData;
    private int weight;

    @AllArgsConstructor
    @Getter
    public static class PunishmentTypeData {

        private String readable;
        private ChatColor color;
        private int durability;

    }

    public static PunishmentType getByName(String name) {
        for (PunishmentType punishmentType : values()) {
            if (punishmentType.readable.equalsIgnoreCase(name)) {
                return punishmentType;
            }
        }
        return null;

    }

}
