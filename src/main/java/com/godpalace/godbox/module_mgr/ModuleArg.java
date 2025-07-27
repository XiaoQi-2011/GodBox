package com.godpalace.godbox.module_mgr;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.io.Serializable;


@Getter
@Setter
public class ModuleArg {
    public ModuleArg() {}

    public ModuleArg(String name, String type, Serializable value, Serializable min, Serializable max, Serializable step) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.min = min;
        this.max = max;
        this.step = step;
    }
    private transient JComponent component;

    private String name;
    private String type;
    private Serializable value;

    private Serializable min;
    private Serializable max;
    private Serializable step;
}
