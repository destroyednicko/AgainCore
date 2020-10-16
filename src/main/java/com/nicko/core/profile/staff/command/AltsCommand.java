package com.nicko.core.profile.staff.command;

import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.string.CC;

import java.util.ArrayList;
import java.util.List;

@CommandMeta(label = "alts", async = true, permission = "core.staff.alts")
public class AltsCommand {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        // Procura por IP, então exibe se o jogador estiver 'offline'
        List<Profile> alts = Profile.getByIpAddress(profile.getCurrentAddress());
        if (alts.size() <= 1) {
            sender.sendMessage(CC.RED + " • Este jogador não tem contas secundárias conhecidas.");
        } else {
            sender.sendMessage(CC.GOLD + " • Contas alternativas de: " + profile.getName() + CC.GRAY + " (" + alts.size() + "): " + CC.RESET + getAltsMessage(alts));
        }
    }

    public static String getAltsMessage(List<Profile> alts) {
        List<String> messages = new ArrayList<>(alts.size());
        for (Profile altProfile : alts) {
            StringBuilder sb = new StringBuilder(CC.YELLOW + CC.BOLD + " * ");
            if (altProfile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                sb.append(CC.DARK_RED);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                sb.append(CC.RED);
            } else if (altProfile.isOnline()) {
                sb.append(CC.GREEN);
            } else {
                sb.append(CC.WHITE);
            }
            sb.append(altProfile.getName());
            sb.append(CC.GRAY)
                .append(" (")
                .append(altProfile.getLastSeenServer())
                .append(" ")
                .append(TimeUtil.getTimeAgo(altProfile.getLastSeen()))
                .append(")");
            messages.add(sb.toString());
        }
        return "\n" + StringUtils.join(messages, CC.RESET + "\n");
    }

}
