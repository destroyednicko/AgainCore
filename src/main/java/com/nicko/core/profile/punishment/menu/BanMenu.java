package com.nicko.core.profile.punishment.menu;

import com.google.common.collect.Maps;
import com.nicko.core.Core;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedure;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureStage;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureType;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.prefix.menu.PageButton;
import com.nicko.core.profile.punishment.BanReason;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.TaskUtil;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.button.DisplayButton;
import com.nicko.core.util.menu.pagination.PaginatedMenu;
import com.nicko.core.util.string.CC;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BanMenu extends PaginatedMenu {

    Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Painel de Punição";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        buttons.put(buttons.size(), new CustomBanButton());
        List<BanReason> reasons = BanReason.getBanReasons().stream().sorted(new BanReasonComparator()).collect(Collectors.toList());
        reasons.forEach(banReason -> buttons.put(buttons.size(), new BanReasonButton(banReason)));
        return buttons;
    }

    private class BanReasonComparator implements Comparator<BanReason> {

        @Override
        public int compare(BanReason o1, BanReason o2) {
            return Integer.compare(o1.getPunishmentType().getWeight(), o2.getPunishmentType().getWeight());
        }
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
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14, " "));
        }

        int highest = 0;

        for (int buttonValue : buttons.keySet()) {
            if (buttonValue > highest) {
                highest = buttonValue;
            }
        }

        int size = (int) (Math.ceil((highest + 1) / 9D) * 9D);

        for (int i = size; i < size + 9; i++) {
            buttons.put(i, Button.placeholder(Material.STAINED_GLASS_PANE, (byte) 14, " "));
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

        buttons.put(0, new DisplayButton(new ItemBuilder(Material.SKULL_ITEM)
            .name("&eBanning &c" + profile.getName())
            .durability(3).skullOwner(profile.getName()).build(), true));
        buttons.put(8, new BackButton());

        buttons.put(3, new PageButton(-1, this));
        buttons.put(4, new PageInfoButton(this));
        buttons.put(5, new PageButton(1, this));


        return buttons;
    }

    public class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE).name("&cFechar painel de punições").build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            TaskUtil.run(player::closeInventory);
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
    public class BanReasonButton extends Button {

        BanReason banReason;

        @Override
        public ItemStack getButtonItem(Player player) {
            String reason = banReason.getReason();
            if (reason.contains(";")) {
                reason = reason.replace(";", " ");
            }
            if (banReason.getPunishmentType() == PunishmentType.TEMP_BAN) {
                Duration duration = Duration.fromString(banReason.getDuration());
                return new ItemBuilder(Material.BOOK)
                    .name("&e" + reason + (banReason.isAdmit() ? " &7(&cAdmitido&7)" : ""))
                    .lore("&cTipo&f " + banReason.getPunishmentType().getReadable())
                    .lore("&9Tempo&f " + (duration.isPermanent() ? "Permanente" : TimeUtil.millisToRoundedTime(Duration.fromString(banReason.getDuration()).getValue())))
                    .build();
            } else if (banReason.getPunishmentType() == PunishmentType.BAN && Duration.fromString(banReason.getDuration()).isPermanent()) {
                Duration duration = Duration.fromString(banReason.getDuration());
                return new ItemBuilder(Material.ENCHANTED_BOOK)
                    .name("&e" + reason + (banReason.isAdmit() ? " &7(&cAdmitido&7)" : ""))
                    .lore("&cTipo&f " + banReason.getPunishmentType().getReadable())
                    .lore("&9Tempo&f " + (duration.isPermanent() ? "Permanente" : TimeUtil.millisToRoundedTime(Duration.fromString(banReason.getDuration()).getValue())))
                    .glow()
                    .build();
            }
            return new ItemBuilder(Material.BOOK_AND_QUILL)
                .name("&e" + reason)
                .lore("&cTipo&f " + banReason.getPunishmentType().getReadable())
                .lore("&9Tempo&f Permanente")
                .glow()
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            String reason = banReason.getReason();
            if (banReason.isAdmit()) {
                reason = reason + " (Admitido)";
            }
            if (reason.contains(";")) {
                reason = reason.replace(";", " ");
            }

            if (banReason.getPunishmentType() == PunishmentType.TEMP_BAN && !player.hasPermission("core.staff.tempban")) {
                player.sendMessage(CC.RED + " • Você não tem permissão!");
                player.closeInventory();
                return;
            } else if (banReason.getPunishmentType() == PunishmentType.BAN && !player.hasPermission("core.staff.ban")) {
                player.sendMessage(CC.RED + " • Você não tem permissão!");
                player.closeInventory();
                return;
            } else if (banReason.getPunishmentType() == PunishmentType.BLACKLIST && !player.hasPermission("core.staff.blacklist")) {
                player.sendMessage(CC.RED + " • Você não tem permissão!");
                player.closeInventory();
                return;
            }

            Punishment punishment = new Punishment(banReason.getPunishmentType(), System.currentTimeMillis(),
                reason, Duration.fromString(banReason.getDuration()).getValue(), Bukkit.getServerName());

            punishment.setAddedBy(player.getUniqueId());

            punishment.setTargetID(profile.getUuid());
            punishment.save();
            profile.getPunishments().add(punishment);
            profile.save();

            Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, player.getName(),
                profile.getColoredUsername(), profile.getUuid(), true, Bukkit.getServerName()));

            Player target = profile.getPlayer();

            if (target != null) {
                TaskUtil.run(() -> target.kickPlayer(punishment.getKickMessage()));
            }

            TaskUtil.run(player::closeInventory);
        }
    }

    public class CustomBanButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&ePunição Customizada")
                .lore("&7Depois de clicar, digite no chat o motivo do ban permanente")
                .lore("&7caso queria cancelar, digite /cancelban")
                .glow()
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new PunishmentProcedure(player, profile, PunishmentProcedureType.ADD, PunishmentProcedureStage.REQUIRE_TEXT);

            player.sendMessage(CC.GREEN + "Digite um motivo para a punição.");
            player.closeInventory();
        }
    }
}
