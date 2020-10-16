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
public class PacketAnticheatAlert implements Packet {

    private String playerName;
    private String flag;
    private int ping;
    private String serverName;

    @Override
    public int id() {
        return 16;
    }

    @Override
    public JsonObject serialize() {
        return new JsonChain()
            .addProperty("playerName", playerName)
            .addProperty("flag", flag)
            .addProperty("ping", ping)
            .addProperty("serverName", serverName)
            .get();
    }

    @Override
    public void deserialize(JsonObject jsonObject) {
        playerName = jsonObject.get("playerName").getAsString();
        flag = jsonObject.get("flag").getAsString();
        ping = jsonObject.get("ping").getAsInt();
        serverName = jsonObject.get("serverName").getAsString();
    }
}
