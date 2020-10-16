package com.nicko.core.profile.notes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
@NoArgsConstructor
public class Note {

    public static NoteJsonSerializer SERIALIZER = new NoteJsonSerializer();
    public static NoteJsonDeSerializer DESERIALIZER = new NoteJsonDeSerializer();

    private int id;
    private long createAt;
    private String createBy;
    private String updateBy;
    private long updateAt;
    private String note;

    public Note(int id, String note, Player staff) {
        this.id = id;
        this.note = note;
        this.createAt = System.currentTimeMillis();
        this.createBy = staff.getName();
    }

}
