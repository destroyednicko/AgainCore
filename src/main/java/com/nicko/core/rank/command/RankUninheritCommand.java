package com.nicko.core.rank.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = "rank delherdeiro", permission = "core.admin.rank", async = true)
public class RankUninheritCommand {

    public void execute(CommandSender sender, @CPL("pai") Rank parent, @CPL("herdeiro") Rank child) {
        if (parent == null) {
            sender.sendMessage(ChatColor.RED + "Não existe nenhum ranking com este nome! (Pai).");
            return;
        }

        if (child == null) {
            sender.sendMessage(ChatColor.RED + "Não existe nenhum ranking com este nome! (Herdeiro).");
            return;
        }

        if (parent.getInherited().remove(child)) {
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "O ranking " + child.getDisplayName() +
                " não é mais herdeiro do ranking " + parent.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "O ranking " + child.getDisplayName() +
                    " não é herdeiro do ranking " + parent.getDisplayName() + ".");
        }
    }

}
