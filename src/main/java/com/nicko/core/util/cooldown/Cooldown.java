package com.nicko.core.util.cooldown;

import com.nicko.core.Core;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import com.nicko.core.util.string.CC;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Cooldown {

    @Getter
    private static Map<String, Cooldown> cooldownMap = new HashMap<>();
    private Map<UUID, Long> longMap;
    private Map<UUID, Integer> taskMap;
    private String name, displayName, expiredMessage;
    private long duration;

    public Cooldown(String name, long duration) {
        this(name, duration, null, null);
    }

    public Cooldown(String name, long duration, String displayName, String expiredMessage) {
        this.longMap = new HashMap<>();
        this.name = name;
        this.duration = duration;
        this.displayName = ((displayName == null) ? name : displayName);
        this.taskMap = new HashMap<>();
        if (expiredMessage != null) {
            this.expiredMessage = expiredMessage;
        }
        cooldownMap.put(name, this);
    }

    public void setCooldown(Player player) {
        this.setCooldown(player, false);
    }

    public void setCooldown(Player player, boolean announce) {
        CooldownStartingEvent event = new CooldownStartingEvent(player, this);
        if (!event.call()) {
            if (event.getReason() != null) {
                player.sendMessage(CC.translate(event.getReason()));
            }
            return;
        }
        taskMap.remove(player.getUniqueId());
        this.longMap.put(player.getUniqueId(), System.currentTimeMillis() + this.duration);
        if (new CooldownStartedEvent(player, this).call()) {
            if (this.expiredMessage != null && announce) {
                int taskId = new BukkitRunnable() {

                    @Override
                    public void run() {
                        if (!player.isOnline()) {
                            return;
                        }
                        for (String s : expiredMessage.split("\n")) player.sendMessage(CC.translate(s));
                        new CooldownExpiredEvent(player, Cooldown.this).call();
                    }
                }.runTaskLater(Core.get(), (int) duration / 1000 * 20L).getTaskId();
                taskMap.put(player.getUniqueId(), taskId);
                /*TaskUtil.runTaskLater(() -> {
                    if (player.isOnline() && isOnCooldown(player)) {
                        for (String s : expiredMessage.split("\n")) {
                            player.sendMessage(ColorText.translate(s));
                        }
                        new CooldownExpiredEvent(player, this).call();
                    }
                }, (int) this.duration / 1000 * 20L);*/
            }
        }
    }

    public long getDuration(Player player) {
        return longMap.getOrDefault(player.getUniqueId(), 0L) - System.currentTimeMillis();
    }

    public boolean isOnCooldown(Player player) {
        return this.getDuration(player) > 0L;
    }

    public boolean remove(Player player) {
        if (isOnCooldown(player)) {
            this.longMap.remove(player.getUniqueId());
            new CooldownExpiredEvent(player, this).setForced(true).call();
            if (taskMap.containsKey(player.getUniqueId())) {
                Bukkit.getServer().getScheduler().cancelTask(taskMap.get(player.getUniqueId()));
            }
        }
        return isOnCooldown(player);
    }
}