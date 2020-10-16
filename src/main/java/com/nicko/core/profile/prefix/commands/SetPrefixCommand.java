package com.nicko.core.profile.prefix.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "setprefix", permission = "core.prefix.setprefix")
public class SetPrefixCommand {
    public void execute(CommandSender player, @CPL("prefixo") String name, @CPL("jogador") Profile target) {
        Prefix prefix = Prefix.getPrefixByName(name);
        if (prefix == null) {
            player.sendMessage(CC.RED + " • Um prefixo com este nome não foi encontrado.");
            return;
        }
        target.getPermissions().add(prefix.getPermission());
        target.updatePermissions();
        target.setPrefix(prefix);
        target.save();
        target.updateDisplayName();
        player.sendMessage(CC.GREEN + " • Você atualizou o prefixo de " + CC.RESET + target.getName() + CC.GREEN + " para: " + prefix.getName());
    }
}
