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
import com.nicko.core.network.packet.PacketStaffRequest;

@CommandMeta(label = {"request", "suporte", "helpop"}, async = true)
public class RequestCommand {

    public void execute(Player player, @CPL("motivo") String reason) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(ChatColor.RED + " • Você está solicitando ajuda rápido de mais, tente novamente mais tarde.");
            return;
        }

        profile.setRequestCooldown(new ProfileCooldown(120_000L));
        player.sendMessage(Idioma.STAFF_REQUEST_SUBMITTED.format());

        Core.get().getHollow().sendPacket(new PacketStaffRequest(
            profile.getColoredUsername(),
            reason,
            Bukkit.getServerName()
        ));
    }

}
