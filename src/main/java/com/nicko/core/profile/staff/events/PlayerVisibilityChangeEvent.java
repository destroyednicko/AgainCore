package com.nicko.core.profile.staff.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.events.BaseEvent;

import java.util.List;

@AllArgsConstructor
@Getter
public class PlayerVisibilityChangeEvent extends BaseEvent {
    private boolean hide;
    private Profile profile;
    private List<Player> players;
}
