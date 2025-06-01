package com.godpalace.godbox.module;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

public class SerializableTypeAdapter extends TypeAdapter<Serializable> {
    @Override
    public void write(JsonWriter jsonWriter, Serializable serializable) throws IOException {
        if (serializable == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(serializable + "");
    }

    @Override
    public Serializable read(JsonReader jsonReader) throws IOException {
        return jsonReader.nextString();
    }
}
