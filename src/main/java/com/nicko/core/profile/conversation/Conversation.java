package com.nicko.core.profile.conversation;

import com.nicko.core.CoreAPI;
import com.nicko.core.Idioma;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

import java.util.UUID;

public class Conversation {

    @Getter
    private final UUID initiatedBy;
    @Getter
    private final UUID target;
    @Getter
    private long lastMessageSentAt;
    @Getter
    private UUID lastMessageSentBy;

    public Conversation(UUID initiatedBy, UUID target) {
        this.initiatedBy = initiatedBy;
        this.target = target;
        this.lastMessageSentAt = System.currentTimeMillis();

        Profile initiatorProfile = Profile.getByUuid(initiatedBy);
        initiatorProfile.getConversations().getConversations().put(target, this);

        Profile targetProfile = Profile.getByUuid(target);
        targetProfile.getConversations().getConversations().put(initiatedBy, this);
    }

    public void sendMessage(Player sender, Player target, String message) {
        Profile profile = Profile.getByPlayer(sender);
        sender.sendMessage(Idioma.CONVERSATION_SEND_MESSAGE.format(
            sender.getName(), sender.getDisplayName(), CoreAPI.getColorOfPlayer(sender),
            target.getName(), target.getDisplayName(), CoreAPI.getColorOfPlayer(target), profile.getOptions().color())
            .replace("%MSG%", message));

        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (targetProfile.getOptions().playingMessageSounds()) {
            target.playSound(target.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 1.0F);
        }

        target.sendMessage(Idioma.CONVERSATION_RECEIVE_MESSAGE.format(
            target.getName(), target.getDisplayName(), CoreAPI.getColorOfPlayer(target),
            sender.getName(), sender.getDisplayName(), CoreAPI.getColorOfPlayer(sender), targetProfile.getOptions().color())
            .replace("%MSG%", message));

        Bukkit.getOnlinePlayers().forEach(staff -> {
            Profile profileStaff = Profile.getByUuid(staff.getUniqueId());
            if (profileStaff.getStaffOptions().isSocialSpy()) {
                staff.sendMessage(CC.translate(Idioma.STAFF_SOCIAL_SPY.format(
                    sender.getName(), sender.getDisplayName(), CoreAPI.getColorOfPlayer(sender),
                    target.getName(), target.getDisplayName(), CoreAPI.getColorOfPlayer(target), profileStaff.getOptions().color())
                    .replace("%MSG%", message)));
            }
        });

        lastMessageSentAt = System.currentTimeMillis();
        lastMessageSentBy = sender.getUniqueId();
    }

    public boolean validate() {
        Player initiator = Bukkit.getPlayer(initiatedBy);

        if (initiator == null || !initiator.isOnline()) {
            destroy();
            return false;
        }

        Player target = Bukkit.getPlayer(this.target);

        if (target == null || !target.isOnline()) {
            destroy();
            return false;
        }

        return true;
    }

    public void destroy() {
        for (Player player : new Player[]{Bukkit.getPlayer(initiatedBy), Bukkit.getPlayer(target)}) {
            if (player != null && player.isOnline()) {
                Profile profile = Profile.getByUuid(player.getUniqueId());
                profile.getConversations().getConversations().remove(getPartner(player.getUniqueId()));
            }
        }
    }

    public UUID getPartner(UUID compareWith) {
        if (initiatedBy.equals(compareWith)) {
            return target;
        } else if (target.equals(compareWith)) {
            return initiatedBy;
        }

        return null;
    }

}
