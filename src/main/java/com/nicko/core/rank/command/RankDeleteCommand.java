package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.rank.Rank;

@CommandMeta(label = {"rank delete", "rank deletar", "rank apagar"}, permission = "core.admin.rank", async = true)
public class RankDeleteCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank) {
        if (rank == null) {
            sender.sendMessage(Idioma.RANK_NOT_FOUND.format());
            return;
        }

        sender.sendMessage(CC.GREEN + "O ranking " + rank + " foi deletado.");

        rank.delete();

    }

}