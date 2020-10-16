package com.nicko.core.profile.notes;

import com.google.gson.JsonObject;
import com.nicko.core.util.json.JsonSerializer;

public class NoteJsonSerializer implements JsonSerializer<Note> {
    @Override
    public JsonObject serialize(Note note) {
        JsonObject object = new JsonObject();
        object.addProperty("id", note.getId());
        object.addProperty("createAt", note.getCreateAt());
        object.addProperty("createBy", note.getCreateBy());
        object.addProperty("note", note.getNote());
        if (note.getCreateBy() != null) {
            object.addProperty("UpdateBy", note.getUpdateBy());
            object.addProperty("UpdateAt", note.getUpdateAt());
        }
        return object;
    }
}
