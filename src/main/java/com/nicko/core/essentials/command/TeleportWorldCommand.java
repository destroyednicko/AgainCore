package com.nicko.core.essentials.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import com.nicko.core.util.string.CC;

@CommandMeta(label = "tpworld", permission = "core.tpworld")
public class TeleportWorldCommand {

    public void execute(Player player, @CPL("mundo") String worldName) {
        World world = Bukkit.getWorld(worldName);

        if (world == null) {
            world = Bukkit.createWorld(new WorldCreator(worldName));
            player.sendMessage(CC.GOLD + " • Criando novo mundo \"" + worldName + "\"");
        }

        if (world == null) {
            player.sendMessage(CC.RED + " • Um mundo com este nome não existe.");
        } else {
            player.teleport(world.getSpawnLocation());
            player.sendMessage(CC.GOLD + " • Teleportado para o mundo " + world.getName());
        }
    }

}
