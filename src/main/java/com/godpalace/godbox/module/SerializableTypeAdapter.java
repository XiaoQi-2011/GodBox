package com.godpalace.godbox.module;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.Serializable;

public class SerializableTypeAdapter extends TypeAdapter<Serializable> {
    @Override
    public void write(JsonWriter jsonWriter, Serializable serializable) throws IOException {
        if (serializable instanceof Integer) {
            jsonWriter.value((int) serializable);
            return;
        }

        if (serializable instanceof String) {
            jsonWriter.value((String) serializable);
            return;
        }

        if (serializable instanceof Boolean) {
            jsonWriter.value((boolean) serializable);
            return;
        }

        if (serializable instanceof Double) {
            jsonWriter.value((double) serializable);
            return;
        }

        if (serializable instanceof Float) {
            jsonWriter.value((float) serializable);
            return;
        }

        if (serializable instanceof Long) {
            jsonWriter.value((long) serializable);
            return;
        }

        if (serializable instanceof Short) {
            jsonWriter.value((short) serializable);
        }
    }

    @Override
    public Serializable read(JsonReader jsonReader) throws IOException {
    }
}
