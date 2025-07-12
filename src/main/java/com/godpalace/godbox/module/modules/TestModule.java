package com.godpalace.godbox.module.modules;

import com.godpalace.godbox.module.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
public class TestModule implements Module {
    @Setter
    private transient ModuleSettingsPanel settingsPanel;

    @Setter
    private transient boolean entered;

    @SerializedName("enabled")
    private boolean enabled;

    private transient final String displayName = "测试";
    private transient final String description = "这是一个测试插件";
    private transient final String version = "v1.0";
    private transient final String author = "XiaoQi";
    private transient final String website = "https://www.example.com";
    private transient final String typeListName = "TEST";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("arg1", "int", 40, -2147483648, 2147483647, 1),
            new ModuleArg("arg2", "string", "hello", "", "", ""),
            new ModuleArg("arg3", "boolean", true, "", "", ""),
            new ModuleArg("arg4", "float", 1.0f, -2147483648.0f, 2147483647.0f, 0.1f),
            new ModuleArg("arg5", "double", 1.0, -2147483648.0, 2147483647.0, 0.1),
            new ModuleArg("arg6", "char", 'a', "", "", ""),
            new ModuleArg("arg7", "byte", (byte) 1, -128, 127, 1),
            new ModuleArg("arg8", "short", (short) 1, -32768, 32767, 1),
            new ModuleArg("arg9", "long", 1L, -9223372036854775808L, 9223372036854775807L, 1L)
    };


    @Override
    public void Enable() {
        enabled = true;
        System.out.println(args[0].getName() + " " + args[0].getValue());
        System.out.println(args[1].getName() + " " + args[1].getValue());
        System.out.println(args[2].getName() + " " + args[2].getValue());
        System.out.println(args[3].getName() + " " + args[3].getValue());
    }

    @Override
    public void Disable() {
        enabled = false;
    }
}
