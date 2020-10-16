package com.nicko.core.profile.staff.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import com.nicko.core.Core;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.menu.Menu;
import com.nicko.core.util.player.NameTagHandler;
import com.nicko.core.util.string.CC;

public class StaffModeListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);
        if (Core.get().getMainConfig().getBoolean("STAFF.STAFFMODE-ENABLED") && player.hasPermission("core.staff")) {
            player.chat("/staffmode");
        }
        Bukkit.getOnlinePlayers().forEach(online -> {
            Profile profileOnline = Profile.getByPlayer(online);
            if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                if (profileOnline.getStaffOptions().isVanish() && !profile.getStaffOptions().isStaffModeEnabled()) {
                    online.hidePlayer(player);
                }
            }
        });
        if (Core.get().getMainConfig().getBoolean("COMMON.COLOR-TAG-ENABLED")) {
            Bukkit.getOnlinePlayers().forEach(online -> {
                Profile profileOnline = Profile.getByPlayer(online);
                if (profileOnline.getStaffOptions().isStaffModeEnabled()) {
                    NameTagHandler.addToTeam(player, online, CC.translate("&7[S] " + profileOnline.getColor()), false);
                } else {
                    NameTagHandler.addToTeam(player, online, CC.translate(profileOnline.getColor()), false);
                }
                if (profile.getStaffOptions().isStaffModeEnabled()) {
                    NameTagHandler.addToTeam(online, player, CC.translate("&7[S] " + profile.getColor()), false);
                } else {
                    NameTagHandler.addToTeam(online, player, CC.translate(profile.getColor()), false);
                }
            });
        }
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickupItem(EntityTargetEvent event) {
        if (event.getTarget() instanceof Player) {
            Player player = (Player) event.getTarget();
            Profile profile = Profile.getByPlayer(player);

            if (profile.getStaffOptions().isStaffModeEnabled()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);

            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
                return;
            }

            if (event.getClickedBlock() == null) {
                return;
            }

            if (event.getClickedBlock().getType() != Material.CHEST) {
                return;
            }

            if (!(event.getClickedBlock().getState() instanceof Chest)) {
                return;
            }

            Chest chest = (Chest) event.getClickedBlock().getState();

            Inventory inventory = chest.getInventory();
            Inventory newInventory = Bukkit.createInventory(null,
                inventory.getSize(), "Staff Inventory");
            newInventory.setContents(inventory.getContents());

            player.openInventory(newInventory);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Profile profile = Profile.getByPlayer(player);

        if (Menu.currentlyOpenedMenus.containsKey(player.getName())) {
            return;
        }

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getDamager();
        Profile profile = Profile.getByPlayer(player);

        if (profile.getStaffOptions().isStaffModeEnabled()) {
            event.setCancelled(true);
        }
    }

}
