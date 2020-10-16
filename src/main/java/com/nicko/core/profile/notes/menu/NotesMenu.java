package com.nicko.core.profile.notes.menu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.nicko.core.profile.Profile;
import com.nicko.core.profile.notes.Note;
import com.nicko.core.util.TimeUtil;
import com.nicko.core.util.item.ItemBuilder;
import com.nicko.core.util.menu.Button;
import com.nicko.core.util.menu.pagination.PaginatedMenu;

import java.util.Date;
import java.util.Map;

@AllArgsConstructor
public class NotesMenu extends PaginatedMenu {

    private Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return "Notas de " + profile.getName();
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = Maps.newHashMap();
        for (Note note : profile.getNotes()) {
            buttons.put(buttons.size(), new NoteButton(note));
        }
        return buttons;
    }

    @AllArgsConstructor
    private class NoteButton extends Button {

        private Note note;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&e • ID&7:&f " + note.getId())
                .lore("&e • Criado por&7:&f " + note.getCreateBy())
                .lore("&e • Nota&7:&f " + note.getNote())
                .lore("&e • Criado em&7:&f " + TimeUtil.dateToString(new Date(note.getCreateAt()), "&7"))
                .build();
        }
    }
}
