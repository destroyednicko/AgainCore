package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "skull", permission = "core.skull")
public class SkullCommand {

    public void execute(Player player, @CPL("dono") String targetName) {
        player.getInventory().addItem(new ItemBuilder(Material.SKULL_ITEM).skullOwner(targetName).build());
        player.sendMessage(CC.translate("&e • Cabeça de " + targetName + " &frecebida com sucesso."));
    }

}
