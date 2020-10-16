package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "tphere", permission = "core.tphere")
public class TPHereCommand {

    public void execute(Player player, @CPL("jogador") Player target) {
        Profile profile = Profile.getByPlayer(player);
        Profile profileTarget = Profile.getByPlayer(target);
        target.teleport(player);
        target.sendMessage(CC.GREEN + " • Você foi teleportado para " + CC.RESET + profile.getActiveRank().getPrefix()
            + player.getName());
        player.sendMessage(CC.GREEN + " • Você teleportou " + CC.RESET + profileTarget.getActiveRank().getPrefix()
            + target.getName() + CC.GREEN + " até você.");
    }

}
