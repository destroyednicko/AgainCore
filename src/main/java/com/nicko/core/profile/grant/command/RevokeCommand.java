package com.nicko.core.profile.grant.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"revoke", "revogar"}, async = true, permission = "core.grants.revoke")
public class RevokeCommand {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        profile.setActiveGrant(null);
        profile.checkGrants();
        if (profile.getPlayer() != null)
            profile.getPlayer().sendMessage(CC.YELLOW + " • Seu ranking foi revogado para 'padrão'.");
        profile.save();
        sender.sendMessage(CC.YELLOW + profile.getName() + " teve o ranking revogado para 'padrão'.");
    }

}
