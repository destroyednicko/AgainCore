package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"fly", "voar"}, permission = "core.fly")
public class FlyCommand {

    public void excute(Player player) {
        player.setAllowFlight(!player.getAllowFlight());
        player.sendMessage(CC.translate("&e • Modo de voo " + (player.getAllowFlight() ? "&ahabilitado" : "&cdesabilitado")));
    }

    public void excute(Player player, Player target) {
        target.setAllowFlight(!target.getAllowFlight());
        target.sendMessage(CC.translate("&e • Modo de voo " + (target.getAllowFlight() ? "&ahabilitado" : "&cdesabilitado")));
        player.sendMessage(CC.translate("&e • Modo de voo de " + target.getName() +
            (target.getAllowFlight() ? " &ahabilitado" : " &cdesabilitado")));
    }

}
