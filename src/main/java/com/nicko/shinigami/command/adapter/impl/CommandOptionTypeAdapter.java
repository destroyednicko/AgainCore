package com.nicko.shinigami.command.adapter.impl;

import com.nicko.shinigami.command.CommandOption;
import com.nicko.shinigami.command.adapter.CommandTypeAdapter;

public class CommandOptionTypeAdapter implements CommandTypeAdapter {
    @Override
    public <T> T convert(final String string, final Class<T> type) {
        if (string.startsWith("-")) {
            return type.cast(new CommandOption(string.substring(1)));
        }
        return null;
    }
}
