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
public class PacketStaffAuth implements Packet {

    private String playerName;
    private String serverName;
    private boolean success;

    public int id() {
        return 15;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerName", playerName)
            .addProperty("serverName", serverName)
            .addProperty("success", success)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName = jsonObject.get("serverName").getAsString();
        success = jsonObject.get("success").getAsBoolean();
    }

}
