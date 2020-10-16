package com.nicko.core.essentials.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import com.nicko.core.util.events.BaseEvent;

@Getter
@Setter
public class SpawnTeleportEvent extends BaseEvent implements Cancellable {

    private final Player player;
    private Location location;
    private boolean cancelled;

    public SpawnTeleportEvent(Player player, Location location) {
        this.player = player;
        this.location = location;
    }

}
