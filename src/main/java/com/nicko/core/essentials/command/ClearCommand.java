package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.util.string.CC;

@CommandMeta(label = {"clearinv", "clear", "ci"}, permission = "core.clearinv")
public class ClearCommand {

    public void execute(Player player) {
        player.getInventory().setContents(new ItemStack[36]);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Você limpou seu inventário.");
    }

    public void execute(CommandSender sender, Player player) {
        player.getInventory().setContents(new ItemStack[36]);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.updateInventory();
        player.sendMessage(CC.GOLD + " • Seu inventário foi limpo por " + sender.getName());
    }

}
