package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.core.util.string.TextSplitter;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.rank.Rank;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CommandMeta(label = "rank info", permission = "core.admin.rank", async = true)
public class RankInfoCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank) {
        if (rank == null) {
            sender.sendMessage(Idioma.RANK_NOT_FOUND.format());
        } else {
            List<String> toSend = new ArrayList<>();
            toSend.add(CC.CHAT_BAR);
            toSend.add(ChatColor.GOLD + " • Informações do Ranking " + ChatColor.GRAY + "(" + ChatColor.RESET +
                rank.getColor() + rank.getDisplayName() + ChatColor.GRAY + ")");

            toSend.add(ChatColor.GRAY + "Peso: " + ChatColor.RESET + rank.getPriority());
            toSend.add(ChatColor.GRAY + "Prefixo: " + ChatColor.RESET + rank.getPrefix());

            List<String> permissions = rank.getAllPermissions();

            toSend.add("");
            toSend.add(ChatColor.GRAY + "Permissões: " + ChatColor.RESET + "(" + permissions.size() + ")");

            if (!permissions.isEmpty()) {
                toSend.addAll(TextSplitter.split(46, StringUtils.join(permissions, " "), "", ", "));
            }

            List<Rank> inherited = rank.getInherited();

            toSend.add("");
            toSend.add(ChatColor.GRAY + "Herdeiros: " + ChatColor.RESET + "(" + inherited.size() + ")");

            if (!rank.getInherited().isEmpty()) {
                List<String> rankNames = rank.getInherited()
                    .stream()
                    .map(inheritedRank -> inheritedRank.getColor() + inheritedRank.getDisplayName())
                    .collect(Collectors.toList());

                toSend.addAll(TextSplitter.split(46, StringUtils.join(rankNames, " "), "", ", "));
            }

            toSend.add(CC.CHAT_BAR);

            for (String line : toSend) {
                sender.sendMessage(line);
            }
        }
    }

}
