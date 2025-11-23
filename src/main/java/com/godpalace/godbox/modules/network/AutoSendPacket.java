package com.godpalace.godbox.modules.network;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxComboBox;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Getter
public class AutoSendPacket implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "自动发包";
    private final String description = "自动发送数据包";
    private final String typeListName = "NETWORK";

    private final BoxComboBox.BoxEnum mode = new BoxComboBox.BoxEnum(new String[]{
            "TCP", "UDP"
    }, 0);

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("发包模式", "enum", mode.toSerializableString(), "", "", ""),
            new ModuleArg("目标IP", "string", "", "", "", ""),
            new ModuleArg("目标端口", "int", 0, 0, 65535, 1),
            new ModuleArg("发送内容", "long-string", "", "", "", ""),
            new ModuleArg("发包间隔(ms)", "int", 100, 0, 2147483647, 1),
            new ModuleArg("是否循环", "boolean", true, "", "", ""),
    };

    private InetAddress targetIP;
    private int targetPort;
    private int sendInterval;
    private String sendContent;
    private boolean loop = false;

    private final Thread sendThread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                String modeStr = mode.getSelectedItem();
                try {
                    if (modeStr.equals("TCP")) {
                        // TCP
                        Socket socket = new Socket(targetIP, targetPort);
                        socket.getOutputStream().write(sendContent.getBytes());
                        socket.close();
                    } else if (modeStr.equals("UDP")) {
                        // UDP
                        DatagramSocket socket = new DatagramSocket();
                        byte[] data = sendContent.getBytes();
                        DatagramPacket packet = new DatagramPacket(data, 0, data.length, targetIP, targetPort);
                        socket.send(packet);
                        socket.close();
                    }
                    Thread.sleep(sendInterval);
                } catch (Exception ignored) {}
            }
            if (!loop) {
                enabled.set(false);
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

        try {
            targetIP = InetAddress.getByName(args[1].getValue().toString());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        targetPort = Integer.parseInt(args[2].getValue().toString());
        sendContent = args[3].getValue().toString();
        sendInterval = Integer.parseInt(args[4].getValue().toString());
        loop = Boolean.parseBoolean(args[5].getValue().toString());

        if (!sendThread.isAlive()) {
            sendThread.start();
        }
        enabled.set(true);
    }

    @Override
    public void Disable() {
        enabled.set(false);
    }

    @Override
    public void init() {}
}
