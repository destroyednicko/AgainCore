package com.nicko.core.profile.option.menu;

import com.google.common.collect.Lists;
import com.nicko.core.profile.option.enmus.Time;
import com.nicko.core.profile.option.menu.button.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;
import com.nicko.core.util.menu.button.DisplayButton;
import com.nicko.core.util.string.CC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileOptionsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&6&lConfigurações";
    }

    @Override
    public int getSize() {
        return 6 * 9;
    }


    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(4, new GeneralSetting());

        for (int i = 9; i < 54; i++) {
            buttons.put(i, new DisplayButton(new ItemBuilder(Material.STAINED_GLASS_PANE).durability(0).build(), true));
        }

        buttons.put(19, new PublicChatOptionButton());
        buttons.put(21, new PrivateChatOptionButton());
        buttons.put(23, new TimeButton());
        buttons.put(25, new ColorButton());

        buttons.put(38, new PrivateChatSoundsOptionButton());
        buttons.put(40, new TipsOptionButton());
        buttons.put(42, new ScoreboardOptionButton());


        //TODO: ??
        /*OptionsOpenedEvent event = new OptionsOpenedEvent(player);
        event.call();

        if (!event.getButtons().isEmpty()) {
            for (ProfileOptionButton button : event.getButtons()) {
                buttons.put(buttons.size(), button);
            }
        }*/

        return buttons;
    }

    public class GeneralSetting extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.WATCH).name("&7&lConfigurações Gerais").durability(0).build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (core.getMainConfig().getBoolean("COMMON.SERVER-OPTION")) {
                player.performCommand(core.getMainConfig().getString("COMMON.SERVER-COMMAND"));
            }
        }
    }

    public class ColorButton extends Button {

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Profile.getByPlayer(player);
            return new ItemBuilder(Material.WOOL)
                .name("&d&Cores")
                .lore("&aSua cor atual é: " + profile.getOptions().color() +
                    StringUtils.capitalize(profile.getOptions().color().name().toLowerCase()
                        .replace("_", " ")))
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            new ColorMenu().openMenu(player);
        }
    }

    @AllArgsConstructor
    public class TimeButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {

            Profile profile = Profile.getByPlayer(player);

            Time currentTime = profile.getOptions().time();

            List<String> lore = Lists.newArrayList();

            for (Time time : Time.values()) {
                lore.add((time == currentTime ? CC.BLUE + StringEscapeUtils.unescapeJava(" » ") : "   ") + "&e" +
                    StringUtils.capitalize(time.name().toLowerCase()));
            }

            return new ItemBuilder(Material.WATCH)
                .name("&6&lTempo")
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            Profile profile = Profile.getByPlayer(player);
            if (profile.getOptions().time() == Time.DAY) {
                profile.getOptions().time(Time.EVENING);
            } else if (profile.getOptions().time() == Time.EVENING) {
                profile.getOptions().time(Time.NIGHT);
            } else if (profile.getOptions().time() == Time.NIGHT) {
                profile.getOptions().time(Time.DAY);
            }
            player.chat("/" + profile.getOptions().time().name().toLowerCase());
        }

        @Override
        public boolean shouldUpdate(Player player, ClickType clickType) {
            return true;
        }
    }

}
