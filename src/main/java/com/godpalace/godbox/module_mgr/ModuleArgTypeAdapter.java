package com.godpalace.godbox.module_mgr;

import com.godpalace.godbox.util.ArgUtils;
import com.google.gson.*;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ModuleArgTypeAdapter implements JsonSerializer<ModuleArg>, JsonDeserializer<ModuleArg> {
    @Override
    public JsonElement serialize(ModuleArg moduleArg, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("name", moduleArg.getName());
        jsonObject.addProperty("type", moduleArg.getType());
        jsonObject.addProperty("value", toValue(moduleArg.getValue()));

        // 可选属性
        // 如果是数字类型，则添加min, max和step属性
        if (ArgUtils.isNumber(moduleArg.getType())) {
            jsonObject.addProperty("min", String.valueOf(moduleArg.getMin()));
            jsonObject.addProperty("max", String.valueOf(moduleArg.getMax()));
            jsonObject.addProperty("step", String.valueOf(moduleArg.getStep()));
        }

        return jsonObject;
    }

    @Override
    public ModuleArg deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!jsonElement.isJsonObject()) {
            throw new JsonParseException("ModuleArg must be a JSON object: " + jsonElement);
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        ModuleArg moduleArg = new ModuleArg();

        moduleArg.setName(jsonObject.get("name").getAsString());
        moduleArg.setType(jsonObject.get("type").getAsString());
        moduleArg.setValue(jsonObject.get("value").getAsString());

        // 可选属性
        // 如果是数字类型，则添加min, max和step属性
        if (ArgUtils.isNumber(moduleArg.getType())) {
            JsonElement min = jsonObject.get("min");
            JsonElement max = jsonObject.get("max");
            JsonElement step = jsonObject.get("step");

            switch (moduleArg.getType()) {
                case "byte" -> {
                    if (min != null) moduleArg.setMin(min.getAsByte()); else moduleArg.setMin(Byte.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsByte()); else moduleArg.setMax(Byte.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsByte()); else moduleArg.setStep(1);
                }

                case "short" -> {
                    if (min != null) moduleArg.setMin(min.getAsShort()); else moduleArg.setMin(Short.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsShort()); else moduleArg.setMax(Short.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsShort()); else moduleArg.setStep(1);
                }

                case "int" -> {
                    if (min != null) moduleArg.setMin(min.getAsInt()); else moduleArg.setMin(Integer.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsInt()); else moduleArg.setMax(Integer.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsInt()); else moduleArg.setStep(1);
                }

                case "long" -> {
                    if (min != null) moduleArg.setMin(min.getAsLong()); else moduleArg.setMin(Long.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsLong()); else moduleArg.setMax(Long.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsLong()); else moduleArg.setStep(1);
                }

                case "float" -> {
                    if (min != null) moduleArg.setMin(min.getAsFloat()); else moduleArg.setMin(Float.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsFloat()); else moduleArg.setMax(Float.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsFloat()); else moduleArg.setStep(1.0f);
                }

                case "double" -> {
                    if (min != null) moduleArg.setMin(min.getAsDouble()); else moduleArg.setMin(Double.MIN_VALUE);
                    if (max != null) moduleArg.setMax(max.getAsDouble()); else moduleArg.setMax(Double.MAX_VALUE);
                    if (step != null) moduleArg.setStep(step.getAsDouble()); else moduleArg.setStep(1.0);
                }

                default -> throw new JsonParseException("Unsupported number type: " + moduleArg.getType());
            }
        }

        return moduleArg;
    }

    private String toValue(Serializable value) {
        if (value == null) {
            return "null";
        }

        return value + "";
    }
}
