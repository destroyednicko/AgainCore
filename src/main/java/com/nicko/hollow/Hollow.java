package com.nicko.hollow;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicko.hollow.packet.Packet;
import com.nicko.hollow.packet.handler.IncomingPacketHandler;
import com.nicko.hollow.packet.handler.PacketExceptionHandler;
import com.nicko.hollow.packet.listener.PacketListener;
import com.nicko.hollow.packet.listener.PacketListenerData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class Hollow {

    // tá feião
    
    private static JsonParser PARSER = new JsonParser();

    private final String channel;
    private JedisPool jedisPool;
    private JedisPubSub jedisPubSub;
    private List<PacketListenerData> packetListeners;
    private Map<Integer, Class> idToType = new HashMap<>();
    private Map<Class, Integer> typeToId = new HashMap<>();

    public Hollow(String channel, String host, int port, String password) {
        this.channel = channel;
        this.packetListeners = new ArrayList<>();

        this.jedisPool = new JedisPool(host, port);

        if (password != null && !password.equals("")) {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.auth(password);
                System.out.println("[Hollow] Autenticando..");
            }
        }

        this.setupPubSub();
    }

    public void sendPacket(Packet packet) {
        sendPacket(packet, null);
    }

    public void sendPacket(Packet packet, PacketExceptionHandler exceptionHandler) {
        try {

            final JsonObject object = packet.serialize();

            if (object == null) {
                throw new IllegalStateException("O pacote não pode gerar 'null datas' serializados");
            }

            try (Jedis jedis = this.jedisPool.getResource()) {

                System.out.println("[Hollow] Tentando publicar pacote..");

                try {

                    jedis.publish(this.channel, packet.id() + ";" + object.toString());
                    System.out.println("[Hollow] Pacote publicado..");

                } catch (Exception ex) {
                    System.out.println("[Hollow] Falha ao publicar pacote..");
                    ex.printStackTrace();
                }

            }
        } catch (Exception e) {
            if (exceptionHandler != null) {
                exceptionHandler.onException(e);
            }
        }

    }

    public Packet buildPacket(int id) {
        if (!idToType.containsKey(id)) {
            throw new IllegalStateException("Um pacote com esse ID não existe");
        }

        try {
            return (Packet) idToType.get(id).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("Não foi possível instanciar o tipo de pacote");
    }

    public void registerPacket(Class clazz) {
        try {
            int id = (int) clazz.getDeclaredMethod("id").invoke(clazz.newInstance(), null);

            if (idToType.containsKey(id) || typeToId.containsKey(clazz)) {
                throw new IllegalStateException("Um pacote com esse ID já foi registrado");
            }

            idToType.put(id, clazz);
            typeToId.put(clazz, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerListener(PacketListener packetListener) {
        methodLoop:
        for (Method method : packetListener.getClass().getDeclaredMethods()) {
            if (method.getDeclaredAnnotation(IncomingPacketHandler.class) != null) {
                Class packetClass = null;

                if (method.getParameters().length > 0) {
                    if (Packet.class.isAssignableFrom(method.getParameters()[0].getType())) {
                        packetClass = method.getParameters()[0].getType();
                    }
                }

                if (packetClass != null) {
                    this.packetListeners.add(new PacketListenerData(packetListener, method, packetClass));
                }
            }
        }
    }

    private void setupPubSub() {
        System.out.println("[Hollow] Configurando PubSup..");

        this.jedisPubSub = new JedisPubSub() {

            @Override
            public void onMessage(String channel, String message) {

                if (channel.equalsIgnoreCase(Hollow.this.channel)) {

                    try {


                       // System.out.println(message);

                        String[] args = message.split(";");

                        Integer id = Integer.valueOf(args[0]);

                        Packet packet = buildPacket(id);

                        if (packet != null) {
                            //System.out.println("packet " + args[1]);
                            packet.deserialize(PARSER.parse(args[1]).getAsJsonObject());

                            for (PacketListenerData data : packetListeners) {
                                if (data.matches(packet)) {
                                    data.getMethod().invoke(data.getInstance(), packet);
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("[Hollow] Falha ao dar 'handle' na mensagem");
                        e.printStackTrace();
                    }
                }
            }

        };

        ForkJoinPool.commonPool().execute(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.subscribe(this.jedisPubSub, channel);
                System.out.println("[Hollow] Inscrevendo no canal...");
            } catch (Exception exception) {
                System.out.println("[Hollow] Falha ao inscrever no canal..");
                exception.printStackTrace();
            }
        });
    }

}
