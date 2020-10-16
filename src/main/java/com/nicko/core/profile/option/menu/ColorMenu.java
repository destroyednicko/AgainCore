package com.nicko.core.profile.option.menu;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.BukkitUtil;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;
import com.nicko.core.util.string.CC;

import java.util.HashMap;
import java.util.Map;

public class ColorMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Menu de Cores";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Map<ChatColor, DyeColor> colors = new HashMap<>(BukkitUtil.CHAT_DYE_COLOUR_MAP);

        colors.forEach((chatColor, dyeColor) -> buttons.put(buttons.size(), new ColorButton(chatColor, dyeColor)));

        return buttons;
    }

    @AllArgsConstructor
    public class ColorButton extends Button {

        ChatColor color;
        DyeColor dyeColor;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByPlayer(player);
            return new ItemBuilder(Material.WOOL)
                .dyeColor(dyeColor)
                .name(color + StringUtils.capitalize(color.name().toLowerCase().replace("_", " ")))
                .lore((color == profile.getOptions().color() ? "&aA cor já está selecionada" : "&7Clique para selecionar a cor") + '!')
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            Profile profile = Profile.getByPlayer(player);
            profile.getOptions().color(color);
            player.sendMessage(CC.translate("&a •Suas mensagens privadas serão exibidas em " + color + StringUtils.capitalize(color.name().toLowerCase().replace("_", " ")) + "&a."));
            player.closeInventory();

        }
    }
}
