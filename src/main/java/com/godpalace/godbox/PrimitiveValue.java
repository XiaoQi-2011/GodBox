package com.godpalace.godbox;

import lombok.Getter;

@Getter
public class PrimitiveValue<T> {
    private T value;

    public static PrimitiveValue<?> getFromString(String data, String type) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("String value cannot be null or empty.");
        }

        // 根据类型转换字符串
        switch (type) {
            case "Boolean" -> {
                return new PrimitiveValue<>(Boolean.parseBoolean(data));
            }

            case "String" -> {
                return new PrimitiveValue<>(data);
            }

            case "Character" -> {
                if (data.length() > 1) {
                    throw new IllegalArgumentException("Character value cannot be more than one character.");
                }

                return new PrimitiveValue<>(data.charAt(0));
            }

            case "Double" -> {
                return new PrimitiveValue<>(Double.parseDouble(data));
            }

            case "Float" -> {
                return new PrimitiveValue<>(Float.parseFloat(data));
            }

            case "Integer" -> {
                return new PrimitiveValue<>(Integer.parseInt(data));
            }

            case "Long" -> {
                return new PrimitiveValue<>(Long.parseLong(data));
            }

            case "Short" -> {
                return new PrimitiveValue<>(Short.parseShort(data));
            }

            case "Byte" -> {
                return new PrimitiveValue<>(Byte.parseByte(data));
            }

            case "Null" -> {
                return new PrimitiveValue<>();
            }

            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }

    public PrimitiveValue(T value) {
        // 检查类型是否为基本类型
        checkType(value.getClass());

        this.value = value;
    }

    public PrimitiveValue() {
        this.value = null;
    }

    public void setValue(T value) {
        // 检查类型是否为基本类型
        checkType(value.getClass());

        this.value = value;
    }

    private void checkType(Class<?> type) {
        boolean notPrimitive = true;

        notPrimitive &= type != String.class;
        notPrimitive &= type != Boolean.class;
        notPrimitive &= type != Character.class;
        notPrimitive &= type != Double.class;
        notPrimitive &= type != Float.class;
        notPrimitive &= type != Integer.class;
        notPrimitive &= type != Long.class;
        notPrimitive &= type != Short.class;
        notPrimitive &= type != Byte.class;

        if (notPrimitive) {
            throw new IllegalArgumentException("BasicValue can only be used with primitive types.");
        }
    }

    @Override
    public String toString() {
        return value.toString() + "@" + (value == null? "Null" : value.getClass().getName());
    }
}
