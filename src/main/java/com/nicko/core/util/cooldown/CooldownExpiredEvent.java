package com.nicko.core.util.cooldown;

import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.util.events.BaseEvent;
import com.nicko.core.util.events.PlayerBase;

@Getter
public class CooldownExpiredEvent extends PlayerBase {

    private Cooldown cooldown;
    private boolean forced;

    CooldownExpiredEvent(Player player, Cooldown cooldown) {
        super(player);
        this.cooldown = cooldown;
    }

    public BaseEvent setForced(boolean forced) {
        this.forced = forced;
        return this;
    }
}