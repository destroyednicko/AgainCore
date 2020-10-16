package com.nicko.core.essentials.menus;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import com.nicko.core.util.PotionUtil;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.item.InventoryUtil;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.Menu;
import com.nicko.core.util.menu.button.DisplayButton;
import com.nicko.core.util.string.CC;

import java.util.*;

public class InvseeMenu extends Menu {

    private Player target;

    public InvseeMenu(Player target) {
        this.target = target;
        setAutoUpdate(true);
    }

    @Override
    public String getTitle(Player player) {
        return CC.GOLD + " • Inventário de " + this.target.getName();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        if (player == null) {
            return buttons;
        }

        final ItemStack[] fixedContents = InventoryUtil.fixInventoryOrder(this.target.getInventory().getContents());

        for (int i = 0; i < fixedContents.length; i++) {
            final ItemStack itemStack = fixedContents[i];

            if (itemStack == null || itemStack.getType() == Material.AIR) {
                continue;
            }

            buttons.put(i, new DisplayButton(itemStack, true));
        }

        for (int i = 0; i < this.target.getInventory().getArmorContents().length; i++) {
            ItemStack itemStack = this.target.getInventory().getArmorContents()[i];

            if (itemStack != null && itemStack.getType() != Material.AIR) {
                buttons.put(39 - i, new DisplayButton(itemStack, true));
            }
        }

        int pos = 45;

        buttons.put(
            pos++,
            new HealthButton(this.target.getHealth() == 0 ? 0 : (int) Math.round(this.target.getHealth() / 2))
        );
        buttons.put(pos++, new HungerButton(this.target.getFoodLevel()));
        buttons.put(pos, new EffectsButton(this.target.getActivePotionEffects()));

        return buttons;
    }

    @Override
    public boolean isAutoUpdate() {
        return true;
    }

    @AllArgsConstructor
    private class HealthButton extends Button {

        private int health;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.MELON)
                .name(CC.YELLOW + CC.BOLD + " • HP: " + CC.PINK + this.health + "/10 " + StringEscapeUtils.unescapeJava("\u2764"))
                .amount(this.health == 0 ? 1 : this.health)
                .build();
        }
    }

    @AllArgsConstructor
    private class HungerButton extends Button {

        private int hunger;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.COOKED_BEEF)
                .name(CC.YELLOW + CC.BOLD + " • Fome: " + CC.PINK + this.hunger + "/20")
                .amount(this.hunger == 0 ? 1 : this.hunger)
                .build();
        }
    }

    @AllArgsConstructor
    private class EffectsButton extends Button {

        private Collection<PotionEffect> effects;

        @Override
        public ItemStack getButtonItem(Player player) {
            final ItemBuilder
                builder = new ItemBuilder(Material.POTION).name(CC.YELLOW + CC.BOLD + "Efeitos de Poções");

            if (this.effects.isEmpty()) {
                builder.lore(CC.GRAY + " • Sem efeitos");
            } else {
                final List<String> lore = new ArrayList<>();

                this.effects.forEach(effect -> {
                    final String name = PotionUtil.getName(effect.getType()) + " " + (effect.getAmplifier() + 1);
                    final String duration = " (" + TimeUtil.millisToTimer((effect.getDuration() / 20) * 1000) + ")";

                    lore.add(CC.PINK + name + duration);
                });

                builder.lore(lore);
            }

            return builder.build();
        }
    }

}
