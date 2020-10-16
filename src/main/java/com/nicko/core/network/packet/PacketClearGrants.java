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
public class PacketClearGrants implements Packet {

    private UUID uuid;

    @Override
    public int id() {
        return 13;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("uuid", uuid.toString())
            .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        uuid = UUID.fromString(object.get("uuid").getAsString());
    }

}
