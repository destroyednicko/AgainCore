package com.nicko.core.profile.notes;

import com.google.gson.JsonObject;
import com.nicko.core.util.json.JsonDeserializer;

public class NoteJsonDeSerializer implements JsonDeserializer<Note> {
    @Override
    public Note deserialize(JsonObject object) {
        Note note = new Note();
        note.setId(object.get("id").getAsInt());
        note.setCreateAt(object.get("createAt").getAsInt());
        note.setCreateBy(object.get("createBy").getAsString());
        note.setNote(object.get("note").getAsString());
        if (object.get("UpdateBy") != null) {
            note.setUpdateBy(object.get("createBy").getAsString());
            note.setUpdateAt(object.get("createAt").getAsInt());
        }

        return note;
    }
}
