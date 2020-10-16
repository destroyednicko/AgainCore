package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.player.PlayerUtil;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "vote")
public class VoteCommand {

    public void execute(Player player) {
        if (PlayerUtil.isLiked(player.getUniqueId())) {
            player.sendMessage(CC.translate("&c • Você já votou no NameMC."));
            return;
        }
        player.sendMessage(CC.translate(
            "&e • Faça login com sua conta, e vote em &ahttps://pt.namemc.com/server/jogar.againmc.com &epara receber o " +
                    "prefixo &7[&a&l✔&7] &aVerificado&e."));
    }

}
