package com.nicko.core.profile;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.nicko.core.Core;
import com.nicko.core.CoreAPI;
import com.nicko.core.profile.conversation.ProfileConversations;
import com.nicko.core.profile.grant.Grant;
import com.nicko.core.profile.grant.event.GrantAppliedEvent;
import com.nicko.core.profile.grant.event.GrantExpireEvent;
import com.nicko.core.profile.notes.Note;
import com.nicko.core.profile.option.ProfileOptions;
import com.nicko.core.profile.option.enmus.Time;
import com.nicko.core.profile.prefix.Prefix;
import com.nicko.core.profile.punishment.Punishment;
import com.nicko.core.profile.punishment.PunishmentType;
import com.nicko.core.profile.staff.ProfileStaffOptions;
import com.nicko.core.profile.staff.events.PlayerVisibilityChangeEvent;
import com.nicko.core.profile.staff.events.StaffModeUpdateEvent;
import lombok.Data;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import com.nicko.core.rank.Rank;
import com.nicko.core.util.LocationUtil;
import com.nicko.core.util.hotbar.Hotbar;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.player.NameTagHandler;
import com.nicko.core.util.player.PlayerUtil;
import com.nicko.core.util.string.CC;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class Profile {
    @Getter
    private static Map<UUID, Profile> profiles = new HashMap<>();

    @Getter private static MongoCollection<Document> collection = Core.get().getMongoDatabase().getCollection("profiles");

    JsonParser parser = new JsonParser();
    private final UUID uuid;
    private String name;
    private boolean online;
    private Long firstSeen;
    private Long lastSeen;
    private String lastSeenServer;
    private boolean frozen;
    private Prefix prefix;

    private String currentAddress;
    private List<String> ipAddresses;

    private List<String> alts;

    private String countryName;
    private String countryCode;

    private final ProfileOptions options;
    private final ProfileStaffOptions staffOptions = new ProfileStaffOptions();
    private final ProfileConversations conversations;

    private Grant activeGrant;

    private final List<Punishment> punishments;

    private boolean loaded; // profile is loaded
    private boolean authenticated; // 2FA system

    private String secretToken; // 2FA secret token

    private ProfileCooldown chatCooldown;
    private ProfileCooldown requestCooldown;

    private List<com.nicko.core.profile.notes.Note> notes;
    private List<UUID> ignoreList;
    private List<String> permissions;

    private List<ItemStack> armor;
    private List<ItemStack> inventory;
    private Location lastLocation;

    public Profile(String username, UUID uuid) {
        this.uuid = uuid;
        this.name = username;
        this.punishments = new ArrayList<>();
        this.ipAddresses = new ArrayList<>();
        this.alts = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.ignoreList = new ArrayList<>();
        this.permissions = new ArrayList<>();
        this.options = new ProfileOptions();
        this.conversations = new ProfileConversations(this);
        this.chatCooldown = new ProfileCooldown(0);
        this.requestCooldown = new ProfileCooldown(0);

        load();
    }

    public static Profile getByUuid(UUID uuid) {
        if (profiles.containsKey(uuid)) {
            return profiles.get(uuid);
        }

        return new Profile(null, uuid);
    }

    public static Profile getByPlayer(Player player) {
        if (player != null) {
            return profiles.get(player.getUniqueId());
        } else return null;
    }

    public static Profile getByUsername(String username) {
        Player player = Bukkit.getPlayer(username);

        if (player != null) {
            return profiles.get(player.getUniqueId());
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);

        if (offlinePlayer.hasPlayedBefore()) {
            if (profiles.containsKey(offlinePlayer.getUniqueId())) {
                return profiles.get(offlinePlayer.getUniqueId());
            }

            return new Profile(offlinePlayer.getName(), offlinePlayer.getUniqueId());
        }

        UUID uuid = Core.get().getRedisCache().getUuid(username);

        if (uuid != null) {
            if (profiles.containsKey(uuid)) {
                return profiles.get(uuid);
            }

            return new Profile(username, uuid);
        }

        return null;
    }

    public static List<Profile> getByIpAddress(String ipAddress) {
        List<Profile> profiles = new ArrayList<>();
        Bson filter = Filters.eq("currentAddress", ipAddress);

        try (MongoCursor<Document> cursor = collection.find(filter).iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                profiles.add(new Profile(document.getString("username"),
                    UUID.fromString(document.getString("uuid"))));
            }
        }

        return profiles;
    }

    public boolean isAuthenticated() {
        return !Core.get().getMainConfig().getBoolean("SETTINGS.2FA_ENABLED") || authenticated;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public String getColoredUsername() {
        return activeGrant.getRank().getColor() + name;
    }

    public String getColor() {
        return String.valueOf(activeGrant.getRank().getColor());
    }

    public Punishment getActivePunishmentByType(PunishmentType type) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type && !punishment.isRemoved() && !punishment.hasExpired()) {
                return punishment;
            }
        }

        return null;
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int i = 0;

        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) i++;
        }

        return i;
    }

    public Rank getActiveRank() {
        return activeGrant.getRank();
    }


    public void hideStaff() {
        if (getPlayer().isOnline()) {
            if (this.getStaffOptions().isHideStaff()) {
                new PlayerVisibilityChangeEvent(true, this, Bukkit.getOnlinePlayers().stream()
                    .filter(online -> Profile.getByPlayer(online).getStaffOptions().isVanish() &&
                        Profile.getByPlayer(online).getStaffOptions().isHideStaff())
                    .collect(Collectors.toList()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        Profile onlineProfile = Profile.getByPlayer(online);
                        if (onlineProfile.getStaffOptions().isVanish() || onlineProfile.getStaffOptions().isStaffModeEnabled()) {
                            getPlayer().hidePlayer(online);
                        }
                    });
                }
            } else {
                new PlayerVisibilityChangeEvent(false, this, Lists.newArrayList(Bukkit.getOnlinePlayers()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(getPlayer()::showPlayer);
                }
            }
        }
    }

    public void toggleVanish() {
        if (getPlayer().isOnline()) {
            if (this.getStaffOptions().isVanish()) {
                if (this.getStaffOptions().isStaffModeEnabled()) {
                    getPlayer().getInventory().setItem(8, new ItemBuilder(Material.INK_SACK)
                        .name("&aVanish")
                        .durability(10)
                        .build());
                }
                new PlayerVisibilityChangeEvent(true, this, Bukkit.getOnlinePlayers().stream()
                    .filter(online -> !getByPlayer(online).getStaffOptions().isVanish() &&
                        getByPlayer(online).getStaffOptions().isStaffModeEnabled())
                    .collect(Collectors.toList()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        if (online == getPlayer()) return;
                        Profile profileOnline = getByPlayer(online);
                        if (profileOnline.getStaffOptions().isHideStaff()) {
                            online.hidePlayer(getPlayer());
                        } else if (!profileOnline.getStaffOptions().isVanish() &&
                            !profileOnline.getStaffOptions().isStaffModeEnabled()) {
                            online.hidePlayer(getPlayer());
                        } else {
                            getPlayer().showPlayer(online);
                        }
                    });
                }
            } else {
                new PlayerVisibilityChangeEvent(false, this, new ArrayList<>(Bukkit.getOnlinePlayers()))
                    .call();
                new PlayerVisibilityChangeEvent(true, this, Bukkit.getOnlinePlayers().stream()
                    .filter(online -> getByPlayer(online).getStaffOptions().isVanish() && !getPlayer().isOp())
                    .collect(Collectors.toList()))
                    .call();
                if (Core.get().getMainConfig().getBoolean("STAFF.VISIBILITY_ENGINE")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        if (online == getPlayer()) return;
                        Profile profileOnline = getByPlayer(online);

                        online.showPlayer(getPlayer());
                        if (profileOnline.getStaffOptions().isVanish() && !getPlayer().isOp()) {
                            getPlayer().hidePlayer(online);
                        }

                    });
                }
                if (this.getStaffOptions().isStaffModeEnabled()) {
                    getPlayer().getInventory().setItem(8, new ItemBuilder(Material.INK_SACK).name("&cVanish").durability(1).build());
                }
            }
            getPlayer().updateInventory();
        }
    }

    public void toggleStaffMode() {
        if (getPlayer().isOnline()) {
            new StaffModeUpdateEvent(getPlayer(), staffOptions.isStaffModeEnabled()).call();
            if (this.getStaffOptions().isStaffModeEnabled()) {
                this.setArmor(Arrays.asList(getPlayer().getInventory().getArmorContents()));
                this.setInventory(Arrays.asList(getPlayer().getInventory().getContents()));
                PlayerUtil.reset(getPlayer(), true);
                getPlayer().setGameMode(GameMode.CREATIVE);
                getPlayer().getInventory().setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS)
                    .glow().build());
                Hotbar.STAFFMODE.giveItems(getPlayer());
                getPlayer().chat("/vanish true");
                if (Core.get().getMainConfig().getBoolean("COMMON.COLOR-TAG-ENABLED")) {
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        NameTagHandler.addToTeam(online, getPlayer(), CC.translate("&7[S] " + CoreAPI.getColorOfPlayer(getPlayer())), false);
                    }
                }
            } else {
                PlayerUtil.reset(getPlayer(), true);

                if (this.getArmor() != null) {
                    getPlayer().getInventory().setArmorContents(this.getArmor().toArray(new ItemStack[this.getArmor().size()]));
                } else {
                    getPlayer().getInventory().setArmorContents(new ItemStack[4]);
                }
                if (this.getInventory() != null) {
                    getPlayer().getInventory().setContents(this.getInventory().toArray(new ItemStack[this.getInventory().size()]));
                } else {
                    getPlayer().getInventory().setContents(new ItemStack[36]);
                }

                this.setArmor(null);
                this.setInventory(null);
                getPlayer().chat("/vanish false");
                if (Core.get().getMainConfig().getBoolean("COMMON.COLOR-TAG-ENABLED")) {
                    Bukkit.getOnlinePlayers().forEach(online -> {
                        Profile profileOnline = this.getByPlayer(online);
                        if (profileOnline.getStaffOptions().isStaffModeEnabled()) {
                            NameTagHandler.addToTeam(getPlayer(), online, CC.translate("&7[S] " + profileOnline.getColor()), false);
                        } else {
                            NameTagHandler.addToTeam(getPlayer(), online, CC.translate(profileOnline.getColor()), false);
                        }
                        if (this.getStaffOptions().isStaffModeEnabled()) {
                            NameTagHandler.addToTeam(online, getPlayer(), CC.translate("&7[S] " + this.getColor()), false);
                        } else {
                            NameTagHandler.addToTeam(online, getPlayer(), CC.translate(this.getColor()), false);
                        }
                    });
                }
            }

            new PlayerVisibilityChangeEvent(true, this, Bukkit.getOnlinePlayers().stream()
                .filter(online -> this.getByPlayer(online).getStaffOptions().isHideStaff()).collect(Collectors.toList())).call();
            if (Core.get().getMainConfig().getBoolean("COMMON.COLOR-TAG-ENABLED")) {
                Bukkit.getOnlinePlayers().forEach(online -> {
                    Profile profileOnline = this.getByPlayer(online);
                    if (profileOnline.getStaffOptions().isStaffModeEnabled()) {
                        NameTagHandler.addToTeam(getPlayer(), online, CC.translate("&7[S] " + profileOnline.getColor()), false);
                    } else {
                        NameTagHandler.addToTeam(getPlayer(), online, CC.translate(profileOnline.getColor()), false);
                    }
                    if (this.getStaffOptions().isStaffModeEnabled()) {
                        NameTagHandler.addToTeam(online, getPlayer(), CC.translate("&7[S] " + this.getColor()), false);
                    } else {
                        NameTagHandler.addToTeam(online, getPlayer(), CC.translate(this.getColor()), false);
                    }
                });
            }
            getPlayer().updateInventory();
        }
    }


    /**
     * Verifica e atualiza qualquer 'grant' com alterações pendentes.
     */
    public void checkGrants() {
        Player player = getPlayer();

        if (activeGrant != null) {
            if (!activeGrant.isRemoved() && activeGrant.hasExpired()) {
                activeGrant.setRemovedAt(System.currentTimeMillis());
                activeGrant.setRemovedReason("Grant expirado");
                activeGrant.setRemoved(true);

                if (player != null) {
                    new GrantExpireEvent(getPlayer(), activeGrant).call();
                }
                activeGrant = null;
            }
        }

        // Gera um 'grant' default, caso não exista um.
        if (activeGrant == null || activeGrant.isRemoved()) {
            Grant defaultGrant = new Grant(UUID.randomUUID(), Rank.getDefaultRank(), null,
                System.currentTimeMillis(), "Default", Long.MAX_VALUE);

            activeGrant = defaultGrant;

            if (player != null) {
                setupBukkitPlayer(getPlayer());
                new GrantAppliedEvent(getPlayer(), defaultGrant).call();
            }
        }
    }


    public void addAlt(Profile altProfile) {
        if (!this.alts.contains(altProfile.getUuid().toString())) {
            this.alts.add(altProfile.getUuid().toString());
        }
    }

    public boolean removeNote(int id) {
        com.nicko.core.profile.notes.Note note = notes.stream().filter(note1 -> note1.getId() == id).findFirst().orElse(null);
        return notes.remove(note);
    }

    public com.nicko.core.profile.notes.Note getNote(int id) {
        return notes.stream().filter(note1 -> note1.getId() == id).findFirst().orElse(null);
    }

    public void setupBukkitPlayer(Player player) {
        if (player == null) {
            return;
        }

        // Limpa todas as permissões definidas para o jogador por este plugin
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null ||
                !attachmentInfo.getAttachment().getPlugin().equals(Core.get())) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions()
                .forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(Core.get());

        for (String permission : activeGrant.getRank().getAllPermissions()) {
            attachment.setPermission(permission, true);
        }

        for (String permission : permissions) {
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();

        String prefixSting = "";

        if (this.prefix != null) {
            prefixSting = prefix.getPrefix();
        }

        String displayName = prefixSting + activeGrant.getRank().getPrefix() + player.getName();
        String coloredName = CoreAPI.getColoredName(player);

        if (coloredName.length() > 16) {
            coloredName = coloredName.substring(0, 15);
        }

        player.setDisplayName(displayName);

    }


    public void updatePermissions() {
        Player player = Bukkit.getPlayer(uuid);
        // Limpa todas as permissões definidas para o jogador por este plugin
        for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
            if (attachmentInfo.getAttachment() == null || attachmentInfo.getAttachment().getPlugin() == null ||
                !attachmentInfo.getAttachment().getPlugin().equals(Core.get())) {
                continue;
            }

            attachmentInfo.getAttachment().getPermissions()
                .forEach((permission, value) -> attachmentInfo.getAttachment().unsetPermission(permission));
        }

        PermissionAttachment attachment = player.addAttachment(Core.get());

        for (String permission : activeGrant.getRank().getAllPermissions()) {
            attachment.setPermission(permission, true);
        }

        for (String permission : permissions) {
            attachment.setPermission(permission, true);
        }

        player.recalculatePermissions();
    }

    public void updateDisplayName() {
        Player player = Bukkit.getPlayer(uuid);
        String prefixSting = "";

        if (this.prefix != null) {
            prefixSting = prefix.getPrefix();
        }

        String displayName = prefixSting + activeGrant.getRank().getPrefix() + player.getName();
        String coloredName = CoreAPI.getColoredName(player);

        if (coloredName.length() > 16) {
            coloredName = coloredName.substring(0, 15);
        }

        player.setDisplayName(displayName);
    }

    public void load() {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (name == null) {
                name = document.getString("playerName");
            }

            online = document.getBoolean("online", false);
            firstSeen = document.getLong("firstSeen");
            lastSeen = document.getLong("lastSeen");
            lastSeenServer = document.getString("lastSeenServer");
            secretToken = document.getString("secretToken");
            authenticated = document.getBoolean("authenticated");
            currentAddress = document.getString("currentAddress");
            ipAddresses = Core.GSON.fromJson(document.getString("ipAddresses"), Core.LIST_STRING_TYPE);
            alts = document.get("alts", List.class);
            if (alts == null) {
                alts = new ArrayList<>();
            }

            if (document.getString("permissions") != null) {
                permissions = Core.GSON.fromJson(document.getString("permissions"), Core.LIST_STRING_TYPE);
            }

            countryName = document.getString("countryName");
            countryCode = document.getString("countryCode");
            if (document.containsKey("prefix") && Prefix.getPrefixByName(document.getString("prefix")) != null) {
                prefix = Prefix.getPrefixByName(document.getString("prefix"));
            }

            if (document.getString("lastLocation") != null) {
                lastLocation = LocationUtil.deserialize(document.getString("lastLocation"));
            }

            Document grantDocument = (Document) document.get("grant");

            Grant grant = new Grant(
                UUID.fromString(grantDocument.getString("uuid")),
                Rank.getRankByDisplayName(grantDocument.getString("rank")),
                null,
                grantDocument.getLong("addedAt"),
                grantDocument.getString("addedReason"),
                grantDocument.getLong("duration"));

            if (grantDocument.containsKey("addedBy")) {
                grant.setAddedBy(UUID.fromString(grantDocument.getString("addedBy")));
            }
            if (grantDocument.containsKey("removedBy")) {
                grant.setRemovedBy(UUID.fromString(grantDocument.getString("removedBy")));
            }
            if (grantDocument.containsKey("removedAt")) {
                grant.setRemovedAt(grantDocument.getLong("removedAt"));
            }
            if (grantDocument.containsKey("removedReason")) {
                grant.setRemovedReason(grantDocument.getString("removedReason"));
            }
            if (grantDocument.containsKey("removed")) {
                grant.setRemoved(grantDocument.getBoolean("removed"));
            } else {
                grant.setRemoved(false);
            }

            activeGrant = grant;

            Document optionsDocument = (Document) document.get("options");
            options.publicChatEnabled(optionsDocument.getBoolean("publicChatEnabled"));
            options.receivingNewConversations(optionsDocument.getBoolean("receivingNewConversations"));
            options.playingMessageSounds(optionsDocument.getBoolean("playingMessageSounds"));
            options.color(ChatColor.valueOf(optionsDocument.getString("color")));
            options.scoreboard(optionsDocument.getBoolean("scoreboard"));
            options.time(Time.valueOf(optionsDocument.getString("time")));
            options.tipsAnnounce(optionsDocument.getBoolean("tipsAnnounce"));

            if (document.getString("notes") != null) {
                JsonArray notesArray = new JsonParser().parse(document.getString("notes")).getAsJsonArray();

                for (JsonElement noteData : notesArray) {
                    // Transforma num objeto
                    com.nicko.core.profile.notes.Note note = com.nicko.core.profile.notes.Note.DESERIALIZER.deserialize(noteData.getAsJsonObject());

                    if (note != null) {
                        this.notes.add(note);
                    }
                }
            }

            if (document.getString("ignorelist") != null) {
                JsonArray notesArray = new JsonParser().parse(document.getString("ignorelist")).getAsJsonArray();

                for (JsonElement noteData : notesArray) {
                    // Transforma num objeto
                    UUID uuid = UUID.fromString(noteData.getAsString());

                    if (uuid != null) {
                        this.ignoreList.add(uuid);
                    }
                }
            }

            JsonArray punishmentList = new JsonParser().parse(document.getString("punishments")).getAsJsonArray();

            for (JsonElement punishmentData : punishmentList) {
                // Transforma num objeto
                Punishment punishment = Punishment.DESERIALIZER.deserialize(punishmentData.getAsJsonObject());

                if (punishment != null) {
                    this.punishments.add(punishment);
                }
            }
        }

        checkGrants();

        // Define o 'loaded' para true
        loaded = true;
    }

    public void save() {
        Document document = new Document();
        document.put("playerName", name);
        document.put("playerNameLowerCase", name.toLowerCase());
        document.put("lastServer", Bukkit.getServerName());
        document.put("uuid", uuid.toString());
        document.put("online", online);
        document.put("firstSeen", firstSeen);
        document.put("lastSeen", lastSeen);
        document.put("lastSeenServer", lastSeenServer);
        document.put("secretToken", secretToken);
        document.put("authenticated", authenticated);
        document.put("currentAddress", currentAddress);
        document.put("ipAddresses", Core.GSON.toJson(ipAddresses, Core.LIST_STRING_TYPE));

        document.append("alts", this.alts);


        document.put("permissions", Core.GSON.toJson(permissions, Core.LIST_STRING_TYPE));
        document.put("countryName", countryName);
        document.put("countryCode", countryCode);
        document.put("rank", activeGrant.getRank().getDisplayName());

        document.put("grant", activeGrant.serialize());

        Document optionsDocument = new Document();
        optionsDocument.put("publicChatEnabled", options.publicChatEnabled());
        optionsDocument.put("receivingNewConversations", options.receivingNewConversations());
        optionsDocument.put("playingMessageSounds", options.playingMessageSounds());
        optionsDocument.put("color", options.color().name());
        optionsDocument.put("scoreboard", options.scoreboard());
        optionsDocument.put("time", options.time().name());
        optionsDocument.put("tipsAnnounce", options.tipsAnnounce());
        document.put("options", optionsDocument);

        if (prefix != null) {
            document.put("prefix", prefix.getName());
        }

        if (lastLocation != null) {
            document.put("lastLocation", LocationUtil.serialize(lastLocation));
        }

        JsonArray ignoreArray = new JsonArray();
        for (UUID uuid : ignoreList) {
            String uuidn = uuid.toString();
            JsonObject object = (JsonObject)parser.parse(uuidn);
            ignoreArray.add(object);
        }

        document.put("ignorelist", ignoreArray.toString());

        JsonArray notesArray = new JsonArray();
        for (com.nicko.core.profile.notes.Note note : notes) {
            notesArray.add(Note.SERIALIZER.serialize(note));
        }

        document.put("notes", notesArray.toString());

        document.put("rank", getActiveGrant().getRank().getDisplayName());

        JsonArray punishmentList = new JsonArray();

        for (Punishment punishment : this.punishments) {
            punishmentList.add(Punishment.SERIALIZER.serialize(punishment));
        }

        document.put("punishments", punishmentList.toString());

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

}
