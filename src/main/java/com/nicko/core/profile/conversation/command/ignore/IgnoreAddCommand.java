package com.nicko.core.profile.conversation.command.ignore;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"ignore add", "ignorar add"})
public class IgnoreAddCommand {

    public void execute(Player player, Player target) {
        if (target == player) {
            player.sendMessage(CC.translate("&c • Você não pode ignorar a si mesmo."));
            return;
        }
        if (target.hasPermission("core.ignore.bypass")) {
            player.sendMessage(CC.translate("&c • Você não pode ignorar esse jogador."));
            return;
        }
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile.getIgnoreList().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate("&c • Você já está ignorando esse jogador."));
        } else {
            profile.getIgnoreList().add(target.getUniqueId());
            player.sendMessage(CC.translate("&e • Agora você está ignorando &f" + target.getName()));
        }
    }

}
