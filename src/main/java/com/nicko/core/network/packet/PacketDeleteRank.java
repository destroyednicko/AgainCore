package com.nicko.core.network.packet;

import com.google.gson.JsonObject;
import com.nicko.hollow.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nicko.core.util.json.JsonChain;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketDeleteRank implements Packet {

    private UUID uuid;

    @Override
    public int id() {
        return 4;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("uuid", uuid.toString())
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
    }

}