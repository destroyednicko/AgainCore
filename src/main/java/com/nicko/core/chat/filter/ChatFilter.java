package com.nicko.core.chat.filter;

import com.nicko.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ChatFilter implements Listener {

    private String command;

    public ChatFilter() {
    }

    public ChatFilter(String command) {
        this.command = command;
    }

    public abstract boolean isFiltered(String message, String[] words);

    public void punish(Player player) {
        if (command != null) {
            command = command
                .replace("{player}", player.getName())
                .replace("{player-uuid}", player.getUniqueId().toString());

            Core.get().getLogger().info(" • Despachando comando por filtro: \"" + command + "\"");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
        }
    }

}
