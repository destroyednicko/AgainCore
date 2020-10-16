package com.nicko.core.profile.conversation;

import lombok.Getter;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;

import java.util.*;
import java.util.stream.Collectors;

public class ProfileConversations {

    @Getter
    private final Profile profile;
    @Getter
    private Map<UUID, Conversation> conversations;

    public ProfileConversations(Profile profile) {
        this.profile = profile;
        this.conversations = new HashMap<>();
    }

    public boolean canBeMessagedBy(Player player) {
        if ((profile.getStaffOptions().isVanish())
            && !player.hasPermission("core.staff")) {
            return false;
        }
        if (!profile.getOptions().receivingNewConversations() && !player.hasPermission("core.msg.bypass")) {
            return false;
        }

        return !profile.getIgnoreList().contains(player.getUniqueId());
    }

    public Conversation getOrCreateConversation(Player target) {
        Player sender = profile.getPlayer();

        if (sender != null) {
            Conversation conversation = conversations.get(target.getUniqueId());

            if (conversation == null) {
                conversation = new Conversation(profile.getUuid(), target.getUniqueId());
            }

            return conversation;
        }

        return null;
    }

    public Conversation getLastRepliedConversation() {
        List<Conversation> list = conversations
            .values()
            .stream()
            .sorted(Comparator.comparingLong(Conversation::getLastMessageSentAt))
            .collect(Collectors.toList());

        Collections.reverse(list);

        return list.isEmpty() ? null : list.get(0);
    }

    public void expireAllConversations() {
        this.conversations.clear();
    }

}
