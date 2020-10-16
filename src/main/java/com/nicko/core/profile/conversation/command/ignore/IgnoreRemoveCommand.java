package com.nicko.core.profile.conversation.command.ignore;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"ignore remove", "ignorar remover"})
public class IgnoreRemoveCommand {

    public void execute(Player player, Player target) {
        if (target == player) {
            player.sendMessage(CC.translate("&c • Você não pode ignorar a si mesmo."));
            return;
        }
        if (target.hasPermission("core.ignore.bypass")) {
            player.sendMessage(CC.translate("&c • Você não pode ignorar este jogador."));
            return;
        }
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (!profile.getIgnoreList().contains(target.getUniqueId())) {
            player.sendMessage(CC.translate("&c • Você não está ignorando esse jogador."));
        } else {
            profile.getIgnoreList().remove(target.getUniqueId());
            player.sendMessage(CC.translate("&e • Você não está mais ignorando &f" + target.getName()));
        }
    }

}
