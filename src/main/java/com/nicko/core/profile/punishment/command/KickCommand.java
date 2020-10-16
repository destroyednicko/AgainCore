package com.nicko.core.profile.punishment.command;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import com.nicko.shinigami.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "kick", permission = "core.staff.kick", async = true, options = "s")
public class KickCommand {

    public void execute(CommandSender sender, CommandOption option,
                        @CPL("jogador") Player player, @CPL("motivo") String reason) {
        if (player == null) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        Profile profile = Profile.getProfiles().get(player.getUniqueId());

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.KICK, System.currentTimeMillis(),
            reason, -1, Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        punishment.setTargetID(player.getUniqueId());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(Core.get());
    }

}
