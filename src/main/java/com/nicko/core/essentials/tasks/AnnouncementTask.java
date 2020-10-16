package com.nicko.core.essentials.tasks;

import org.bukkit.Bukkit;
import com.nicko.core.Core;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;

import java.util.List;

public class AnnouncementTask {

    private Core plugin = Core.get();
    private List<String> announcements = plugin.getMainConfig().getStringList("ANNOUNCEMENTS.LIST");
    private int count = 0;

    public AnnouncementTask() {
        this.plugin.getServer().getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
            String announce = getNextAnnouncerMessage();
            Bukkit.getOnlinePlayers().forEach(player -> {
                Profile profile = Profile.getByPlayer(player);
                if (profile.getOptions().tipsAnnounce()) {
                    player.sendMessage(CC.translate(announce));
                }
            });
        }, 20L * 60, 20L * 60 * plugin.getMainConfig().getInteger("ANNOUNCEMENTS.TIME"));
    }

    private String getNextAnnouncerMessage() {
        if (this.count >= this.announcements.size()) {
            this.count = 0;
        }

        String message = this.announcements.get(count);

        message = message.replace("\\n", System.lineSeparator());
        this.count++;

        return CC.translate(message);
    }

}
