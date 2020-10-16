package com.nicko.core.profile.punishment.listener;

import com.nicko.core.Core;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedure;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureStage;
import com.nicko.core.profile.punishment.procedure.PunishmentProcedureType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import com.nicko.core.network.packet.PacketBroadcastPunishment;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.util.callback.TypeCallback;
import com.nicko.core.util.menu.menus.ConfirmMenu;
import com.nicko.core.util.string.CC;

public class PunishmentListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("core.staff.grant")) return;

        PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancelar") || event.getMessage().equalsIgnoreCase("cancelban")) {
                PunishmentProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(CC.RED + " • Você cancelou o processo.");
                return;
            }

            if (procedure.getType() == PunishmentProcedureType.PARDON) {
                new ConfirmMenu(CC.YELLOW + "Perdoar esta punição?", (TypeCallback<Boolean>) data -> {
                    if (data) {
                        procedure.getPunishment().setRemovedBy(event.getPlayer().getUniqueId());
                        procedure.getPunishment().setRemovedAt(System.currentTimeMillis());
                        procedure.getPunishment().setRemovedReason(event.getMessage());
                        procedure.getPunishment().setRemoved(true);
                        procedure.finish();

                        event.getPlayer().sendMessage(CC.GREEN + " • A punição foi removida.");
                    } else {
                        procedure.cancel();
                        event.getPlayer().sendMessage(CC.RED + "Você não confirmou o processo.");
                    }
                }, true) {
                    @Override
                    public void onClose(Player player) {
                        if (!isClosedByMenu()) {
                            procedure.cancel();
                            event.getPlayer().sendMessage(CC.RED + "Você não confirmou o processo.");
                        }
                    }
                }.openMenu(event.getPlayer());
            } else if (procedure.getType() == PunishmentProcedureType.ADD) {
                String reason = event.getMessage();
                Profile profile = procedure.getRecipient();

                Punishment punishment = new Punishment(PunishmentType.BAN, System.currentTimeMillis(),
                    reason, Long.MAX_VALUE, Bukkit.getServerName());

                punishment.setAddedBy(event.getPlayer().getUniqueId());
                punishment.setTargetID(procedure.getRecipient().getUuid());
                punishment.save();
                profile.getPunishments().add(punishment);
                profile.save();

                Core.get().getHollow().sendPacket(new PacketBroadcastPunishment(punishment, event.getPlayer().getName(),
                    profile.getColoredUsername(), profile.getUuid(), true, Bukkit.getServerName()));

                Player target = profile.getPlayer();

                if (target != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            target.kickPlayer(punishment.getKickMessage());
                        }
                    }.runTask(Core.get());
                }
                procedure.finish();
            }
        }
    }

}
