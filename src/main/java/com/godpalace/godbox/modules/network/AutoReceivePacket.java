package com.godpalace.godbox.modules.network;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxCheckBox;
import com.godpalace.godbox.ui.box_ui.BoxComboBox;
import com.godpalace.godbox.ui.box_ui.BoxShowPanel;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Getter
public class AutoReceivePacket implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "自动收包";
    private final String description = "自动接收数据包";
    private final String typeListName = "NETWORK";

    private final BoxComboBox.BoxEnum mode = new BoxComboBox.BoxEnum(new String[]{
            "TCP", "UDP"
    }, 0);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("收包模式", "enum", mode.toSerializableString(), "", "", ""),
            new ModuleArg("接收端口", "int", 0, 0, 65535, 1),
            new ModuleArg("缓冲区大小", "int", 2048, 1024, 1048576, 1024),
            new ModuleArg("自动获取数据", "boolean", true, "", "", ""),
            new ModuleArg("数据包内容", "show", "", "", "", ""),
    };

    private int port;
    private int bufferSize;

    private final Thread receiveThread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                String modeStr = mode.getSelectedItem();
                try {
                    if (modeStr.equals("TCP")) {
                        // TCP
                        ServerSocket serverSocket = new ServerSocket(port);
                        serverSocket.setSoTimeout(1000);

                        Socket socket = serverSocket.accept();
                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[bufferSize];
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        int len = 0;
                        while ((len = inputStream.read(buffer))!= -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        String data = outputStream.toString();

                        args[4].setValue(data);
                        socket.close();
                        serverSocket.close();
                        inputStream.close();
                        outputStream.close();
                    } else if (modeStr.equals("UDP")) {
                        // UDP
                        DatagramSocket socket = new DatagramSocket(port);
                        socket.setSoTimeout(1000);

                        byte[] buffer = new byte[bufferSize];
                        DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
                        socket.receive(packet);
                        String data = new String(packet.getData(), 0, packet.getLength());

                        args[4].setValue(data);
                        socket.close();
                    }
                } catch (Exception ignored) {}
            }
        }
    });

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        mode.serialize(args[0].getValue().toString());
        port = Integer.parseInt(args[1].getValue().toString());
        bufferSize = Integer.parseInt(args[2].getValue().toString());

        if (!receiveThread.isAlive()) {
            receiveThread.start();
        }
        enabled.set(true);
    }

    @Override
    public void Disable() {
        enabled.set(false);
    }

    @Override
    public void init() {
        BoxShowPanel showPanel = (BoxShowPanel) args[4].getComponent();
        showPanel.setAutoShow(true, 100);
        showPanel.setOnClickEvent(() -> {
            if (args[4].getValue() == null || args[4].getValue().toString().isEmpty()) {
                args[4].setValue("请先开启模块");
            }
        });

        BoxCheckBox checkBox = (BoxCheckBox) args[3].getComponent();
        checkBox.addActionListener(e -> {
            showPanel.setAutoShow(checkBox.isSelected(), 100);
        });
    }
}
