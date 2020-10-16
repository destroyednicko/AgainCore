package com.nicko.core.network.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import com.nicko.core.util.events.BaseEvent;

@Getter
public class ReceiveStaffChatEvent extends BaseEvent implements Cancellable {

    private Player player;
    @Setter private boolean cancelled;

    public ReceiveStaffChatEvent(Player player) {
        this.player = player;
    }

}
