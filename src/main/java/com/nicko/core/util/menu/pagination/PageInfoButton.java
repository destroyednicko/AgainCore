package com.nicko.core.util.menu.pagination;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;

@AllArgsConstructor
public class PageInfoButton extends Button {

    private PaginatedMenu menu;

    @Override
    public ItemStack getButtonItem(Player player) {
        int pages = menu.getPages(player);

        return new ItemBuilder(Material.PAPER)
            .name(ChatColor.GOLD + "Informações da página")
            .lore(
                ChatColor.YELLOW + "Você está visualizando a página #" + menu.getPage() + ".",
                ChatColor.YELLOW + (pages == 1 ? "Existe 1 página." : "Existem " + pages + " páginas."),
                "",
                ChatColor.YELLOW + "Clique central para",
                ChatColor.YELLOW + "ver todas as páginas."
            )
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.RIGHT) {
            new ViewAllPagesMenu(this.menu).openMenu(player);
            playNeutral(player);
        }
    }

}
