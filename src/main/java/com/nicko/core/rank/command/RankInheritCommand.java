package com.nicko.core.rank.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = "rank inherit", permission = "core.admin.rank", async = true)
public class RankInheritCommand {

    public void execute(CommandSender sender, @CPL("pai") Rank parent, @CPL("herdeiro") Rank child) {
        if (parent == null) {
            sender.sendMessage(ChatColor.RED + "Não existe nenhum ranking com esse nome! (Pai).");
            return;
        }

        if (child == null) {
            sender.sendMessage(ChatColor.RED + "Não existe nenhum ranking com esse nome! (Herdeiro).");
            return;
        }

        if (parent.canInherit(child)) {
            parent.getInherited().add(child);
            parent.save();
            parent.refresh();

            sender.sendMessage(ChatColor.GREEN + "O ranking " + child.getDisplayName() +
                " agora é parente do ranking " + parent.getDisplayName() + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "O ranking " + child.getDisplayName() +
                    " não pode ser herdeiro do ranking " + parent.getDisplayName() + ".");
        }
    }

}
