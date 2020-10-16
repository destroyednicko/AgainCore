package com.nicko.core.config.impl;

import org.bukkit.configuration.file.FileConfiguration;
import com.nicko.core.config.ConfigConversion;

import java.io.File;
import java.io.IOException;

public class ConfigConversion2 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 2);
        //fileConfiguration.set("SETTINGS.UPDATE_PLAYER_LIST_NAME", true);

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
