package com.nicko.core.essentials.command;

import com.nicko.core.CoreAPI;
import com.nicko.shinigami.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.rank.Rank;

import java.util.ArrayList;
import java.util.List;

@CommandMeta(label = {"listar", "list", "who", "ls"})
public class ListCommand {

    public void execute(CommandSender sender) {
        List<Player> sortedPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        sortedPlayers.sort((o1, o2) -> {
            Profile p1 = Profile.getByUuid(o1.getUniqueId());
            Profile p2 = Profile.getByUuid(o2.getUniqueId());
            return p2.getActiveRank().getPriority() - p1.getActiveRank().getPriority();
        });

        List<String> playerNames = new ArrayList<>();

        for (Player player : sortedPlayers) {
            playerNames.add(CoreAPI.getColoredName(player));
        }

        List<Rank> sortedRanks = new ArrayList<>(Rank.getRanks().values());
        sortedRanks.sort((o1, o2) -> o2.getPriority() - o1.getPriority());

        List<String> rankNames = new ArrayList<>();

        for (Rank rank : sortedRanks) {
            rankNames.add(rank.getColor() + rank.getDisplayName());
        }

        sender.sendMessage(StringUtils.join(rankNames, ChatColor.WHITE + ", "));
        sender.sendMessage("(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "): " +
            StringUtils.join(playerNames, ChatColor.WHITE + ", "));
    }

}
