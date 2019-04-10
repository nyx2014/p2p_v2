package org.pstoragebox.tools;

import java.io.File;

public class ConfigPathTool {
    public static String get(String filename) {
        return System.getProperty("user.dir") + File.separator + filename;
    }
}
