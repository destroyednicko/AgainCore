package com.nicko.shinigami.command.adapter.impl;

import com.nicko.shinigami.command.adapter.CommandTypeAdapter;

public class IntegerTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(final String string, final Class<T> type) {
        return type.cast(Integer.parseInt(string));
    }
}
