package com.nicko.core.profile.option.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import com.nicko.core.profile.option.menu.ProfileOptionButton;
import com.nicko.core.util.events.BaseEvent;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class OptionsOpenedEvent extends BaseEvent {

    private final Player player;
    private List<ProfileOptionButton> buttons = new ArrayList<>();

}
