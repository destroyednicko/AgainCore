package com.nicko.core.network;

import com.nicko.hollow.packet.handler.IncomingPacketHandler;
import com.nicko.hollow.packet.listener.PacketListener;
import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.network.event.ReceiveStaffChatEvent;
import com.nicko.core.network.packet.*;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.grant.Grant;
import com.nicko.core.profile.grant.event.GrantAppliedEvent;
import com.nicko.core.profile.grant.event.GrantExpireEvent;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.nicko.core.rank.Rank;
import com.nicko.core.util.string.CC;

import java.util.Objects;

public class NetworkPacketListener implements PacketListener {

    private Core core;

    public NetworkPacketListener(Core core) {
        this.core = core;
    }

    @IncomingPacketHandler
    public void onAddGrant(PacketAddGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            //profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.setActiveGrant(grant);

            new GrantAppliedEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onDeleteGrant(PacketDeleteGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            //profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.setActiveGrant(null);
            profile.checkGrants();

            new GrantExpireEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onAnticheatAlert(PacketAnticheatAlert packet) {
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "IMPLEMENTAR MENSAGEM:");
        Bukkit.broadcastMessage(packet.serialize().toString());
    }

    @IncomingPacketHandler
    public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
        Punishment punishment = packet.getPunishment();
        punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());

        Player player = Bukkit.getPlayer(packet.getTargetUuid());

        if (player != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getPunishments().stream().noneMatch(other -> Objects.equals(other, punishment))) {
                profile.getPunishments().add(punishment);
                punishment.setTargetID(packet.getTargetUuid());
                punishment.save();
                if (punishment.getType().isBan() && !punishment.isRemoved() && !punishment.hasExpired()) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.kickPlayer(punishment.getKickMessage());
                        }
                    }.runTask(Core.get());
                }
            }
        } else {
            Profile profile = Profile.getByUuid(packet.getTargetUuid());
            if (profile.getPunishments().stream().noneMatch(other -> Objects.equals(other.getUuid(), punishment.getUuid()))) {
                if (punishment.isRemoved() && punishment.getType().isBan()) {
                    if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                        profile.getPunishments().remove(profile.getActivePunishmentByType(PunishmentType.BAN));
                    } else {
                        profile.getPunishments().remove(profile.getActivePunishmentByType(PunishmentType.TEMP_BAN));
                    }
                }
                profile.getPunishments().removeIf(other -> punishment.getUuid() == other.getUuid());
                punishment.setTargetID(packet.getTargetUuid());
                profile.getPunishments().add(punishment);

                punishment.save();
                profile.save();
            }

        }
    }

    @IncomingPacketHandler
    public void onRankRefresh(PacketRefreshRank packet) {
        Rank rank = Rank.getRankByUuid(packet.getUuid());

        if (rank == null) {
            rank = new Rank(packet.getUuid(), packet.getName());
        }

        rank.load();

        Core.broadcastOps("&8[&eNetwork&8] &fRanking recarregado " + rank.getDisplayName());
    }

    @IncomingPacketHandler
    public void onRankDelete(PacketDeleteRank packet) {
        Rank rank = Rank.getRanks().remove(packet.getUuid());

        if (rank != null) {
            Core.broadcastOps("&8[&eNetwork&8] &fRanking deletado " + rank.getDisplayName());
        }
    }

    @IncomingPacketHandler
    public void onStaffAuthenticated(PacketStaffAuth packet) {
        if (packet.isSuccess()) {
            CC.sendStaff(Idioma.STAFF_AUTH_SUCCESS.format(packet.getPlayerName(), packet.getServerName()));
            return;
        }

        CC.sendStaff(Idioma.STAFF_AUTH_FAIL.format(packet.getPlayerName(), packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onStaffChat(PacketStaffChat packet) {
        core.getServer().getOnlinePlayers().stream()
            .filter(onlinePlayer -> onlinePlayer.hasPermission("core.staff"))
            .forEach(onlinePlayer -> {
                ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

                core.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    Profile profile = Profile.getProfiles().get(event.getPlayer().getUniqueId());

                    if (profile != null) {
                        onlinePlayer.sendMessage(Idioma.STAFF_CHAT.format(packet.getPlayerName(), packet.getServerName(),
                            packet.getChatMessage()));
                    }
                }
            });
    }

    @IncomingPacketHandler
    public void onPrefixUpdate(PacketPrefixUpdate packet) {
        Prefix.getPrefixByName(packet.getName()).update();
    }

    @IncomingPacketHandler
    public void onPrefixUpdate(PacketPrefixDelete packet) {
        Prefix.getPrefixByName(packet.getName()).delete();
    }

    @IncomingPacketHandler
    public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
        CC.sendStaff(Idioma.STAFF_JOIN_NETWORK.format(packet.getPlayerName(), packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
        CC.sendStaff(Idioma.STAFF_LEAVE_NETWORK.format(packet.getPlayerName(), packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
        CC.sendStaff(Idioma.STAFF_SWITCH_SERVER.format(packet.getPlayerName(), packet.getToServerName()));
    }

    @IncomingPacketHandler
    public void onStaffReport(PacketStaffReport packet) {
        CC.sendStaff(Idioma.STAFF_REPORT_BROADCAST.formatLines(packet.getSentBy(), packet.getAccused(),
            packet.getReason(), packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onStaffRequest(PacketStaffRequest packet) {
        CC.sendStaff(Idioma.STAFF_REQUEST_BROADCAST.formatLines(packet.getSentBy(), packet.getReason(),
            packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onClearGrants(PacketClearPunishments packet) {
        Player player = Bukkit.getPlayer(packet.getUuid());

        if (player != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            //profile.getGrants().clear(); WTF?
        }
    }

    @IncomingPacketHandler
    public void onClearPunishments(PacketClearPunishments packet) {
        Player player = Bukkit.getPlayer(packet.getUuid());

        if (player != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.getPunishments().clear();
        }
    }

}
