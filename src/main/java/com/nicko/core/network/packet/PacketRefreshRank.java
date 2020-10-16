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
public class PacketRefreshRank implements Packet {

    private UUID uuid;
    private String name;

    @Override
    public int id() {
        return 5;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("uuid", uuid.toString())
            .addProperty("name", name)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        name = jsonObject.get("name").getAsString();
    }

}