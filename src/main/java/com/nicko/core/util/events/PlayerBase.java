package com.nicko.core.util.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PlayerBase extends BaseEvent {
    @Getter Player player;
}
