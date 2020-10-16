package com.nicko.core.essentials;

import com.nicko.core.essentials.event.SpawnTeleportEvent;
import com.nicko.core.essentials.tasks.RestartTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.util.LocationUtil;

import java.io.IOException;
import java.util.List;

public class Essentials {

    private Core core;
    private Location spawn;
    @Getter @Setter
    private boolean donorOnly;
    @Getter private List<String> commandsBlocked;
    @Getter private boolean restarting;
    @Getter private int taskID;

    public Essentials(Core core) {
        this.core = core;
        this.spawn = LocationUtil.deserialize(core.getMainConfig().getStringOrDefault("ESSENTIAL.SPAWN_LOCATION", null));
        this.donorOnly = core.getMainConfig().getBoolean("ESSENTIAL.DONOR_ONY");
        this.commandsBlocked = core.getMainConfig().getStringList("ESSENTIAL.COMMAND_BLOCK");
    }

    public void restart(int time) {
        this.restarting = true;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(core, new RestartTask(time), 0, 20);
    }

    public void cancelRestart() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public void setSpawn(Location location) {
        spawn = location;

        if (spawn == null) {
            core.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", null);
        } else {
            core.getMainConfig().getConfiguration().set("ESSENTIAL.SPAWN_LOCATION", LocationUtil.serialize(this.spawn));
        }

        try {
            core.getMainConfig().getConfiguration().save(core.getMainConfig().getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        core.reloadConfig();
        core.getMainConfig().setConfiguration(YamlConfiguration.loadConfiguration(core.getMainConfig().getFile()));
        this.commandsBlocked = core.getMainConfig().getStringList("ESSENTIAL.COMMAND_BLOCK");
    }

    public void teleportToSpawn(Player player) {
        Location location = spawn == null ? core.getServer().getWorlds().get(0).getSpawnLocation() : spawn;

        SpawnTeleportEvent event = new SpawnTeleportEvent(player, location);
        event.call();

        if (!event.isCancelled() && event.getLocation() != null) {
            player.teleport(event.getLocation());
        }
    }

    public int clearEntities(World world) {
        int removed = 0;

        for (Entity entity : world.getEntities()) {
            if (entity.getType() == EntityType.PLAYER) {
                continue;
            }

            removed++;
            entity.remove();
        }

        return removed;
    }

    public int clearEntities(World world, EntityType... excluded) {
        int removed = 0;

        entityLoop:
        for (Entity entity : world.getEntities()) {
            if (entity instanceof Item) {
                removed++;
                entity.remove();
                continue;
            }

            for (EntityType type : excluded) {
                if (entity.getType() == EntityType.PLAYER) {
                    continue entityLoop;
                }

                if (entity.getType() == type) {
                    continue entityLoop;
                }
            }

            removed++;
            entity.remove();
        }

        return removed;
    }

}
