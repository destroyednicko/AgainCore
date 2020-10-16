package com.nicko.core.util.player;

import com.nicko.core.Core;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTagHandler {

    private static Scoreboard createScoreboard(Player player) {
        Scoreboard mainScoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
        if (player.getScoreboard() != mainScoreboard) {
            return player.getScoreboard();
        }

        Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Runnable task = () -> player.setScoreboard(scoreboard);
        if (Bukkit.getServer().isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(Core.get(), task);
        }

        return scoreboard;
    }

    public static void addToTeam(Player player, Player target, String prefix, boolean showHealth) {

        Scoreboard scoreboard = createScoreboard(player);

        Team team = scoreboard.getTeam(prefix);

        if (team == null) {
            team = scoreboard.registerNewTeam(prefix);
            team.setPrefix(prefix);
        }

        if (!team.hasEntry(target.getName())) {
            removeFromTeams(player, target, prefix);

            team.addEntry(target.getName());

            if (showHealth) {
                Objective objective = scoreboard.getObjective(DisplaySlot.BELOW_NAME);

                if (objective == null) {
                    objective = scoreboard.registerNewObjective("showhealth", "health");
                }

                objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
                objective.setDisplayName(ChatColor.RED + StringEscapeUtils.unescapeJava("\u2764"));
                objective.getScore(target.getName()).setScore((int) Math.floor(target.getHealth() / 2));
            }
        }
    }

    public static void removeFromTeams(Player player, Player other, String teamName) {
        if (player != null && other != null && !player.equals(other)) {
            Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

            if (objective != null) {
                objective.unregister();
            }


            Team team = player.getScoreboard().getTeam(teamName);

            if (team != null) {
                team.removeEntry(other.getName());
            }
        }
    }

    private static Team getExistingOrCreateNewTeam(String string, Scoreboard scoreboard, ChatColor prefix) {
        Team toReturn = scoreboard.getTeam(string);

        if (toReturn == null) {
            toReturn = scoreboard.registerNewTeam(string);
            toReturn.setPrefix(prefix + "");
        }

        return toReturn;
    }

    public static void removeHealthDisplay(Player player) {
        if (player == null) {
            return;
        }

        Objective objective = player.getScoreboard().getObjective(DisplaySlot.BELOW_NAME);

        if (objective != null) {
            objective.unregister();
        }
    }

}