package com.nicko.core.chat.command;

import com.nicko.core.Core;
import com.nicko.core.CoreAPI;
import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "slowchat", permission = "core.staff.slowchat")
public class SlowChatCommand {

    public void execute(CommandSender sender) {
        Core.get().getChat().togglePublicChatDelay();

        String senderName;

        if (sender instanceof Player) {
            senderName = CoreAPI.getColoredName((Player) sender);
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        String context = Core.get().getChat().getDelayTime() == 1 ? "" : "s";

        if (Core.get().getChat().isPublicChatDelayed()) {
            Bukkit.broadcastMessage(Idioma.DELAY_CHAT_ENABLED_BROADCAST.format(senderName,
                Core.get().getChat().getDelayTime(), context));
        } else {
            Bukkit.broadcastMessage(Idioma.DELAY_CHAT_DISABLED_BROADCAST.format(senderName));
        }
    }

    public void execute(CommandSender sender, Number secondsN) {
        int seconds = secondsN.intValue();
        if (seconds < 0 || seconds > 60) {
            sender.sendMessage(ChatColor.RED + " • O delay deve estar entre 1-60 segundos.");
            return;
        }

        String context = seconds == 1 ? "" : "s";

        sender.sendMessage(ChatColor.GREEN + " • Você atualizou o delay para " + seconds + " segundo" + context + ".");
        Core.get().getChat().setDelayTime(seconds);
    }

}
