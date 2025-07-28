package com.godpalace.godbox.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

public final class FileReleaseUtil {
    public static String releaseFile(String filename) {
        File file = new File(System.getenv("TEMP"), filename);
        URL Url = FileReleaseUtil.class.getResource(File.separator + filename);

        if (Url != null && !file.exists()) {
            try {
                InputStream in = Url.openStream();
                FileOutputStream out = new FileOutputStream(file);

                byte[] buffer = new byte[10240];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }

                in.close();
                out.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to release " + filename, e);
            }
        }

        return file.getAbsolutePath();
    }
}
