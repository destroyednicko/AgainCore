package com.nicko.core.chat.command;

import com.nicko.core.Core;
import com.nicko.core.profile.Profile;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;

@CommandMeta(label = "mutechat", permission = "core.staff.mutechat")
public class MuteChatCommand {

    public void execute(CommandSender sender) {
        Core.get().getChat().togglePublicChatMute();

        String senderName;

        if (sender instanceof Player) {
            Profile profile = Profile.getProfiles().get(((Player) sender).getUniqueId());
            senderName = profile.getActiveRank().getColor() + sender.getName();
        } else {
            senderName = ChatColor.DARK_RED + "Console";
        }

        String context = Core.get().getChat().isPublicChatMuted() ? "silenciado" : "dessilenciado";

        Bukkit.broadcastMessage(Idioma.MUTE_CHAT_BROADCAST.format(context, senderName));
    }

}
