package com.nicko.core.profile.permissions;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "perms", permission = "core.perms")
public class PermissionsCommand {

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.translate("&7/perms add <jogador> <permissão>"));
        sender.sendMessage(CC.translate("&7/perms remover <jogador> <permissão>"));
    }

    @CommandMeta(label = "add", permission = "core.perms")
    public class AddCommand extends PermissionsCommand {
        public void execute(CommandSender sender, @CPL("jogador") Profile target, @CPL("permissão") String permission) {
            target.getPermissions().add(permission);
            target.updatePermissions();
            sender.sendMessage(CC.translate("&a • Permissão adicionada com sucesso."));
        }
    }

    @CommandMeta(label = "remover", permission = "core.perms")
    public class RemoveCommand extends PermissionsCommand {
        public void execute(CommandSender sender, @CPL("jogador") Profile target, @CPL("permissão") String permission) {
            target.getPermissions().remove(permission);
            target.updatePermissions();
            sender.sendMessage(CC.translate("&c • Permissão removida com sucesso."));
        }
    }

}
