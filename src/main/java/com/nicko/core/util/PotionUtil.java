package com.nicko.core.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.potion.PotionEffectType;

public class PotionUtil {

    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
            return "Resistencia ao Fogo";
        } else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
            return "Velocidade";
        } else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
            return "Fraqueza";
        } else if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
            return "Lentidão";
        } else {
            return StringUtils.capitalize(potionEffectType.getName().replace("_", " ").toLowerCase());
        }
    }

}
