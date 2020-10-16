package com.nicko.core.util.menu.button;

import com.nicko.core.util.string.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;

import java.util.Arrays;

@AllArgsConstructor
public class BackButton extends Button {

    private Menu back;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
            .name(CC.RED + CC.BOLD + "Voltar")
            .lore(Arrays.asList(
                CC.RED + "Clique para voltar",
                CC.RED + "ao menu anterior.")
            )
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Button.playNeutral(player);
        back.openMenu(player);
    }

}
