package com.godpalace.godbox.ui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.text.JTextComponent;

@Getter
@Setter
public class BoxTextField extends JTextComponent {
    private String text;

    public BoxTextField(String text) {
        super();
        this.text = text;
    }
}
