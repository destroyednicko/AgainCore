package com.nicko.core.config.impl;

import org.bukkit.configuration.file.FileConfiguration;
import com.nicko.core.config.ConfigConversion;

import java.io.File;
import java.io.IOException;

public class ConfigConversion1 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 1);
        fileConfiguration.set("CHAT.FORMAT", "%1$s&r: %2$s");
        fileConfiguration.set("CHAT.CLEAR_CHAT_BROADCAST", "&e • O chat foi limpo por &r{0}");
        fileConfiguration.set("CHAT.CLEAR_CHAT_FOR_STAFF", false);
        fileConfiguration.set("CHAT.MUTE_CHAT_BROADCAST", "&e • O chat foi {0} por &r{1}");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
