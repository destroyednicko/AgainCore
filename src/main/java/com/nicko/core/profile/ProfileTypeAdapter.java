package com.nicko.core.profile;

import com.nicko.shinigami.command.adapter.CommandTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProfileTypeAdapter implements CommandTypeAdapter {

    public <T> T convert(String string, Class<T> type) {
        return type.cast(Profile.getByUsername(string));
    }

    @Override
    public <T> List<String> tabComplete(String string, Class<T> type) {
        List<String> completed = new ArrayList<>();

        for (Profile profile : Profile.getProfiles().values()) {
            if (profile.getName() == null) continue;

            if (profile.getName().toLowerCase().startsWith(string.toLowerCase()) &&
                !profile.getStaffOptions().isStaffModeEnabled()
                && !profile.getStaffOptions().isVanish()) {
                completed.add(profile.getName());
            }
        }

        return completed;
    }

}
