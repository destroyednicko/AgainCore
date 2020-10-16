package com.nicko.core.profile.prefix.commands;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"delprefix", "removeprefix"}, permission = "core.prefix.setprefix")
public class UnSetPrefixCommand {
    public void execute(CommandSender player, @CPL("prefixo") String name, @CPL("jogador") Profile target) {
        Prefix prefix = Prefix.getPrefixByName(name);
        if (prefix == null) {
            player.sendMessage(CC.RED + " • Um prefixo com este nome não foi encontrado.");
            return;
        }
        target.getPermissions().remove(prefix.getPermission());
        target.updatePermissions();
        target.setPrefix(null);
        target.save();
        target.updateDisplayName();
        player.sendMessage(CC.GREEN + " • Você atualizou o prefixo de " + CC.RESET + target.getName() + CC.GREEN + " para: " + prefix.getName());
    }
}
