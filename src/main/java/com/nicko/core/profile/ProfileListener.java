package com.nicko.core.profile;

import com.nicko.core.Core;
import com.nicko.core.Idioma;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.cache.RedisPlayerData;
import com.nicko.core.network.packet.PacketStaffChat;
import com.nicko.core.network.packet.PacketStaffJoinNetwork;
import com.nicko.core.network.packet.PacketStaffLeaveNetwork;
import com.nicko.core.util.TaskUtil;
import com.nicko.core.util.string.CC;

import java.util.Iterator;

public class ProfileListener implements Listener {

    private static Core core = Core.get();

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST)) {
            event.setKickMessage(core.getMainConfig().getString("GLOBAL_WHITELIST.KICK_MAINTENANCE"));
            return;
        }

        Player player = Bukkit.getPlayer(event.getUniqueId());

        // É necessário verificar se o jogador ainda está logado ao apontar outra tentativa de login
        // Isso acontece quando um jogador usando um client personalizado pode acessar a lista de servidores durante o jogo (e reconectar)
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.RED + "Você tentou logar muito rápido após desconectar.\nTente novamente em alguns instantes.");
            core.getServer().getScheduler().runTask(core, () -> player.kickPlayer(CC.RED + "Login duplicado"));
            return;
        }

        Profile profile = null;

        try {
            profile = new Profile(event.getName(), event.getUniqueId());

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Idioma.FAILED_TO_LOAD_PROFILE.format());
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BLACKLIST));
                return;
            }

            if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.BAN));
                return;
            }
            if (profile.getActivePunishmentByType(PunishmentType.TEMP_BAN) != null) {
                handleBan(event, profile.getActivePunishmentByType(PunishmentType.TEMP_BAN));
                return;
            }

            profile.setName(event.getName());

            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }
            profile.setLastSeen(System.currentTimeMillis());
            profile.setLastSeenServer(Bukkit.getServerName());
            profile.setOnline(true);

            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            } else if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
                profile.setAuthenticated(false);
            }

            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }

            for (Profile alt : Profile.getByIpAddress(event.getAddress().getHostAddress())) {
                profile.addAlt(alt);
                alt.addAlt(profile);
                if (profile != alt) {
                    alt.save();
                }
                if (alt.getActivePunishmentByType(PunishmentType.BAN) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BAN));
                    return;
                }
                if (alt.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                    handleBan(event, alt.getActivePunishmentByType(PunishmentType.BLACKLIST));
                    return;
                }
            }


            profile.save();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (profile == null || !profile.isLoaded()) {
            event.setKickMessage(Idioma.FAILED_TO_LOAD_PROFILE.format());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        Profile.getProfiles().put(profile.getUuid(), profile);

        RedisPlayerData playerData = new RedisPlayerData(event.getUniqueId(), event.getName());
        playerData.setLastAction(RedisPlayerData.LastAction.JOINING_SERVER);
        playerData.setLastSeenServer(Bukkit.getServerName());
        playerData.setLastSeenAt(System.currentTimeMillis());

        core.getRedisCache().updatePlayerData(playerData);
        core.getRedisCache().updateNameAndUUID(event.getName(), event.getUniqueId());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL) && event.getPlayer().hasPermission("core.utils.bypassfull")) {
            event.setResult(PlayerLoginEvent.Result.ALLOWED);
            event.allow();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.setupBukkitPlayer(player);

        Bukkit.getScheduler().runTaskAsynchronously(core, () -> {
            // TODO: atualizar apenas se currentAddress != lastAddress ou se countryCode ou countryName == "unknown"
            core.getGeoIPAPI().lookupPlayer(profile, player.getAddress().getAddress());
        });

        if (!player.hasPermission("core.staff")) return;

        TaskUtil.runLater(() -> {
            core.getHollow().sendPacket(new PacketStaffJoinNetwork(profile.getColoredUsername(), Bukkit.getServerName()));
        }, 5L);
    }

    public static void leavePlayer(Player player, boolean disable) {

        Profile profile = Profile.getProfiles().remove(player.getUniqueId());
        profile.setLastSeen(System.currentTimeMillis());
        profile.setLastSeenServer(Bukkit.getServerName());
        profile.setOnline(false);

        if (profile.isFrozen()) {
            String format = "{player} &csaiu do jogo durante um frozen.".replace("{player}", profile.getColoredUsername());
            CC.sendStaff(format);
        }

        if (profile.isLoaded()) {
            if (profile.getStaffOptions().isStaffModeEnabled()) {
                if (profile.getArmor() != null) {
                    player.getInventory().setArmorContents(profile.getArmor().toArray(new ItemStack[profile.getArmor().size()]));
                } else {
                    player.getInventory().setArmorContents(new ItemStack[4]);
                }
                if (profile.getInventory() != null) {
                    player.getInventory().setContents(profile.getInventory().toArray(new ItemStack[profile.getInventory().size()]));
                } else {
                    player.getInventory().setContents(new ItemStack[36]);
                }
                player.setGameMode(GameMode.SURVIVAL);
                player.saveData();
            }

            profile.setLastLocation(player.getLocation());

            if (disable) {
                profile.save();
            } else {
                TaskUtil.runAsync(profile::save);
            }
        }

        if (!disable) {
            RedisPlayerData playerData = new RedisPlayerData(player.getUniqueId(), player.getName());
            playerData.setLastAction(RedisPlayerData.LastAction.LEAVING_SERVER);
            playerData.setLastSeenServer(Bukkit.getServerName());
            playerData.setLastSeenAt(System.currentTimeMillis());

            core.getRedisCache().updatePlayerData(playerData);

            if (!player.hasPermission("core.staff")) return;

            core.getHollow().sendPacket(new PacketStaffLeaveNetwork(profile.getColoredUsername(), Bukkit.getServerName()));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        leavePlayer(event.getPlayer(), false);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("core.staff")) return;

        Profile profile = Profile.getByUuid(player.getUniqueId());
        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Idioma.SECURITY_AUTH_REQUIRED.format());
            event.setCancelled(true);
            return;
        }*/

        Iterator<Player> recipents = event.getRecipients().iterator();

        while (recipents.hasNext()) {
            Player recipient = recipents.next();
            Profile profileRecipient = Profile.getByUuid(recipient.getUniqueId());
            if (profileRecipient.getIgnoreList().contains(player.getUniqueId())) {
                recipents.remove();
            }
        }

        event.getRecipients().forEach(recipient -> {
            Profile profileRecipient = Profile.getByUuid(recipient.getUniqueId());
            if (profileRecipient.getIgnoreList().contains(player.getUniqueId())) {
                event.getRecipients().remove(recipient);
            }
        });

        if (profile.getStaffOptions().isStaffChatModeEnabled()) {
            core.getHollow().sendPacket(new PacketStaffChat(
                profile.getColoredUsername(), Bukkit.getServerName(), event.getMessage()));
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("core.staff") || event.getMessage().startsWith("/auth")) return;

        Profile profile = Profile.getByUuid(player.getUniqueId());
        /*if (!profile.isAuthenticated()) {
            player.sendMessage(Idioma.SECURITY_AUTH_REQUIRED.format());
            event.setCancelled(true);
        }*/
    }

    /*@EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ())
            return;

        Profile profile = Profile.getByUuid(e.getPlayer().getUniqueId());
        if (profile.isFrozen()) {
            e.getPlayer().teleport(e.getFrom());
            List<String> messages = Idioma.STAFF_FREEZE.formatLines();
            for (String message : messages) {
                e.getPlayer().sendMessage(message);
            }
        }
    }*/

    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(punishment.getKickMessage());
    }

}
