package com.nicko.core.profile.option.menu.button;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.Idioma;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import com.nicko.core.util.item.ItemBuilder;

public class PrivateChatSoundsOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&e&lSons";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.ANVIL).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.ANVIL).build();
    }

    @Override
    public String getDescription() {
        return "Se habilitado, um som irá tocar ao receber uma mensagem privada.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().playingMessageSounds();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().playingMessageSounds(!profile.getOptions().playingMessageSounds());
        if (profile.getOptions().playingMessageSounds()) {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGE_SOUND_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGE_SOUND_DISABLED.format());
        }
        player.closeInventory();
    }

}
