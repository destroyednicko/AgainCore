package com.nicko.core.profile.grant.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.profile.grant.Grant;
import com.nicko.core.util.events.BaseEvent;

@AllArgsConstructor
@Getter
public class GrantExpireEvent extends BaseEvent {

    private Player player;
    private Grant grant;

}
