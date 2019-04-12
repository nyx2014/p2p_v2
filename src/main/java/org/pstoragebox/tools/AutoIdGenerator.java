package org.pstoragebox.tools;

import java.io.*;
import java.util.UUID;
import java.util.logging.Level;

public class AutoIdGenerator {
    public static String getId() throws IOException {
        if (userID == null)
            throw new IOException("ID_GET_ERROR");
        return userID;
    }

    private static String initID() {
//        String filepath = "./etc/IDconfig.txt";
//
//        String path = System.getProperty("user.dir") + "\\init.properties";
//        FormatSystemPrint.printError(path);
//        AnsiConsole.systemUninstall();
//        System.exit(-1);

        File file = new File(ConfigPathTool.get("IDconfig"));
        if (!file.exists()) {
            String id = UUID.randomUUID().toString().replace("-", "");
            try {
                if (!file.createNewFile()) return null;
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(id);
                bufferedWriter.close();
                fileWriter.close();
                return id;
            } catch (IOException e) {
                MyLogger.get().log(Level.FINER, e.toString());
            }

        } else {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                return bufferedReader.readLine();
            } catch (Exception e) {
                MyLogger.get().log(Level.FINER, e.toString());
            }
        }
        return null;
    }

    private static final String userID = initID();
}
