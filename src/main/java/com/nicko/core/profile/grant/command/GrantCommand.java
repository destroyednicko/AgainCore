package com.nicko.core.profile.grant.command;

import com.nicko.shinigami.command.CPL;
import com.nicko.shinigami.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.network.packet.PacketAddGrant;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.grant.Grant;
import com.nicko.core.profile.grant.event.GrantAppliedEvent;
import com.nicko.core.rank.Rank;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.duration.Duration;
import com.nicko.core.util.string.CC;

import java.util.UUID;

@CommandMeta(label = "grant", async = true, permission = "core.grants.add")
public class GrantCommand {

    public void execute(CommandSender sender, @CPL("jogador") Profile profile, @CPL("rank") Rank rank, @CPL("duração") Duration duration, @CPL("motivo") String reason) {
        if (rank == null) {
            sender.sendMessage(Idioma.RANK_NOT_FOUND.format());
            return;
        }

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Idioma.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + " • A duração é inválida.");
            sender.sendMessage(CC.RED + " • Exemplo: [perm/1y, 1m, 1w, 1d]");
            return;
        }

        UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
            duration.getValue());

        profile.setActiveGrant(grant);
        profile.save();

        Core.get().getHollow().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

        sender.sendMessage(CC.GREEN + " • O ranking `{rank}` foi definido para `{player}` por {time-remaining}."
            .replace("{rank}", rank.getDisplayName())
            .replace("{player}", profile.getName())
            .replace("{time-remaining}", duration.getValue() == Long.MAX_VALUE ? "permanente"
                : TimeUtil.millisToRoundedTime(duration.getValue())));

        Player player = profile.getPlayer();

        if (player != null) {
            new GrantAppliedEvent(player, grant).call();
        }
    }

}
