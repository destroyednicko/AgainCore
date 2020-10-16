package com.nicko.core.util.duration;

import com.nicko.shinigami.command.adapter.CommandTypeAdapter;

public class DurationTypeAdapter implements CommandTypeAdapter {

    @Override
    public <T> T convert(String string, Class<T> type) {
        return type.cast(Duration.fromString(string));
    }

}

