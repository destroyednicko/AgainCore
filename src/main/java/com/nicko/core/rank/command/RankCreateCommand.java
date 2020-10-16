package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = {"rank create", "rank criar"}, permission = "core.admin.rank", async = true)
public class RankCreateCommand {

    public void execute(CommandSender sender, @CPL("rank") String name) {
        if (Rank.getRankByDisplayName(name) != null) {
            sender.sendMessage(CC.RED + "Já há um ranking existente com este nome.");
            return;
        }

        Rank rank = new Rank(name);
        rank.save();

        sender.sendMessage(CC.GREEN + "Você criou o ranking " + name + ".");
    }

}
