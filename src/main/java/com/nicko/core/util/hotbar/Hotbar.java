package com.nicko.core.util.hotbar;

import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.Core;
import com.nicko.core.util.item.ItemBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
public enum Hotbar {

    STAFFMODE(
        (profile -> profile.getStaffOptions().isStaffModeEnabled()), //Filtro

        new HotbarItem(0, new ItemBuilder(Material.COMPASS).name("&eCompasso").build(),
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK),

        new HotbarItem(1, new ItemBuilder(Material.BOOK).name("&9Inspecionar Inventário").build())
            .addInteractEntity((player, entity) -> {
                if (entity instanceof Player && Core.get().getMainConfig().getBoolean("STAFF.MOD_COMMAND")) {
                    Player target = (Player) entity;
                    player.chat("/invsee " + target.getName());
                }
            }),

        new HotbarItem(2, new ItemBuilder(Material.WOOD_AXE).name("&dWorld Edit").build()),

        new HotbarItem(4, new ItemStack(Material.CARPET)),

        new HotbarItem(6, new ItemBuilder(Material.WATCH).name("&eTeleporte Aleatório").build(),
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
            .addInteract((player) -> {
                List<Player> randomList = Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(online -> !Profile.getByPlayer(online).getStaffOptions().isStaffModeEnabled())
                    .collect(Collectors.toList());

                if (randomList.isEmpty()) {
                    player.sendMessage(CC.RED + "Não há jogadores para teleportar.");
                    return;
                }
                Player randomPlayer = randomList.get(new Random().nextInt(randomList.size()));

                if (Bukkit.getServerName().contains("Practice")) {
                    player.performCommand("spec " + randomPlayer.getName());
                } else {
                    player.performCommand("tp " + randomPlayer.getName());
                }
            }),

        new HotbarItem(7, new ItemBuilder(Material.BLAZE_ROD).name("&6Congelar").build())
            .addInteractEntity((player, entity) -> {
                if (entity instanceof Player) {
                    Player target = (Player) entity;
                    player.chat("/ss " + target.getName());
                }
            }),
        new HotbarItem(8, new ItemBuilder(Material.INK_SACK).name("&cVanish").durability(1).build()
            , Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
            .addInteract((player) -> player.chat("/vanish")),

        new HotbarItem(8, new ItemBuilder(Material.INK_SACK).name("&aVanish").durability(10).build()
            , Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK)
            .addInteract((player) -> player.chat("/vanish"))

    );


    private Predicate<Profile> filter;
    private List<HotbarItem> items;

    Hotbar(Predicate<Profile> filter, HotbarItem... items) {
        this.filter = filter;
        this.items = Arrays.asList(items);
    }

    public void giveItems(Player player) {
        items.forEach(hotbarItem -> {
            ItemStack itemStack = hotbarItem.getItem();
            Integer slot = hotbarItem.getSlot();
            player.getInventory().setItem(slot, itemStack);
        });

        player.updateInventory();
    }


    public static List<Hotbar> getHotbars() {
        return Arrays.asList(values());
    }

    public HotbarItem getItem(ItemStack itemStack) {
        return items.stream().filter(hotbarItem -> hotbarItem.getItem().isSimilar(itemStack)).findFirst().orElse(null);
    }

}
