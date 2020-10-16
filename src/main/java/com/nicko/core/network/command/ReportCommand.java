package com.nicko.core.network.command;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.ProfileCooldown;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import com.nicko.core.CoreAPI;
import com.nicko.core.network.packet.PacketStaffReport;

@CommandMeta(label = {"report", "reportar", "denunciar"}, async = true)
public class ReportCommand {

    public void execute(Player player, @CPL("jogador") Player target, @CPL("motivo") String reason) {
        if (target == null) {
            player.sendMessage(Idioma.PLAYER_NOT_FOUND.format());
            return;
        }

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + " • Você não pode reportar a si mesmo.");
            return;
        }

        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(ChatColor.RED + " • Você está reportando muito rápido. Tente novamente mais tarde.");
            return;
        }

        Core.get().getHollow().sendPacket(new PacketStaffReport(
            profile.getColoredUsername(),
            CoreAPI.getColoredName(target),
            reason,
            Bukkit.getServerName()
        ));

        profile.setRequestCooldown(new ProfileCooldown(120_000L));
        player.sendMessage(Idioma.STAFF_REQUEST_SUBMITTED.format());
    }

}
