package org.pstoragebox.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {

    public static Logger getMyLogger(){
        Logger myLogger = Logger.getLogger("LOGGER");
        myLogger.setLevel(Level.ALL);
        return myLogger;
    }
}
