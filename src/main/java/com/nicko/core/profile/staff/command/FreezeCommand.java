package com.nicko.core.profile.staff.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Idioma;
import com.nicko.core.network.event.FreezePlayerEvent;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.player.PlayerUtil;
import com.nicko.core.util.string.CC;

import java.util.List;

@CommandMeta(label = {"freeze", "ss", "screenshare"}, async = true, permission = "core.staff.freeze")
public class FreezeCommand {

    public void execute(CommandSender sender, @CPL("jogador") Player target) {
        Profile profile = Profile.getByUuid(target.getUniqueId());
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        Profile staff = Profile.getByUsername(sender.getName());
        if (staff == null || !staff.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile == staff) {
            sender.sendMessage(CC.RED + " • Você não pode congelar a si mesmo.");
            return;
        }

        if (profile.isFrozen()) {
            String format = Idioma.STAFF_UNFREEZE_MESSAGE.format(profile.getColoredUsername(), staff.getColoredUsername());
            CC.sendStaff(format);
            profile.setFrozen(false);
            PlayerUtil.allowMovement(target);
            new FreezePlayerEvent(sender, target, false).call();
            return;
        }

        profile.setFrozen(true);
        String format = Idioma.STAFF_FREEZE_MESSAGE.format(profile.getColoredUsername(), staff.getColoredUsername());
        CC.sendStaff(format);
        PlayerUtil.denyMovement(target);
        new FreezePlayerEvent(sender, target, true).call();
        List<String> messages = Idioma.STAFF_FREEZE.formatLines();
        for (String message : messages) {
            target.sendMessage(message);
        }
    }

}