package com.nicko.core.chat.filter.impl;

import com.nicko.core.chat.filter.ChatFilter;

import java.util.Arrays;
import java.util.List;

public class ContainsFilter extends ChatFilter {

    // TODO: adicionar mais coisas...
    private static final List<String> filteredWords = Arrays.asList("puta", "noob", "macaco");

    public ContainsFilter(String command) {
        super(command);
    }

    @Override
    public boolean isFiltered(String message, String[] words) {
        for (String word : words) {
            for (String filteredWord : filteredWords) {
                if (word.contains(filteredWord)) {
                    return true;
                }
            }
        }

        return false;
    }

}
