package com.godpalace.godbox.module;

import com.godpalace.godbox.mgr.GsonFactory;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

@SuppressWarnings("unused")
@Getter
public class Module {
    public static Module fromJsonFile(File json) {
        try (FileReader reader = new FileReader(json)) {
            return GsonFactory.getGson().fromJson(reader, Module.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Setter
    private transient String path;

    @SerializedName("display_name")
    private String displayName;
    private String description;
    private String version;
    private String author;
    private String website;

    @Setter
    private transient String exePath;

    @SerializedName("type")
    private String typeListName;

    @SerializedName("enabled")
    private boolean isEnabled;

    // 判断鼠标是否进入
    @Setter
    private transient boolean isEntered;

    // 模块运行时参数
    private ModuleArg[] args;

    // 模块设置面板
    @Setter
    private transient ModuleSettingsPanel settingsPanel;

    private Module() {
    }

    public void Enable() {
        isEnabled = true;
    }

    public void Disable() {
        isEnabled = false;
    }

    public void save() {
        File file = new File(path);

        try (FileWriter writer = new FileWriter(file)) {
            GsonFactory.getGson().toJson(this, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }
}
