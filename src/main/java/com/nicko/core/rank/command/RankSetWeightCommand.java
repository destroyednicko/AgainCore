package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.rank.Rank;

@CommandMeta(label = "rank prioridade", permission = "core.admin.rank", async = true)
public class RankSetWeightCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("prioridade") String weight) {
        if (rank == null) {
            sender.sendMessage(CC.RED + "Não existe um ranking com este nome.");
            return;
        }

        try {
            Integer.parseInt(weight);
        } catch (Exception e) {
            sender.sendMessage(CC.RED + "Ops! Número inválido.");
            return;
        }

        rank.setPriority(Integer.parseInt(weight));
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "A prioridade do ranking foi atualizada!");
    }

}
