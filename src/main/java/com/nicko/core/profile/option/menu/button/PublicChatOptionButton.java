package com.nicko.core.profile.option.menu.button;

import com.nicko.core.Idioma;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import com.nicko.core.util.item.ItemBuilder;

public class PublicChatOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&a&lChat Global";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.BOOK_AND_QUILL).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.BOOK_AND_QUILL).build();
    }

    @Override
    public String getDescription() {
        return "Se habilitado, você irá receber mensagens do chat global.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().publicChatEnabled();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().publicChatEnabled(!profile.getOptions().publicChatEnabled());
        if (profile.getOptions().publicChatEnabled()) {
            player.sendMessage(Idioma.OPTIONS_GLOBAL_CHAT_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_GLOBAL_CHAT_DISABLED.format());
        }
        player.closeInventory();
    }

}
