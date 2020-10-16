package com.nicko.core.profile.punishment.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import com.nicko.shinigami.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "mute", permission = "core.staff.mute", async = true, options = "s")
public class MuteCommand {

    public void execute(CommandSender sender, CommandOption option,
                        @CPL("jogador") Profile profile, @CPL("duração") Duration duration, @CPL("motivo") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
            sender.sendMessage(CC.RED + "Esse jogador já está silenciado.");
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "A duração é inválida.");
            sender.sendMessage(CC.RED + "Exemplo: [perm/1y1m1w1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.MUTE, System.currentTimeMillis(),
            reason, duration.getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        Player player = profile.getPlayer();

        if (player != null) {
            String senderName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";
            player.sendMessage(CC.RED + "Você foi " + punishment.getContext() + " por " +
                senderName + CC.RED + ".");
            player.sendMessage(CC.RED + "A razão da punição é: " + CC.WHITE +
                punishment.getAddedReason());

            if (!punishment.isPermanent()) {
                player.sendMessage(CC.RED + "O silenciamento expira em " + CC.WHITE +
                    punishment.getTimeRemaining() + CC.RED + ".");
            }
        }

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));
    }

}
