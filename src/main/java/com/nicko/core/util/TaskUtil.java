package com.nicko.core.util;

import com.nicko.core.Core;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskUtil {

    public static void run(Runnable runnable) {
        Core.get().getServer().getScheduler().runTask(Core.get(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        Core.get().getServer().getScheduler().runTaskTimer(Core.get(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(Core.get(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        Core.get().getServer().getScheduler().runTaskLater(Core.get(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        Core.get().getServer().getScheduler().runTaskAsynchronously(Core.get(), runnable);
    }

}
