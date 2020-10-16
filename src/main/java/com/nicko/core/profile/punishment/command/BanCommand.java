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
import com.nicko.core.profile.punishment.menu.BanMenu;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "ban", permission = "core.staff.ban", async = true, options = "s")
public class BanCommand {

    public void execute(Player sender) {
        sender.sendMessage(CC.GRAY + "/ban <jogador>");
    }

    public void execute(Player sender, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "Esse jogador já está banido.");
            return;
        }


        new BanMenu(profile).openMenu(sender);

        /*
        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "A duração não é válida.");
            sender.sendMessage(CC.RED + "Exemplo: [perm/1y, 1m, 1w, 1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
                reason, duration.getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
                profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Core.get());
        }*/
    }

    public void execute(CommandSender sender
        , CommandOption option, @CPL("jogador") Profile profile,
                        @CPL("duração") Duration duration, @CPL("nituvi") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "Esse jogador já está banido.");
            return;
        }


        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "A duração é inválida.");
            sender.sendMessage(CC.RED + "Exemplo: [perm/1y, 1m, 1w, 1d]");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, duration.getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Core.get());
        }
    }

    public void execute(CommandSender sender
        , CommandOption option, @CPL("jogador") Profile profile, @CPL("motivo") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "Esse jogador já está banido.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, new Duration(Long.MAX_VALUE).getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), option != null, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Core.get());
        }
    }

    public void execute(CommandSender sender, @CPL("jogador") Profile profile, @CPL("motivo") String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(CC.RED + "Esse jogador já está banido.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
            .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
            reason, new Duration(Long.MAX_VALUE).getValue(), Bukkit.getServerName());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }
        punishment.setTargetID(profile.getUuid());
        punishment.save();
        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
            profile.getColoredUsername(), profile.getUuid(), false, Bukkit.getServerName()));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Core.get());
        }
    }

}
