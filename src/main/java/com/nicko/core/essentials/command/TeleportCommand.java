package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"tp", "teleport", "tppos"}, permission = "core.teleport", autoAddSubCommands = false)
public class TeleportCommand {

    public void execute(Player player, @CPL("jogador") Profile profileTarget) {
        Player target = Bukkit.getPlayer(profileTarget.getUuid());
        if (target != null) {
            player.teleport(target);
            player.sendMessage(CC.GREEN + " • Você foi teleportado para " + CC.RESET + profileTarget.getActiveRank().getPrefix()
                + target.getName());
            return;
        }
        Location lastLocation = profileTarget.getLastLocation();
        if (lastLocation == null) {
            player.sendMessage(CC.RED + " • Último jogador não encontrado");
            return;
        }
        player.teleport(lastLocation);
        player.sendMessage(CC.translate("&e • Você foi teleportado para a última localização de " + profileTarget.getActiveRank().getPrefix()
            + profileTarget.getName() + "&e."));
    }

    public void execute(Player player, @CPL("origem") Player origin, @CPL("alvo") Player target) {
        origin.teleport(target);
        origin.sendMessage(CC.GREEN + " • Você foi teleportado para " + CC.RESET + target.getName() + CC.GREEN + " por " + CC.RESET + player.getName());
    }

    public void execute(Player player, @CPL("origem") Player origin, @CPL("x") Number x, @CPL("y") Number y, @CPL("z") Number z) {
        origin.teleport(new Location(origin.getWorld(), x.intValue(), y.intValue(), z.intValue()));
        player.sendMessage(CC.GREEN + " • Você foi teleportado para " + CC.RESET + "(" + x.intValue() + "," + y.intValue() + "," + z.intValue() + ")" + CC.GREEN +
                " por " + CC.RESET + player.getName());
    }

    public void execute(Player player, @CPL("x") Number x, @CPL("y") Number y, @CPL("z") Number z) {
        player.teleport(new Location(player.getWorld(), x.intValue(), y.intValue(), z.intValue()));
        player.sendMessage(CC.GREEN + " • Você foi teleportado para " + CC.RESET + "(" + x.intValue() + "," + y.intValue() + "," + z.intValue() + ")");
    }

}