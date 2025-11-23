package com.godpalace.godbox.modules.test;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.File;

@Getter
public class Test implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled;

    @Setter
    private String keyBind = "None";

    private final String displayName = "测试";
    private final String description = "这是一个测试插件";
    private final String typeListName = "TEST";

    private final BoxComboBox.BoxEnum ArgType = new BoxComboBox.BoxEnum(new String[]{
            "Type1", "Type2", "Type3"
    }, 1);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("int", "int", 40, -2147483648, 2147483647, 1),
            new ModuleArg("string", "string", "hello", "", "", ""),
            new ModuleArg("long-string", "long-string", "hello\nworld", "", "", ""),
            new ModuleArg("boolean", "boolean", true, "", "", ""),
            new ModuleArg("float", "float", 1.0f, -2147483648.0f, 2147483647.0f, 0.1f),
            new ModuleArg("double", "double", 1.0, -2147483648.0, 2147483647.0, 0.1),
            new ModuleArg("char", "char", 'a', "", "", ""),
            new ModuleArg("byte", "byte", (byte) 1, -128, 127, 1),
            new ModuleArg("short", "short", (short) 1, -32768, 32767, 1),
            new ModuleArg("long", "long", 1L, -9223372036854775808L, 9223372036854775807L, 1L),
            new ModuleArg("enum", "enum", ArgType.toSerializableString(), "", "", ""),
            new ModuleArg("file", "file", new File("C:/Users/Administrator/Desktop/test.txt"), "", "", ""),
            new ModuleArg("color", "color", new Color(89, 142, 211).getRGB(), "", "", "")
    };


    @Override
    public void Enable() {
        enabled = true;
        ArgType.serialize(args[10].getValue().toString());
        Color color = new Color(Integer.parseInt(args[12].getValue().toString()));
        System.out.println(args[0].getName() + " " + args[0].getValue());
        System.out.println(args[1].getName() + " " + args[1].getValue());
        System.out.println(args[2].getName() + " " + args[2].getValue());
        System.out.println(args[3].getName() + " " + args[3].getValue());
        System.out.println(args[4].getName() + " " + args[4].getValue());
        System.out.println(args[5].getName() + " " + args[5].getValue());
        System.out.println(args[6].getName() + " " + args[6].getValue());
        System.out.println(args[7].getName() + " " + args[7].getValue());
        System.out.println(args[8].getName() + " " + args[8].getValue());
        System.out.println(args[9].getName() + " " + args[9].getValue());
        System.out.println(args[10].getName() + " " + ArgType.getSelectedItem());
        System.out.println(args[11].getName() + " " + args[11].getValue());
        System.out.println(args[12].getName() + " " + color);
    }

    @Override
    public void Disable() {
        enabled = false;
    }

    @Override
    public void init() {}
}
