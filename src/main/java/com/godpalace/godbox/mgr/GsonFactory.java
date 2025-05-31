package com.godpalace.godbox.mgr;

import com.godpalace.godbox.module.ClassTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

public class GsonFactory {
    @Getter
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(Class.class, new ClassTypeAdapter())
                .create();
    }
}
