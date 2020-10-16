package com.nicko.core.chat.event;

import com.nicko.core.util.events.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import com.nicko.core.chat.ChatAttempt;

@Getter
public class ChatAttemptEvent extends BaseEvent implements Cancellable {

    private final Player player;
    private final ChatAttempt chatAttempt;
    @Setter
    private String chatMessage;
    @Setter
    private boolean cancelled;
    @Setter
    private String cancelReason = "";

    public ChatAttemptEvent(Player player, ChatAttempt chatAttempt, String chatMessage) {
        this.player = player;
        this.chatAttempt = chatAttempt;
        this.chatMessage = chatMessage;
    }

}
