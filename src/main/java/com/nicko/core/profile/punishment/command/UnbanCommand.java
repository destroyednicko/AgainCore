package com.nicko.core.profile.punishment.command;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import com.nicko.shinigami.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "unban", permission = "core.staff.unban", async = true, options = "s")
public class UnbanCommand {

    public void execute(CommandSender sender, CommandOption option,
                        @CPL("jogador") Profile profile, @CPL("motivo") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) == null && profile.getActivePunishmentByType(PunishmentType.TEMP_BAN) == null) {
            sender.sendMessage(CC.RED + "Esse jogador não está banido.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);
        if (punishment == null) {
            punishment = profile.getActivePunishmentByType(PunishmentType.TEMP_BAN);
        }
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setRemoved(true);
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        if (sender instanceof Player) {
            punishment.setRemovedBy(((Player) sender).getUniqueId());
        }

        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));
    }

}
