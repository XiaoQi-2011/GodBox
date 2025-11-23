package com.godpalace.godbox.ui.box_ui;

import com.godpalace.godbox.UiSettings;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BoxComboBox extends JComboBox<String> {

    public BoxComboBox(BoxEnum boxEnum) {
        super(boxEnum.items);
        init();
    }

    private void init() {
        setFont(UiSettings.font);
        setForeground(Color.WHITE);
        setLightWeightPopupEnabled(false);
        setBorder(new LineBorder(UiSettings.themeColor));
        setOpaque(false);
    }

    public static class BoxEnum {
        @Setter
        @Getter
        private int selectedIndex;
        public String[] items;

        public BoxEnum(String[] items) {
            this.items = items;
            selectedIndex = 0;
        }
        public BoxEnum(String[] items, int selectedIndex) {
            this.items = items;
            this.selectedIndex = selectedIndex;
        }

        public BoxEnum(String serializableString) {
            serialize(serializableString);
        }

        public void serialize(String serializableString) {
            String[] objects = serializableString.split("\n");
            selectedIndex = Integer.parseInt(objects[0]);
            items = new String[objects.length - 1];
            System.arraycopy(objects, 1, items, 0, objects.length - 1);
        }

        public String toSerializableString() {
            StringBuilder sb = new StringBuilder();
            sb.append(selectedIndex).append("\n");
            for (String item : items) {
                sb.append(item).append("\n");
            }
            return sb.toString();
        }

        public String getSelectedItem() {
            return items[selectedIndex];
        }

        public void setSelectedItem(String selectedItem) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].equals(selectedItem)) {
                    selectedIndex = i;
                    break;
                }
            }
        }
    }
}
