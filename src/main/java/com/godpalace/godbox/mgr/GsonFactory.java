package com.godpalace.godbox.mgr;

import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.module_mgr.ModuleArgTypeAdapter;
import com.godpalace.godbox.module_mgr.SerializableTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.Serializable;

public class GsonFactory {
    @Getter
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(ModuleArg.class, new ModuleArgTypeAdapter())
                .registerTypeAdapter(Serializable.class, new SerializableTypeAdapter())
                .create();
    }
}
