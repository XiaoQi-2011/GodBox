package com.godpalace.godbox.module;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
* 使用JSON:
*
* name: 参数名称
* type: 值类型(必须可序列化)，如: java.lang.String
* value: 初始值，如: "Hello, World!"
*
* 可选字段:
*   Number:
*   min: 最小值
*   max: 最大值
*   step: 步长
*
* example:
* {
*     "name": "text",
*     "type": "java.lang.String",
*     "value": "Hello, World!"
* }
*
* {
*     "name": "age",
*     "type": "java.lang.Integer",
*     "value": 18
*     "min": 0,
*     "max": 100,
*     "step": 1
* }
*
* 引用参数: $参数名称
*/

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

    private String name;
    private String type;
    private Serializable value;

    private Serializable min;
    private Serializable max;
    private Serializable step;
}
