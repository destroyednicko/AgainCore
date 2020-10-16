package com.nicko.core.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import com.nicko.core.Core;

public class BungeeUtil {

    /**
     * Conecta o jogador ao servidor determinado.
     *
     * @param player     o jogador a ser conectado.
     * @param serverName O nome do servidor para conectar.
     */
    public static void connect(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(Core.get(), "BungeeCord", output.toByteArray());
    }

}
