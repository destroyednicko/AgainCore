package com.nicko.core.config.impl;

import org.bukkit.configuration.file.FileConfiguration;
import com.nicko.core.config.ConfigConversion;

import java.io.File;
import java.io.IOException;

public class ConfigConversion3 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 3);
        fileConfiguration.set("CONVERSATION.SEND_MESSAGE", "&7[Para &r{5}{3}&7] %MSG%");
        fileConfiguration.set("CONVERSATION.RECEIVE_MESSAGE", "&7[De &r{5}{3}&7] %MSG%");
        fileConfiguration.set("OPTIONS.GLOBAL_CHAT_ENABLED", "&e • Agora o chat global está visível.");
        fileConfiguration.set("OPTIONS.GLOBAL_CHAT_DISABLED", "&c • Agora o chat global está oculto.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGES_ENABLED", "&a • Agora as mensagens privadas estão habilitadas.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGES_DISABLED", "&c • Agora as mensagens privadas estão desabilitadas. Caso você envie uma mensagem para alguém, ele não será capaz de te responder.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGE_SOUNDS_ENABLED", "&e • Agora as notificações de mensagens privadas estão habilitadas.");
        fileConfiguration.set("OPTIONS.PRIVATE_MESSAGE_SOUNDS_DISABLED", "&c • Agora as notificações de mensagens privadas estão desabilitadas.");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
