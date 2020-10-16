package com.nicko.core.profile.option.menu.button;

import com.nicko.core.Idioma;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import com.nicko.core.util.item.ItemBuilder;

public class ScoreboardOptionButton extends ProfileOptionButton {
    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public String getOptionName() {
        return "&9&lScoreboard";
    }

    @Override
    public String getDescription() {
        return "Se habilitado, você verá a scoreboard.";
    }

    @Override
    public String getEnabledOption() {
        return "&aHabilitar";
    }

    @Override
    public String getDisabledOption() {
        return "&cDesabilitar";
    }

    @Override
    public boolean isEnabled(Player player) {
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().scoreboard();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().scoreboard(!profile.getOptions().scoreboard());
        if (profile.getOptions().scoreboard()) {
            player.sendMessage(Idioma.OPTIONS_SCOREBOARD_ENABLE.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_SCOREBOARD_DISABLE.format());
        }
        player.closeInventory();
    }
}
