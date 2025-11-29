package com.godpalace.godbox.modules.screen;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.box_ui.BoxShowPanel;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.godpalace.godbox.util.FileReleaseUtil;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;

import static com.godpalace.godbox.util.StringUtil.charToString;

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

    private static String exePath;
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

    @Override
    public void Enable() {
        enabled = true;
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

    @Override
    public void init() {
        exePath = FileReleaseUtil.releaseFile("NotCaptureCmd.exe");

        BoxShowPanel showPanel = (BoxShowPanel) args[2].getComponent();
        showPanel.getTextArea().setText("");
        showPanel.setShowType(BoxShowPanel.ShowType.DIALOG);
        showPanel.setOnClickEvent(() -> {
            windows.clear();
            Pointer pointer = Pointer.createConstant(0);
            User32.INSTANCE.EnumWindows(new WndEnumProc(), pointer);
            StringBuilder result = new StringBuilder();
            for (String window : windows) {
                result.append(window).append("\n");
            }
            String resultStr = result.substring(0, result.length() - 1);
            args[2].setValue(resultStr);
        });
    }
}