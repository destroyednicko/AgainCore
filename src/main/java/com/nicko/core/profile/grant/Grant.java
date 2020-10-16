package com.nicko.core.profile.grant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import com.nicko.core.rank.Rank;
import com.nicko.core.util.TimeUtil;

import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class Grant {

    public static GrantJsonSerializer SERIALIZER = new GrantJsonSerializer();
    public static GrantJsonDeserializer DESERIALIZER = new GrantJsonDeserializer();

    private final UUID uuid;
    private final Rank rank;
    private final long addedAt;
    private final String addedReason;
    private final long duration;
    private UUID addedBy;
    private UUID removedBy;
    private long removedAt;
    private String removedReason;
    private boolean removed;

    public Grant(UUID uuid, Rank rank, UUID addedBy, long addedAt, String addedReason, long duration) {
        this.uuid = uuid;
        this.rank = rank;
        this.addedBy = addedBy;
        this.addedAt = addedAt;
        this.addedReason = addedReason;
        this.duration = duration;
    }

    public boolean isPermanent() {
        return duration == Long.MAX_VALUE;
    }

    public boolean hasExpired() {
        return (!isPermanent()) && (System.currentTimeMillis() >= addedAt + duration);
    }

    public String getExpiresAtDate() {
        if (duration == Long.MAX_VALUE) {
            return "Never";
        }

        return TimeUtil.dateToString(new Date(addedAt + duration), "&7");
    }

    public String getDurationText() {
        if (removed) {
            return "Removido";
        }

        if (isPermanent()) {
            return "Permanente";
        }

        return TimeUtil.millisToRoundedTime(duration);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Grant && ((Grant) object).uuid.equals(uuid);
    }

    public Document serialize() {
        Document document = new Document();
        document.put("uuid", uuid.toString());
        document.put("rank", rank.getDisplayName());
        document.put("addedAt", addedAt);
        document.put("addedReason", addedReason);
        document.put("duration", duration);
        if (addedBy != null) document.put("addedBy", addedBy.toString());
        if (removedBy != null) document.put("removedBy", removedBy.toString());
        if (removedAt > 0) document.put("removedAt", removedAt);
        if (removedReason != null) document.put("removedReason", removedReason);
        if (removedReason != null) document.put("removed", removedReason);
        return document;
    }
}
