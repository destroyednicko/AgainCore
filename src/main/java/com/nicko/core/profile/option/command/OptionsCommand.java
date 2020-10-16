package com.nicko.core.profile.option.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.profile.option.menu.ProfileOptionsMenu;

@CommandMeta(label = {"settings"})
public class OptionsCommand {

    public void execute(Player player) {
        new ProfileOptionsMenu().openMenu(player);
    }

}
