package com.nicko.core.profile.option.menu.button;

import com.nicko.core.Idioma;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.item.ItemBuilder;

public class TipsOptionButton extends ProfileOptionButton {
    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.SIGN).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.SIGN).build();
    }

    @Override
    public String getOptionName() {
        return "&7&lAnúncios Globais";
    }

    @Override
    public String getDescription() {
        return "Se habilitado, você verá os anúncios globais.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().tipsAnnounce();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().tipsAnnounce(!profile.getOptions().tipsAnnounce());
        if (profile.getOptions().tipsAnnounce()) {
            player.sendMessage(Idioma.OPTIONS_TIPS_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_TIPS_DISABLE.format());
        }
        player.closeInventory();
    }
}
