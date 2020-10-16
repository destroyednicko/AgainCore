package com.nicko.core.network.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.util.events.BaseEvent;

@AllArgsConstructor
@Getter
public class FreezePlayerEvent extends BaseEvent {

    private CommandSender staff;
    private Player target;
    private boolean freeze;

}
