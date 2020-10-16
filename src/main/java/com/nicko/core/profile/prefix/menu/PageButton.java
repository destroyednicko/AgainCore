package com.nicko.core.profile.prefix.menu;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.pagination.PaginatedMenu;

import java.util.Arrays;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GREEN + "Próxima página")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Clique para ir até",
                        ChatColor.YELLOW + "a próxima página."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.RED + "Próxima página")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Não há uma",
                        ChatColor.YELLOW + "página seguinte."
                    ))
                    .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GREEN + "Página anterior")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Clique para ir até",
                        ChatColor.YELLOW + "a página anterior."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.RED + "Página anterior")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Não há uma",
                        ChatColor.YELLOW + "página anterior."
                    ))
                    .build();
            }
        }
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                this.menu.modPage(player, this.mod);
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
            }
        }
    }

    private boolean hasNext(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return this.menu.getPages(player) >= pg;
    }

    private boolean hasPrevious(Player player) {
        int pg = this.menu.getPage() + this.mod;
        return pg > 0;
    }

}