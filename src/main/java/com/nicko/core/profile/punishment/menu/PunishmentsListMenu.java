package com.nicko.core.profile.punishment.menu;

import com.nicko.core.profile.punishment.procedure.PunishmentProcedure;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureStage;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureType;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.cache.RedisPlayerData;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.menu.ProfileMenuControlHeaderButton;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.pagination.FilterablePaginatedMenu;
import com.nicko.core.util.menu.pagination.PageFilter;
import com.nicko.core.util.string.CC;

import java.util.*;

@AllArgsConstructor
public class PunishmentsListMenu extends FilterablePaginatedMenu<Punishment> {

    private Profile profile;
    private RedisPlayerData redisPlayerData;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "&c • Punições de " + profile.getName() + " (" + getCountOfFilteredPunishments() + ")";
    }

    @Override
    public Map<Integer, Button> getFilteredButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        obj:
        for (Punishment punishment : profile.getPunishments()) {
            for (PageFilter<Punishment> filter : getFilters()) {
                if (!filter.test(punishment)) {
                    continue obj;
                }
            }

            buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = super.getGlobalButtons(player);

        if (profile.getName() != null) {
            buttons.put(4, new ProfileMenuControlHeaderButton(profile, redisPlayerData));
        }

        return buttons;
    }

    @Override
    public List<PageFilter<Punishment>> generateFilters() {
        List<PageFilter<Punishment>> filters = new ArrayList<>();
        filters.add(new PageFilter<>("Tipo: Ban", (punishment -> punishment.getType() == PunishmentType.BAN)));
        filters.add(new PageFilter<>("Tipo: Mute", (punishment -> punishment.getType() == PunishmentType.MUTE)));
        filters.add(new PageFilter<>("Tipo: Warn", (punishment -> punishment.getType() == PunishmentType.WARN)));
        filters.add(new PageFilter<>("Tipo: Kick", (punishment -> punishment.getType() == PunishmentType.KICK)));
        filters.add(new PageFilter<>("Tipo: Temp Ban", (punishment -> punishment.getType() == PunishmentType.TEMP_BAN)));
        filters.add(new PageFilter<>("Tipo: BlackList", (punishment -> punishment.getType() == PunishmentType.BLACKLIST)));
        filters.add(new PageFilter<>("Por console", (punishment -> punishment.getAddedBy() == null)));
        filters.add(new PageFilter<>("Removido", Punishment::isRemoved));
        return filters;
    }

    private int getCountOfFilteredPunishments() {
        int i = 0;

        obj:
        for (Punishment punishment : profile.getPunishments()) {
            for (PageFilter<Punishment> filter : getFilters()) {
                if (!filter.test(punishment)) {
                    continue obj;
                }
            }

            i++;
        }

        return i;
    }

    @AllArgsConstructor
    private class PunishmentInfoButton extends Button {

        private Punishment punishment;

        @Override
        public ItemStack getButtonItem(Player player) {
            int durability;

            if (punishment.isRemoved()) {
                durability = 5;
            } else if (punishment.hasExpired()) {
                durability = 4;
            } else {
                durability = 14;
            }

            String addedBy = "Console";

            if (punishment.getAddedBy() != null) {
                try {
                    Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
                    addedBy = addedByProfile.getName();
                } catch (Exception e) {
                    addedBy = "Não foi possível encontrar...";
                }
            }

            String removedBy = "Console";

            if (punishment.getRemovedBy() != null) {
                try {
                    Profile removedByProfile = Profile.getByUuid(punishment.getRemovedBy());
                    removedBy = removedByProfile.getName();
                } catch (Exception e) {
                    removedBy = "Não foi possível encontrar...";
                }
            }

            List<String> lore = new ArrayList<>();
            lore.add(CC.MENU_BAR);
            lore.add("&3Tipo: &e" + punishment.getType().getReadable());
            lore.add("&3Duração: &e" + punishment.getDurationText());
            lore.add("&3Responsável: &e" + addedBy);
            lore.add("&3Motivo: &e&o\"" + punishment.getAddedReason() + "\"");

            if (punishment.isRemoved()) {
                lore.add(CC.MENU_BAR);
                lore.add("&a&lPunição Removida");
                lore.add("&a" + TimeUtil.dateToString(new Date(punishment.getRemovedAt()), "&7"));
                lore.add("&aRemovida por: &7" + removedBy);
                lore.add("&aMotivo: &7&o\"" + punishment.getRemovedReason() + "\"");
            } else {
                if (!punishment.hasExpired() && punishment.getType().isRemovable()) {
                    lore.add(CC.MENU_BAR);
                    lore.add("&aClique Direito para remover essa punição.");
                }
            }

            lore.add(CC.MENU_BAR);

            return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(durability)
                .name("&3" + TimeUtil.dateToString(new Date(punishment.getAddedAt()), "&7"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType == ClickType.RIGHT && !punishment.isRemoved() && !punishment.hasExpired() && punishment.getType().isRemovable()) {
                PunishmentProcedure procedure = new PunishmentProcedure(player, profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
                procedure.setPunishment(punishment);

                player.sendMessage(CC.GREEN + "Digite um motivo para remover esta punição.");
                player.closeInventory();
            }
        }
    }

}
