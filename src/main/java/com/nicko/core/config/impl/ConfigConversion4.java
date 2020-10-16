package com.nicko.core.config.impl;

import org.bukkit.configuration.file.FileConfiguration;
import com.nicko.core.config.ConfigConversion;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ConfigConversion4 implements ConfigConversion {

    @Override
    public void convert(File file, FileConfiguration fileConfiguration) {
        fileConfiguration.set("CONFIG_VERSION", 4);

        fileConfiguration.set("STAFF.REPORT_BROADCAST", Arrays.asList(
            "&9[R] &b[{3}] &r{1} &7foi denunciado por &r{0}",
            "   &9Motivo&7: {2}"
        ));

        fileConfiguration.set("STAFF.REQUEST_BROADCAST", Arrays.asList(
            "&9[R] &b[{2}] &r{0} &7solicitou assistência",
            "   &9Motivo&7: {1}"
        ));

        fileConfiguration.set("STAFF.REQUEST_SUBMITTED", "&a • Seu pedido foi enviado para todos os membros da equipe online, e logo será respondido.");

        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
