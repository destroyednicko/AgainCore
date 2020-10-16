package com.nicko.core.profile.conversation.command.ignore;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"ignore list", "ignorar listar"}, async = true)
public class IgnoreListCommand {
    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getIgnoreList().isEmpty()) {
            player.sendMessage(CC.translate("&e • Você não está ignorando ninguém."));
            return;
        }
        player.sendMessage(CC.translate("&7&l • Ignorar - Lista"));
        player.sendMessage(CC.SB_BAR);
        profile.getIgnoreList().forEach(uuid -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            player.sendMessage(CC.translate(" &7-&f" + offlinePlayer.getName()));
        });
        player.sendMessage(CC.SB_BAR);
    }
}
