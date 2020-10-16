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
public class PacketStaffLeaveNetwork implements Packet {

    private String playerName;
    private String serverName;

    @Override
    public int id() {
        return 8;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerName", playerName)
            .addProperty("serverName", serverName)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        serverName = jsonObject.get("serverName").getAsString();
    }

}
