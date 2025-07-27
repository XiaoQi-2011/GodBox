package com.godpalace.godbox.modules;

import com.godpalace.godbox.UiSettings;
import com.godpalace.godbox.mgr.KeyBindListener;
import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.BoxShowPanel;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.AntiMostTopUtil;
import com.godpalace.godbox.util.DialogUtil;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;

import static com.godpalace.godbox.util.CharToStringUtil.charToString;

@Getter
public class AntiScreenshot implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private boolean enabled;

    @Setter
    private String keyBind = "None";

    private final String displayName = "防截屏";
    private final String description = "防止截屏、录屏等方式获取屏幕内容";
    private final String typeListName = "SCREEN";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
            new ModuleArg("窗口名称(一行一个)", "long-string", "", "", "", ""),
            new ModuleArg("关闭模块后取消防截屏", "boolean", true, "", "", ""),
            new ModuleArg("获取所有窗口名称", "show", "", "", "", ""),
    };

    private static final String exePath;
    private String[] screenNames = null;
    private final Vector<String> windows = new Vector<>();

    private class WndEnumProc implements WinUser.WNDENUMPROC{
        @Override
        public boolean callback(WinDef.HWND hWnd, Pointer data) {
            if (User32.INSTANCE.IsWindowVisible(hWnd)) {
                char[] lpString = new char[260];
                //得到句柄窗口的名字
                User32.INSTANCE.GetWindowText(hWnd, lpString, 260);
                if (lpString[0] == '\0') return true;

                windows.add(charToString(lpString));
            }

            User32.INSTANCE.EnumChildWindows(hWnd, new WndEnumProc(), data);
            return true;
        }
    }
    static {
        File exe = new File(System.getenv("TEMP"), "NotCaptureCmd.exe");
        URL exeUrl = AntiMostTopUtil.class.getResource("/NotCaptureCmd.exe");

        if (exeUrl != null && !exe.exists()) {
            try {
                InputStream in = exeUrl.openStream();
                FileOutputStream out = new FileOutputStream(exe);

                byte[] buffer = new byte[10240];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load NotCaptureCmd.exe", e);
            }
        }

        exePath = exe.getAbsolutePath();
    }

    @Override
    public void Enable() {
        enabled = true;
        BoxShowPanel showPanel = (BoxShowPanel) args[2].getComponent();
        showPanel.getButton().addActionListener(e -> {
            Pointer pointer = Pointer.createConstant(0);
            User32.INSTANCE.EnumWindows(new WndEnumProc(), pointer);
            StringBuilder result = new StringBuilder();
            for (String window : windows) {
                result.append(window).append("\n");
            }
            showPanel.getTextArea().setText(result.toString());
            DialogUtil.showModuleInfo(result.toString());
        });

        screenNames = args[0].getValue().toString().split("\n");
        for (String screenName : screenNames) {
            if (screenName.trim().isEmpty()) {
                continue;
            }
            try {
                Runtime.getRuntime().exec(exePath + " \"" + screenName.trim() + "\" h");
            } catch (Exception e) {
                throw new RuntimeException("Failed to enable AntiScreenshot", e);
            }
        }
    }

    @Override
    public void Disable() {
        enabled = false;
        if (!Boolean.parseBoolean(args[1].getValue().toString()) || screenNames == null) {
            return;
        }
        for (String screenName : screenNames) {
            if (screenName.trim().isEmpty()) {
                continue;
            }
            try {
                Runtime.getRuntime().exec(exePath + " \"" + screenName.trim() + "\" s");
            } catch (Exception e) {
                throw new RuntimeException("Failed to disable AntiScreenshot", e);
            }
        }
    }
}