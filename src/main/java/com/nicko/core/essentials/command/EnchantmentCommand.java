package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.item.EnchantmentUtil;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"enchantment", "enchant", "encantar"}, permission = "core.enchantment")
public class EnchantmentCommand {

    public void excute(Player player, @CPL("encantamento") String enchantment, @CPL("nível") String level) {
        ItemStack hand = player.getItemInHand();
        if (hand == null) {
            player.sendMessage(CC.translate("&c • Você não tem nenhum item em sua mão!"));
            return;
        }
        if (EnchantmentUtil.getByName(enchantment) == null) {
            player.sendMessage(CC.translate("&c • Encantamento não encontrado."));
            return;
        }
        hand.addUnsafeEnchantment(EnchantmentUtil.getByName(enchantment), Integer.parseInt(level));
        player.sendMessage(CC.translate("&e • Encantamento &c" + enchantment + " " + level + " &eaplicado a &c"
            + StringUtils.capitalize(hand.getType().name().toLowerCase().replace("_", " "))));
        Bukkit.getOnlinePlayers().forEach(staff -> {
            if (staff.hasPermission("core.enchantment.alert")) {
                staff.sendMessage(CC.translate("&e • &7(" + player.getName() + "&7) &eaplicou &c" + enchantment + " " + level + " &ea &c"
                    + StringUtils.capitalize(hand.getType().name().toLowerCase().replace("_", " "))));
            }
        });
    }

}
