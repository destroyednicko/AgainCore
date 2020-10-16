package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.util.player.PlayerUtil;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "voteclaim")
public class VoteClaimCommand {
    public void execute(Player player) {
        if (!PlayerUtil.isLiked(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c • Você não votou no NameMC."));
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "perms add " + player.getName() + " core.prefix.verified");
        player.sendMessage(CC.translate(" • Obrigado por votar!"));
    }
}
