package com.nicko.core.chat.command;

import com.nicko.core.profile.Profile;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.Idioma;

@CommandMeta(label = {"clearchat", "cc"}, permission = "core.staff.clearchat")
public class ClearChatCommand {

    public void execute(CommandSender sender) {
        String[] strings = new String[101];

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("core.staff") &&
                !Core.get().getMainConfig().getBoolean("CHAT.CLEAR_CHAT_FOR_STAFF")) {
                player.sendMessage("");
            } else {
                player.sendMessage(strings);
            }
        }

        String senderName;
        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        Bukkit.broadcastMessage(Idioma.CLEAR_CHAT_BROADCAST.format(senderName));
    }

}
