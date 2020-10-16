package com.nicko.core.network.packet;

import com.google.gson.JsonObject;
import com.nicko.hollow.packet.Packet;
import com.nicko.core.profile.grant.Grant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nicko.core.util.json.JsonChain;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketDeleteGrant implements Packet {

    private UUID playerUuid;
    private Grant grant;

    @Override
    public int id() {
        return 3;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerUuid", playerUuid.toString())
            .add("grant", Grant.SERIALIZER.serialize(grant))
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerUuid = UUID.fromString(jsonObject.get("playerUuid").getAsString());
        grant = Grant.DESERIALIZER.deserialize(jsonObject.get("grant").getAsJsonObject());
    }

}
