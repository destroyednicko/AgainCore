package com.nicko.core.profile.prefix;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {

    COUNTRY("País"),
    SYMBOL("Símbolo"),
    TEXT("Texto"),
    PARTNER("Parceiro");

    private String name;

    public static Category getByName(String name) {
        for (Category category : values()) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}
