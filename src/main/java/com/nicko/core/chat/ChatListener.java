package com.nicko.core.chat;

import com.nicko.core.Core;
import com.nicko.core.chat.event.ChatAttemptEvent;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.string.CC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.nicko.core.Idioma;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        ChatAttempt chatAttempt = Core.get().getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

        Bukkit.getServer().getPluginManager().callEvent(chatAttemptEvent);

        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
                    event.setFormat("%1$s" + CC.RESET + ": %2$s");
                }
                break;

                case MESSAGE_FILTERED: {
                    chatAttempt.getFilterFlagged().punish(event.getPlayer());
                    event.setCancelled(true);
                }
                break;

                case PLAYER_MUTED: {
                    event.setCancelled(true);

                    if (chatAttempt.getPunishment().isPermanent()) {
                        event.getPlayer().sendMessage(CC.RED + " • Você está silenciado permanentemente.");
                    } else {
                        event.getPlayer().sendMessage(CC.RED + " • Você está silenciado por mais " +
                            chatAttempt.getPunishment().getTimeRemaining() + ".");
                    }
                }
                break;
                case CHAT_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "The public chat is currently muted.");
                }
                break;

                case CHAT_DELAYED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(Idioma.CHAT_DELAYED.format(
                        TimeUtil.millisToSeconds((long) chatAttempt.getValue())) + " seconds");
                }
                break;
            }
        }

        if (chatAttempt.getResponse() == ChatAttempt.Response.ALLOWED) {
            event.getRecipients().removeIf(player -> {
                Profile profile = Profile.getProfiles().get(player.getUniqueId());
                return profile != null && !profile.getOptions().publicChatEnabled();
            });
        }
    }

}
