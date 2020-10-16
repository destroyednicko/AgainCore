package com.nicko.core.util.menu.pagination;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;

import java.util.Arrays;

@AllArgsConstructor
public class PageButton extends Button {

    private int mod;
    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.mod > 0) {
            if (hasNext(player)) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                    .name(ChatColor.GREEN + "Próxima Página")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Clique para ir até",
                        ChatColor.YELLOW + "a próxima página."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GRAY + "Próxima Página")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "A próxima página",
                        ChatColor.YELLOW + "não está disponível."
                    ))
                    .build();
            }
        } else {
            if (hasPrevious(player)) {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                    .name(ChatColor.GREEN + "Página Anterior")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "Clique para ir até",
                        ChatColor.YELLOW + "a pagina anterior."
                    ))
                    .build();
            } else {
                return new ItemBuilder(Material.LEVER)
                    .name(ChatColor.GRAY + "Página Anterior")
                    .lore(Arrays.asList(
                        ChatColor.YELLOW + "A página anterior",
                        ChatColor.YELLOW + "não está disponível."
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
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
            }
        } else {
            if (hasPrevious(player)) {
                this.menu.modPage(player, this.mod);
                Button.playNeutral(player);
            } else {
                Button.playFail(player);
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
