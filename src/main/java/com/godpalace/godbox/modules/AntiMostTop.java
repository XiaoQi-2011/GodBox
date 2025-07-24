package com.godpalace.godbox.modules;

import com.godpalace.godbox.module_mgr.Module;
import com.godpalace.godbox.module_mgr.ModuleArg;
import com.godpalace.godbox.ui.ModuleSettingsPanel;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import lombok.Getter;
import lombok.Setter;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.godpalace.godbox.util.CharToStringUtil.charToString;

@Getter
public class AntiMostTop implements Module {
    @Setter
    private ModuleSettingsPanel settingsPanel;

    @Setter
    private boolean entered;
    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Setter
    private String keyBind = "None";

    private final String displayName = "反置顶";
    private final String description = "将强制置顶的窗口取消置顶";
    private final String typeListName = "SCREEN";

    @Setter
    private ModuleArg[] args = new ModuleArg[]{
    };

    private final Vector<WinDef.HWND> hwnds = new Vector<>();
    private class WndEnumProc implements WinUser.WNDENUMPROC{

        @Override
        public boolean callback(WinDef.HWND hWnd, Pointer data) {
            if (User32.INSTANCE.IsWindowVisible(hWnd)) {
                char[] lpString = new char[260];
                //得到句柄窗口的名字
                User32.INSTANCE.GetWindowText(hWnd, lpString, 260);
                if (lpString[0] == '\0') return true;

                hwnds.add(hWnd);
            }
            User32.INSTANCE.EnumChildWindows(hWnd, new WndEnumProc(), data);
            return true;
        }
    }
    private boolean isWndTopMost(WinDef.HWND hWnd) {
        return (User32.INSTANCE.GetWindowLong(hWnd, WinUser.GWL_EXSTYLE) & User32.WS_EX_TOPMOST) != 0;
    }
    private void cancelTopMost(WinDef.HWND hWnd) {
        User32.RECT rect = new User32.RECT();
        User32.INSTANCE.GetWindowRect(hWnd, rect);
        User32.INSTANCE.SetWindowPos(hWnd, new WinDef.HWND(Pointer.createConstant(-2)), rect.left, rect.top, Math.abs(rect.right - rect.left), Math.abs(rect.bottom - rect.top), WinUser.SWP_SHOWWINDOW);
    }
    private final Thread thread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                Pointer pointer = Pointer.createConstant(0);
                User32.INSTANCE.EnumWindows(new WndEnumProc(),pointer);
                for (WinDef.HWND hWnd : hwnds) {
                    if (isWndTopMost(hWnd)) {
                        char[] lpString = new char[260];
                        User32.INSTANCE.GetWindowText(hWnd, lpString, 260);
                        System.out.println("取消置顶窗口：" + charToString(lpString));

                        cancelTopMost(hWnd);
                    }
                }
            }
        }
    });


    @Override
    public boolean isEnabled() {
        return enabled.get();
    }

    @Override
    public void Enable() {
        enabled.set(true);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    @Override
    public void Disable() {
        enabled.set(false);
    }
}
