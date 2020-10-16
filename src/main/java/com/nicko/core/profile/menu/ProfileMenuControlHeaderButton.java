package com.nicko.core.profile.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import com.nicko.core.cache.RedisPlayerData;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.string.CC;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ProfileMenuControlHeaderButton extends Button {

    private Profile profile;
    private RedisPlayerData playerData;

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();
        skullMeta.setOwner(profile.getName());
        itemStack.setItemMeta(skullMeta);

        List<String> lore = new ArrayList<>();
        lore.add(CC.MENU_BAR);

        if (playerData == null) {
            lore.add("&cDados indisponíveis");
        } else {
            if (playerData.getLastAction() == RedisPlayerData.LastAction.JOINING_SERVER) {
                lore.add("&aAtualmente Online");
            } else if (playerData.getLastAction() == RedisPlayerData.LastAction.LEAVING_SERVER) {
                lore.add("&cVisto por Último");
            }

            lore.add("&3Servidor: &e" + playerData.getLastSeenServer());
            lore.add("&3Atualizado: &e" + playerData.getTimeAgo());
        }

        lore.add(CC.MENU_BAR);

        return new ItemBuilder(itemStack)
            .name("&3" + profile.getName())
            .lore(lore)
            .build();
    }

}
