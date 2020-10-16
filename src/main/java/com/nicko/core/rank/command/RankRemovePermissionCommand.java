package com.nicko.core.rank.command;

import com.nicko.core.util.string.CC;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.Idioma;
import com.nicko.core.rank.Rank;

@CommandMeta(label = {"rank removepermission", "rank removeperm", "rank deleteperm", "rank delperm"},
    permission = "core.admin.rank",
    async = true)
public class RankRemovePermissionCommand {

    public void execute(CommandSender sender, @CPL("rank") Rank rank, @CPL("permissão") String permission) {
        if (rank == null) {
            sender.sendMessage(Idioma.RANK_NOT_FOUND.format());
            return;
        }

        permission = permission.toLowerCase().trim();

        if (!rank.getPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + "Este ranking não tem esta permissão.");
        } else {
            rank.getPermissions().remove(permission);
            rank.save();
            rank.refresh();

            sender.sendMessage(CC.GREEN + "Permissão " + permission + " removida com sucesso.");
        }
    }

}
