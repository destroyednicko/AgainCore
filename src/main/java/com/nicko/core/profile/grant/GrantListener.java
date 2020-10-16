package com.nicko.core.profile.grant;

import com.nicko.core.profile.grant.event.GrantAppliedEvent;
import com.nicko.core.profile.grant.event.GrantExpireEvent;
import com.nicko.core.profile.grant.procedure.GrantProcedure;
import com.nicko.core.profile.grant.procedure.GrantProcedureStage;
import com.nicko.core.profile.grant.procedure.GrantProcedureType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import com.nicko.core.Core;
import com.nicko.core.network.packet.PacketDeleteGrant;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.callback.TypeCallback;
import com.nicko.core.util.menu.menus.ConfirmMenu;
import com.nicko.core.util.string.CC;

public class GrantListener implements Listener {

    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.GREEN + ("O ranking `{rank}` foi adicionado a sua conta. Duração: {time-remaining}.")
            .replace("{rank}", grant.getRank().getDisplayName())
            .replace("{time-remaining}", grant.getDuration() == Long.MAX_VALUE ?
                "permanente" : TimeUtil.millisToRoundedTime((grant.getAddedAt() + grant.getDuration()) -
                System.currentTimeMillis())));

        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.RED + ("Seu ranking `{rank}` expirou.")
            .replace("{rank}", grant.getRank().getDisplayName()));

        Profile profile = Profile.getByUuid(player.getUniqueId());
        profile.setupBukkitPlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("core.staff.grant")) {
            return;
        }

        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancelar")) {
                GrantProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(CC.RED + " • O processo de 'grant' foi cancelado.");
                return;
            }

            if (procedure.getType() == GrantProcedureType.REMOVE) {
                new ConfirmMenu(CC.YELLOW + "Deletar este grant?", (TypeCallback<Boolean>) data -> {
                    if (data) {
                        procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                        procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                        procedure.getGrant().setRemovedReason(event.getMessage());
                        procedure.getGrant().setRemoved(true);
                        procedure.finish();
                        event.getPlayer().sendMessage(CC.GREEN + "O 'grant' foi removido.");

                        Core.get().getHollow().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(),
                            procedure.getGrant()));
                    } else {
                        procedure.cancel();
                        event.getPlayer().sendMessage(CC.RED + "Você não confirmou o cancelamento do 'grant'.");
                    }
                }, true) {
                    @Override
                    public void onClose(Player player) {
                        if (!isClosedByMenu()) {
                            procedure.cancel();
                            event.getPlayer().sendMessage(CC.RED + "Você não confirmou o cancelamento do 'grant'.");
                        }
                    }
                }.openMenu(event.getPlayer());
            }
        }
    }

}
