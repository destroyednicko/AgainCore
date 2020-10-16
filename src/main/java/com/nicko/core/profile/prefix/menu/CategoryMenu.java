package com.nicko.core.profile.prefix.menu;

import com.google.common.collect.Maps;
import com.nicko.core.CoreAPI;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.prefix.Category;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.pagination.PaginatedMenu;
import com.nicko.core.util.string.CC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CategoryMenu extends PaginatedMenu {

    Category category;
    List<Prefix> prefixes;

    public CategoryMenu(Category category) {
        this.category = category;
        prefixes = Prefix.getPrefixesByCategory(category);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.PINK + category.getName() + " • Prefixos";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        prefixes.forEach(prefix -> buttons.put(buttons.size(), new PrefixButton(prefix)));
        return buttons;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 27;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        int minIndex = (int) ((double) (page - 1) * getMaxItemsPerPage(player));
        int maxIndex = (int) ((double) (page) * getMaxItemsPerPage(player));
        int topIndex = 0;

        HashMap<Integer, Button> buttons = new HashMap<>();

        for (Map.Entry<Integer, Button> entry : getAllPagesButtons(player).entrySet()) {
            int ind = entry.getKey();

            if (ind >= minIndex && ind < maxIndex) {
                ind -= (int) ((double) (getMaxItemsPerPage(player)) * (page - 1)) - 18;
                buttons.put(ind, entry.getValue());

                if (ind > topIndex) {
                    topIndex = ind;
                }
            }
        }

        for (int i = 9; i < 18; i++) {
            buttons.put(i, getPlaceholderButton());
        }

        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        int size = (int) (Math.ceil((highest + 1) / 9D) * 9D);

        for (int i = size; i < size + 9; i++) {
            buttons.put(i, getPlaceholderButton());
        }

        Map<Integer, Button> global = getGlobalButtons(player);

        if (global != null) {
            for (Map.Entry<Integer, Button> gent : global.entrySet()) {
                buttons.put(gent.getKey(), gent.getValue());
            }
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();

        buttons.put(0, new BackButton());
        buttons.put(8, new BackButton());

        buttons.put(3, new PageButton(-1, this));
        buttons.put(4, new PageInfoButton(this));
        buttons.put(5, new PageButton(1, this));


        return buttons;
    }

    public class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE).name("&cVoltar as Categorias").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new PrefixMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    public class PageInfoButton extends Button {

        private PaginatedMenu menu;

        @Override
        public ItemStack getButtonItem(Player player) {
            int pages = menu.getPages(player);

            return new ItemBuilder(Material.CARPET)
                .durability(5)
                .name("&aPágina " + menu.getPage() + "&7/&a" + pages)
                .build();
        }

        @Override
        public boolean shouldCancel(Player player, ClickType clickType) {
            return true;
        }
    }


    @AllArgsConstructor
    public class PrefixButton extends Button {

        private Prefix prefix;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (player.hasPermission(prefix.getPermission())) {
                return new ItemBuilder(Material.NAME_TAG)
                    .name(CC.GREEN + prefix.getName())
                    .lore("&aExibido como:")
                    .lore(prefix.getPrefix() + CoreAPI.getColoredName(player))
                    .lore("")
                    .lore((profile.getPrefix() != null && profile.getPrefix() == prefix) ?
                        "&7Clique para remover este prefixo" : "&aClique para usar este prefixo")
                    .build();
            }
            return new ItemBuilder(Material.NAME_TAG)
                .name(CC.RED + prefix.getName())
                .lore("&aExibido como:")
                .lore(prefix.getPrefix() + CoreAPI.getColoredName(player))
                .lore("")
                .lore("&cPara adquirir prefixos, acesse &eloja.againmc.com")
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (!player.hasPermission(prefix.getPermission())) {
                player.sendMessage(CC.translate("&cVocê não este prefixo, adquira-o em &eloja.againmc.com"));
                return;
            }
            Profile profile = Profile.getByUuid(player.getUniqueId());
            if (profile.getPrefix() != null && profile.getPrefix() == prefix) {
                profile.setPrefix(null);
                profile.updateDisplayName();
                player.closeInventory();
                player.sendMessage(CC.RED + " • Seu prefixo foi removido.");
                return;
            }
            profile.setPrefix(prefix);
            profile.updateDisplayName();
            player.closeInventory();
            player.sendMessage(CC.GREEN + " • Seu novo prefixo é: " + prefix.getPrefix());
        }
    }
}
