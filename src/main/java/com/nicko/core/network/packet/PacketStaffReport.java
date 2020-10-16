package com.nicko.core.network.packet;

import com.google.gson.JsonObject;
import com.nicko.hollow.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.nicko.core.util.json.JsonChain;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PacketStaffReport implements Packet {

    private String sentBy;
    private String accused;
    private String reason;
    private String serverName;

    @Override
    public int id() {
        return 11;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("sentBy", sentBy)
            .addProperty("accused", accused)
            .addProperty("reason", reason)
            .addProperty("serverName", serverName)
            .get();
    }

    @Override
    public void deserialize(JsonObject object) {
        sentBy = object.get("sentBy").getAsString();
        accused = object.get("accused").getAsString();
        reason = object.get("reason").getAsString();
        serverName = object.get("serverName").getAsString();
    }

}
