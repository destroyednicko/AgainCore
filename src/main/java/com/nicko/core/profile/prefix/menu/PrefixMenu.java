package com.nicko.core.profile.prefix.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.prefix.Category;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;
import com.nicko.core.util.menu.button.DisplayButton;
import com.nicko.core.util.string.CC;

import java.util.Map;

public class PrefixMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Tags de Chat";
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        for (int i = 0; i < 27; i++) {
            buttons.put(i, new DisplayButton(
                new ItemBuilder(Material.STAINED_GLASS_PANE).durability(0).build(),
                true));
        }

        int i = 8;
        for (Category category : Category.values()) {
            buttons.put(i += 2, new CategoryButton(category));
        }

        return buttons;
    }


    @AllArgsConstructor
    public class CategoryButton extends Button {

        private Category category;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.NAME_TAG).name(CC.PINK + category.getName() + " • Prefixos").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new CategoryMenu(category).openMenu(player);
        }
    }
}
