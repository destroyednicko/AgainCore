package com.nicko.core.essentials.command;

import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.profile.staff.command.AltsCommand;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.string.CC;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CommandMeta(label = {"whois", "who", "profile"}, permission = "core.whois", async = true)
public class WhoisCommand {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void execute(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        showProfile(player, profile);
    }

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        showProfile(sender, profile);
    }

    private void showProfile(CommandSender sender, Profile profile) {
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.GOLD + "Perfil de " + CC.GREEN + profile.getName());
        sender.sendMessage(CC.GOLD + "UUID: " + CC.RESET + profile.getUuid().toString());
        sender.sendMessage(CC.GOLD + "País: " + CC.RESET + profile.getCountryName() + CC.GRAY + " (" + profile.getCountryCode() + ")");
        sender.sendMessage(CC.GOLD + "Ranking: " + CC.RESET + profile.getActiveRank().getColor() + profile.getActiveRank().getDisplayName());
        sender.sendMessage(CC.GOLD + "Status: " + getProfileStatus(profile));
        sender.sendMessage(CC.GOLD + "Primeira Entrada: " + CC.RESET + new Timestamp(profile.getFirstSeen()).toLocalDateTime().format(formatter));
        sender.sendMessage(CC.GOLD + "Último Servidor: " + CC.RESET + profile.getLastSeenServer() + CC.GRAY + " (" + TimeUtil.getTimeAgo(profile.getLastSeen()) + ")");
        if (sender.hasPermission("core.staff")) {
            List<Profile> alts = Profile.getByIpAddress(profile.getCurrentAddress());
            if (alts.size() > 1) {
                sender.sendMessage(CC.GOLD + "Contas secundárias " + CC.GRAY + "(" + alts.size() + "): " + CC.RESET + AltsCommand.getAltsMessage(alts));
            }
        }
        sender.sendMessage(CC.CHAT_BAR);
    }

    private String getProfileStatus(Profile profile) {
        if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
            return CC.DARK_RED + "Blacklistado";
        } else if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            return CC.DARK_RED + "Banido";
        } else if (profile.isOnline()) {
            return CC.GREEN + "Online";
        }
        return CC.RED + "Offline";
    }

}

