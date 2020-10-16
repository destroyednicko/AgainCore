package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.rank.Rank;

@CommandMeta(label = {"rank addpermission", "rank addperm"}, permission = "core.admin.rank", async = true)
public class RankAddPermissionCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("permissão") String permission) {
        if (rank == null) {
            sender.sendMessage(Idioma.RANK_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "Esse ranking já possui essa permissão.");
            return;
        }

        for (Rank childRank : rank.getInherited()) {
            if (childRank.hasPermission(permission)) {
                sender.sendMessage(CC.RED + "Esse ranking está herdando a permissão do grupo " +
                    rank.getColor() + rank.getDisplayName() + ".");
                return;
            }
        }

        rank.getPermissions().add(permission);
        rank.save();
        rank.refresh();

        sender.sendMessage(CC.GREEN + "Permissão adicionada com sucesso ao ranking.");
    }

}
