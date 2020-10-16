package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = "rank setprefix", permission = "core.admin.rank", async = true)
public class RankSetPrefixCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("prefixo") String prefix) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "Não existe nenhum ranking com este nome.");
            return;
        }

        rank.setPrefix(CC.translate(prefix));
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "O prefixo do ranking foi atualizado para " + rank.getPrefix() + ".");
    }

}
