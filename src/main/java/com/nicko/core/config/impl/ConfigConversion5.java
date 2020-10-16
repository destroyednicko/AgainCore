package com.nicko.core.config.impl;

import org.bukkit.configuration.file.FileConfiguration;
import com.nicko.core.config.ConfigConversion;

import java.io.File;
import java.io.IOException;

public class ConfigConversion5 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 5);
        fileConfiguration.set("GLOBAL_WHITELIST.KICK_MAINTENANCE", "&c • O servidor está em manutenção.\\nVerifique o nosso Discord para mais informações!");
        fileConfiguration.set("GLOBAL_WHITELIST.KICK_CLOSED_TESTING", "&c • O servidor está em whitelist, para ganhar acesso\\nTorne-se um beta-tester em nosso Discord.\\n&fhttps://discord.gg/againmc");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
