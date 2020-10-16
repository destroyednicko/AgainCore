package com.nicko.core.profile.punishment;

import com.google.gson.JsonObject;
import com.nicko.core.util.json.JsonSerializer;

public class PunishmentJsonSerializer implements JsonSerializer<Punishment> {

    @Override
    public JsonObject serialize(Punishment punishment) {
        JsonObject object = new JsonObject();
        object.addProperty("uuid", punishment.getUuid().toString());
        object.addProperty("targetID", punishment.getTargetID().toString());
        object.addProperty("type", punishment.getType().name());
        object.addProperty("addedBy", punishment.getAddedBy() == null ? null : punishment.getAddedBy().toString());
        object.addProperty("addedAt", punishment.getAddedAt());
        object.addProperty("addedReason", punishment.getAddedReason());
        object.addProperty("expiration", punishment.getDuration());
        object.addProperty("removedBy", punishment.getRemovedBy() == null ? null : punishment.getRemovedBy().toString());
        object.addProperty("removedAt", punishment.getRemovedAt());
        object.addProperty("removedReason", punishment.getRemovedReason());
        object.addProperty("removed", punishment.isRemoved());
        object.addProperty("serverName", punishment.getServerName());
        return object;
    }

}
