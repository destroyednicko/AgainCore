package com.nicko.core.profile.staff.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.network.packet.PacketStaffChat;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"staffchat", "sc"}, permission = "core.staff.staffchat")
public class StaffChatCommand {

    public void execute(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.getStaffOptions().setStaffChatModeEnabled(!profile.getStaffOptions().isStaffChatModeEnabled());

        player.sendMessage(profile.getStaffOptions().isStaffChatModeEnabled() ?
            CC.GREEN + " • Você está falando no chat da equipe." : CC.RED + " • Você não está mais falando no chat da equipe.");
    }

    public void execute(Player player, @CPL("mensagem") String message) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        Core.get().getHollow().sendPacket(new PacketStaffChat(profile.getColoredUsername(),
            Bukkit.getServerName(), message));
    }

}
