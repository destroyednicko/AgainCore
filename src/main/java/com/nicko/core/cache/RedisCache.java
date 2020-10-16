package com.nicko.core.cache;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicko.core.Core;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

public class RedisCache {

    private Core core;

    public RedisCache(Core core) {
        this.core = core;
    }

    /**
     * Obtem o UUID do player
     *
     * @param name o nome do jogador.
     * @return O {@link UUID} obtido da Mojang API.
     * @throws IOException se o request for mal-sucedido
     */
    private static UUID getFromMojang(String name) throws IOException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = reader.readLine();

        if (line == null) {
            return null;
        }

        String[] id = line.split(",");

        String part = id[0];
        part = part.substring(7, 39);

        return UUID.fromString(part.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
    }

    /**
     * Obtem o UUID do player.
     *
     * @param name o nome do jogador.
     * @return O {@link UUID} do player se encontrado no Cache ou obtido anteriormente na Mojang API. Caso contrário, nulo.
     */
    public UUID getUuid(String name) {
        if (core.getServer().isPrimaryThread()) {
            throw new IllegalStateException("Impossivel de consultar no thread principal (Redis profile cache)");
        }

        try (Jedis jedis = core.getJedisPool().getResource()) {
            String uuid = jedis.hget("uuid-cache:name-to-uuid", name.toLowerCase());

            if (uuid != null) {
                return UUID.fromString(uuid);
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Impossivel de conectar ao Redis");
            e.printStackTrace();
        }

        try {
            UUID uuid = getFromMojang(name);

            if (uuid != null) {
                updateNameAndUUID(name, uuid);
                return uuid;
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Impossivel de buscar na Mojang API");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Atualiza o usuário e o UUID do jogador no Cache.
     *
     * @param name O usuário do jogador.
     * @param uuid O {@link UUID} do jogador.
     * @throws IllegalStateException se executado no {@link Thread} primário.
     */
    public void updateNameAndUUID(String name, UUID uuid) {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Impossivel de executar uma requisição redis no thread principal!");
        }

        try (Jedis jedis = core.getJedisPool().getResource()) {
            jedis.hset("uuid-cache:name-to-uuid", name.toLowerCase(), uuid.toString());
            jedis.hset("uuid-cache:uuid-to-name", uuid.toString(), name);
        }
    }

    /**
     * Obtem o {@link RedisPlayerData} do player
     *
     * @param uuid O {@link UUID} do jogador.
     * @return O {@link RedisPlayerData} do jogador.
     * @throws IllegalStateException se executado no {@link Thread} primário.
     */
    public RedisPlayerData getPlayerData(UUID uuid) {
        if (Bukkit.isPrimaryThread()) {
            throw new IllegalStateException("Impossivel de executar uma requisição redis no thread principal!");
        }

        try (Jedis jedis = core.getJedisPool().getResource()) {
            String data = jedis.hget("player-data", uuid.toString());

            if (data == null) {
                return null;
            }

            try {
                JsonObject dataJson = new JsonParser().parse(data).getAsJsonObject();
                return new RedisPlayerData(dataJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Atualiza o {@link RedisPlayerData} do jogador.
     *
     * @param playerData o {@link RedisPlayerData} do jogador.
     */
    public void updatePlayerData(RedisPlayerData playerData) {
        try (Jedis jedis = core.getJedisPool().getResource()) {
            jedis.hset("player-data", playerData.getUuid().toString(), playerData.getJson().toString());
        }
    }

}
