package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"rename", "renomear"}, permission = "core.rename")
public class RenameCommand {

    public void execute(Player player, String name) {
        if (player.getItemInHand() != null) {
            ItemStack itemStack = player.getItemInHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(CC.translate(name));
            itemStack.setItemMeta(itemMeta);

            player.updateInventory();
            player.sendMessage(CC.GREEN + " • Você renomeou o item em sua mão.");
        } else {
            player.sendMessage(CC.RED + " • Não há nada em sua mão.");
        }
    }

}