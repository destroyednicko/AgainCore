package com.nicko.core.profile.punishment;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import com.nicko.core.Core;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BanReason {
    @Getter private static MongoCollection<Document> collection = Core.get().getMongoDatabase().getCollection("banreasons");

    @Getter private static List<BanReason> banReasons = Lists.newArrayList();

    private String reason;
    private String duration;
    private PunishmentType punishmentType;
    private boolean admit;

    {
        banReasons.add(this);
    }

    public static BanReason getByReason(String reason) {
        return banReasons.stream().filter(banReason -> banReason.getReason().equalsIgnoreCase(reason)).findFirst().orElse(null);
    }

    public static void load() {
        try (final MongoCursor<Document> cursor = collection.find().iterator()) {
            cursor.forEachRemaining(document -> {
                new BanReason(document.getString("reason"),
                    document.getString("duration"),
                    PunishmentType.getByName(document.getString("punishmentType")),
                    document.getBoolean("admit"));
            });
        }
    }

    public void save() {
        Document document = new Document();
        document.put("reason", this.reason);
        document.put("duration", this.duration);
        document.put("punishmentType", punishmentType.getReadable());
        document.put("admit", this.admit);
        collection.replaceOne(Filters.eq("reason", this.reason), document, new ReplaceOptions().upsert(true));
    }
}
