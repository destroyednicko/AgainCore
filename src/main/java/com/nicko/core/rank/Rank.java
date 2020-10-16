package com.nicko.core.rank;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.nicko.core.Core;
import com.nicko.core.network.packet.PacketDeleteRank;
import com.nicko.core.network.packet.PacketRefreshRank;
import com.nicko.core.profile.Profile;
import com.nicko.core.util.string.CC;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class Rank {

    @Getter
    private static Map<UUID, Rank> ranks = new HashMap<>();
    private static MongoCollection<Document> collection;

    private final UUID uuid;
    private final String displayName;
    private final List<String> permissions = new ArrayList<>();
    private final List<Rank> inherited = new ArrayList<>();
    @Setter
    private String prefix = "";
    @Setter
    private String color = "&f";
    @Setter
    private int priority;
    @Setter
    private boolean defaultRank;

    public Rank(String displayName) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;

        ranks.put(uuid, this);
    }

    public Rank(UUID uuid, String displayName) {
        this.uuid = uuid;
        this.displayName = displayName;

        ranks.put(uuid, this);
    }

    public Rank(UUID uuid, String displayName, String prefix, String color, int weight, boolean defaultRank) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.prefix = prefix;
        this.color = color;
        this.priority = weight;
        this.defaultRank = defaultRank;

        ranks.put(uuid, this);
    }

    public static void init() {
        collection = Core.get().getMongoDatabase().getCollection("ranks");

        Map<Rank, List<UUID>> inheritanceReferences = new HashMap<>();

        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document document = cursor.next();

                Rank rank = new Rank(UUID.fromString(document.getString("id")),
                    document.getString("name"));
                rank.load(document);
                rank.inherited.clear();

                List<UUID> ranksToInherit = new ArrayList<>();

                if (document.getString("inherits") != null) {
                    Core.GSON.<List<String>> fromJson(document.getString("inherits"), Core.LIST_STRING_TYPE)
                        .stream()
                        .map(UUID::fromString)
                        .forEach(ranksToInherit::add);
                }

                inheritanceReferences.put(rank, ranksToInherit);

                ranks.put(rank.getUuid(), rank);
            }
        }

        inheritanceReferences.forEach((rank, list) -> list.forEach(uuid -> {
            Rank inherited = ranks.get(uuid);

            if (inherited != null) {
                rank.getInherited().add(inherited);
            }
        }));

        getDefaultRank();
    }

    /**
     * Obtem um 'rank' por UUID, caso exista.
     *
     * @param uuid O UUID.
     * @return O 'rank' que combina com UUID, caso encontrado.
     */
    public static Rank getRankByUuid(UUID uuid) {
        return ranks.get(uuid);
    }

    /**
     * Obtem um 'rank' por nome, caso exista.
     *
     * @param name O nome.
     * @return O 'rank' que combina com o nome, caso encontrado.
     */
    public static Rank getRankByDisplayName(String name) {
        for (Rank rank : ranks.values()) {
            if (rank.getDisplayName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return null;
    }

    /**
     * Obtem o 'rank' padrão, ou cria um novo caso não exista.
     *
     * @return O 'rank' padrão, ou o novo, caso não exista.
     */
    public static Rank getDefaultRank() {
        for (Rank rank : ranks.values()) {
            if (rank.isDefaultRank()) {
                return rank;
            }
        }

        Rank defaultRank = new Rank("User");
        defaultRank.setDefaultRank(true);
        defaultRank.save();

        ranks.put(defaultRank.getUuid(), defaultRank);

        return defaultRank;
    }

    public boolean isDefaultRank() {
        return defaultRank;
    }

    public boolean hasPermission(String permission) {
        if (permissions.contains(permission)) {
            return true;
        }

        for (Rank rank : inherited) {
            if (rank.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public boolean canInherit(Rank rankToCheck) {
        if (inherited.contains(rankToCheck) || rankToCheck.inherited.contains(this)) {
            return false;
        }

        for (Rank rank : inherited) {
            if (!rank.canInherit(rankToCheck)) {
                return false;
            }
        }

        return true;
    }

    public List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>(this.permissions);

        for (Rank rank : inherited) {
            permissions.addAll(rank.getAllPermissions());
        }

        return permissions;
    }

    public void load() {
        load(collection.find(Filters.eq("id", uuid.toString())).first());
    }

    private void load(Document document) {
        if (document == null) {
            return;
        }

        prefix = ChatColor.translateAlternateColorCodes('&', document.getString("prefix"));
        color = CC.translate(document.getString("color"));
        priority = document.getInteger("priority");
        defaultRank = document.getBoolean("defaultRank");

        permissions.clear();
        permissions.addAll(Core.GSON.<List<String>> fromJson(document.getString("permissions"), Core.LIST_STRING_TYPE));

        inherited.clear();

        if (document.getString("inherits") != null) {
            Core.GSON.<List<String>> fromJson(document.getString("inherits"), Core.LIST_STRING_TYPE)
                .stream()
                .map(s -> Rank.getRankByUuid(UUID.fromString(s)))
                .filter(Objects::nonNull)
                .forEach(inherited::add);
        }
    }

    public void save() {
        Document document = new Document();
        document.put("id", uuid.toString());
        document.put("name", displayName);
        document.put("prefix", prefix.replace(String.valueOf(ChatColor.COLOR_CHAR), "&"));
        document.put("color", color);
        document.put("priority", priority);
        document.put("defaultRank", defaultRank);
        document.put("permissions", Core.GSON.toJson(permissions));
        document.put("inherits", Core.GSON.toJson(inherited
            .stream()
            .map(Rank::getUuid)
            .map(UUID::toString)
            .collect(Collectors.toList())));

        collection.replaceOne(Filters.eq("id", uuid.toString()), document,
            new ReplaceOptions().upsert(true));

        Core.get().getHollow().sendPacket(new PacketRefreshRank(uuid, displayName));
    }

    public void delete() {
        ranks.remove(uuid);
        collection.deleteOne(Filters.eq("id", uuid.toString()));

        Core.get().getHollow().sendPacket(new PacketDeleteRank(uuid));
    }

    public void refresh() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Profile profile = Profile.getByUuid(player.getUniqueId());

            if (this.equals(profile.getActiveRank())) {
                profile.setupBukkitPlayer(player);
            }
        }
    }

}
