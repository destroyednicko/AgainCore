package com.nicko.core.essentials.tasks;

import com.nicko.core.Core;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.nicko.core.util.BungeeUtil;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.string.CC;

@AllArgsConstructor
public class RestartTask implements Runnable {

    int time;

    @Override
    public void run() {
        if (time <= 0) {
            Bukkit.broadcastMessage(CC.translate("&a • O servidor irá reiniciar agora."));

            Core.get().getEssentials().cancelRestart();
            for (Player player : Bukkit.getOnlinePlayers()) {
                BungeeUtil.connect(player, "lobby");
            }
            Bukkit.shutdown();
            return;
        }
        if (String.valueOf(time).endsWith("0") || String.valueOf(time).endsWith("5")) {
            Bukkit.broadcastMessage(CC.translate("&a • O servidor irá reiniciar em&e " + TimeUtil.formatIntoDetailedString(time)));
        }
        time--;
    }
}
