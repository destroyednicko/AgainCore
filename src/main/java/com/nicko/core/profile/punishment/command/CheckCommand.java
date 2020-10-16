package com.nicko.core.profile.punishment.command;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.entity.Player;
import com.nicko.core.cache.RedisPlayerData;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.menu.PunishmentsListMenu;

@CommandMeta(label = {"check", "c", "hist", "history", "historico"}, permission = "core.staff.check", async = true)
public class CheckCommand {

    public void execute(Player player, @CPL("jogador") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        RedisPlayerData redisPlayerData = Core.get().getRedisCache().getPlayerData(profile.getUuid());
        if (redisPlayerData == null) {
            player.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        new PunishmentsListMenu(profile, redisPlayerData).openMenu(player);
    }

}
