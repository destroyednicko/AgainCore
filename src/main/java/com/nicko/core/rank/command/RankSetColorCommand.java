package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = {"rank setcor", "rank setcolor"}, permission = "core.admin.rank", async = true)
public class RankSetColorCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("cor") String chatColor) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "Não existe nenhum ranking com este nome.");
            return;
        }

        if (chatColor == null) {
            sender.sendMessage(CC.RED + "Essa cor não é válida.");
            return;
        }

        rank.setColor(chatColor);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "A cor do ranking " + rank.getDisplayName() +
                " foi atualizada para " + rank.getColor() + ".");
    }

}
