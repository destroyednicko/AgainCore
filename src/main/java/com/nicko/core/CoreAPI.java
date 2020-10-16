package com.nicko.core;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.nicko.core.network.packet.PacketAnticheatAlert;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.rank.Rank;
import com.nicko.core.util.cooldown.Cooldown;
import com.nicko.core.util.string.CC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CoreAPI {

    /**
     * Obtem a cor do {@link Player}
     *
     * @param player O {@link Player} para obter a cor
     * @return O {@link ChatColor} baseado na cor do {@link Rank} atual
     */
    public static String getColorOfPlayer(Player player) {
        Profile profile = Profile.getByPlayer(player);
        return profile == null ? ChatColor.WHITE.toString() : profile.getActiveRank().getColor();
    }

    /**
     * Obtem o 'username' colorido do {@link Player}
     *
     * @param player O {@link Player} para obter a cor
     * @return O 'username' colorido do {@code player} baseado no {@link Rank}
     */
    public static String getColoredName(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return (profile == null ? ChatColor.WHITE : profile.getActiveRank().getColor()) + player.getName();
    }

    /**
     * Obtem o {@link Rank} ativo do {@link Player}
     *
     * @param player O {@link Player} para obter o rank
     * @return O {@link Rank} ativo do {@code player}
     */
    public static Rank getRankOfPlayer(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile == null ? Rank.getDefaultRank() : profile.getActiveRank();
    }

    /**
     * Obtem o estado atual do 'staffMode' do jogador
     *
     * @param player O {@link Player} para verificar se está no 'staffMode'
     * @return se o {@code player} está ou não no 'staffMode'
     */
    public static boolean isInStaffMode(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile != null && player.hasPermission("core.staff") && profile.getStaffOptions().isStaffModeEnabled();
    }

    /**
     * Obtem o estado atual do 'vanish' do player
     *
     * @param player The {@link Player} to check if its in staff mode
     * @return weather the {@code player} is in staff mode or not
     */
    public static boolean isVanish(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        return profile != null && profile.getStaffOptions().isVanish();
    }

    /**
     * Bane um jogador usando o sistema interno de punições
     *
     * @param player   O {@link Player} para punir
     * @param reason   O motivo da punição
     * @param duration A duração do ban
     */
    public static void banPlayer(Player player, String reason, long duration) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        if (profile == null) return;

        Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(), reason, duration, Bukkit.getServerName());
        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, CC.DARK_RED + "Console", player.getDisplayName(), player.getUniqueId(), false, Bukkit.getServerName()));

        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(Core.get());
    }

    /**
     * Anuncia um 'flag' do Anticheat
     *
     * @param player O {@link Player} que 'flaggou'
     * @param flag   O flag que o {@code player} apontou
     */
    public static void broadcastFlag(Player player, String flag) {
        Core.get().getHollow().sendPacket(new PacketAnticheatAlert(player.getName(), flag, ((CraftPlayer) player).getHandle().ping, Bukkit.getServerName()));
    }

    public static Profile getProfile(Player player) {
        return Profile.getByPlayer(player);
    }

    public static Profile getProfile(UUID uuid) {
        return Profile.getByUuid(uuid);
    }

    /**
     * Envia o player para o servidor no 'Bungee'
     *
     * @param player
     * @param server Nome do servidor 'Bungee'
     */
    public static void sendToServer(Player player, String server) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("ConnectOther");
        output.writeUTF(player.getName());
        output.writeUTF(server);
        player.sendPluginMessage(Core.get(), "BungeeCord", output.toByteArray());
    }

    /**
     * Envia uma mensagem no 'Staffchat'
     *
     * @param message
     */
    public static void sendStaff(String message) {
        CC.sendStaff(message);
    }


    // Resfriamento
    public static Cooldown getCooldown(String name) {
        return Cooldown.getCooldownMap().get(name);
    }

    public static List<Cooldown> getPlayerCooldowns(Player player) {
        return Cooldown.getCooldownMap()
            .values()
            .stream()
            .filter(check -> check.getLongMap().containsKey(player.getUniqueId()))
            .collect(Collectors.toList());
    }


}
