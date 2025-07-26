package com.godpalace.godbox.util;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.godpalace.godbox.util.CharToStringUtil.charToString;

public class AntiMostTopUtil {
    private final AtomicBoolean enabled = new AtomicBoolean(false);
    private final Vector<WinDef.HWND> hwnds = new Vector<>();

    static {
        File dll = new File(System.getenv("TEMP"), "AntiTopMost.dll");
        URL dllUrl = AntiMostTopUtil.class.getResource("/AntiTopMost.dll");

        if (dllUrl != null && !dll.exists()) {
            try {
                InputStream in = dllUrl.openStream();
                FileOutputStream out = new FileOutputStream(dll);

                byte[] buffer = new byte[10240];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load AntiTopMost.dll", e);
            }
        }

        System.load(dll.getAbsolutePath());
    }

    private interface TopMostFunc extends StdCallLibrary {
        TopMostFunc INSTANCE =  Native.load("AntiTopMost", TopMostFunc.class);

        boolean isWndTopMost(WinDef.HWND hWnd);
        void showWndTopMost(WinDef.HWND hWnd);
        void cancelTopMost(WinDef.HWND hWnd);
    }
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

    private final Thread thread = new Thread(() -> {
        while (true) {
            if (enabled.get()) {
                Pointer pointer = Pointer.createConstant(0);
                User32.INSTANCE.EnumWindows(new WndEnumProc(),pointer);
                for (WinDef.HWND hWnd : hwnds) {
                    if (TopMostFunc.INSTANCE.isWndTopMost(hWnd)) {
                        char[] lpString = new char[260];
                        User32.INSTANCE.GetWindowText(hWnd, lpString, 260);
                        System.out.println("取消置顶窗口：" + charToString(lpString));

                        TopMostFunc.INSTANCE.cancelTopMost(hWnd);
                    }
                }
            }
        }
    });

    public void start() {
        enabled.set(true);
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    public void stop() {
        enabled.set(false);
    }
}
