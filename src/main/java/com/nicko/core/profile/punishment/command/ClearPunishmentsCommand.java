package com.nicko.core.profile.punishment.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.network.packet.PacketClearPunishments;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;

@CommandMeta(label = {"clearpunishments", "limparpunicoes"}, permission = "core.admin.clearpunishments", async = true)
public class ClearPunishmentsCommand {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile) {
        if (profile == null) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }


        profile.getPunishments().forEach(Punishment::delete);

        profile.getPunishments().clear();
        profile.save();

        Core.get().getHollow().sendPacket(new PacketClearPunishments(profile.getUuid()));

        sender.sendMessage(ChatColor.GREEN + " • Você limpou as punições de " + profile.getColoredUsername() +
            ChatColor.GREEN + "!");
    }

}
