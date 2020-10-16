package com.nicko.core.profile.option.menu.button;

import com.nicko.core.Idioma;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import com.nicko.core.util.item.ItemBuilder;

public class PrivateChatOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&c&lMensagens Privadas";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.NAME_TAG).build();
    }

    @Override
    public String getDescription() {
        return "Se habilitado, você irá receber mensagens privadas.";
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
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().receivingNewConversations();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().receivingNewConversations(!profile.getOptions().receivingNewConversations());
        if (profile.getOptions().receivingNewConversations()) {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGES_ENABLED.format());
        } else {
            player.sendMessage(Idioma.OPTIONS_PRIVATE_MESSAGES_DISABLED.format());
        }
        player.closeInventory();
    }

}
