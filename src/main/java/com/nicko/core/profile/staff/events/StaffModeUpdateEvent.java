package com.nicko.core.profile.staff.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.util.events.BaseEvent;

@AllArgsConstructor
@Getter
public class StaffModeUpdateEvent extends BaseEvent {

    Player player;
    boolean toggle;
}
