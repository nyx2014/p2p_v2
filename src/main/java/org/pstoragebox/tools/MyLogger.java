package org.pstoragebox.tools;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {
    private static final AtomicReference<Logger> mLogger = new AtomicReference<Logger>();
    static {
        var handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);

        mLogger.set(Logger.getLogger("LOGGER"));
        mLogger.get().setLevel(Level.ALL);
        mLogger.get().addHandler(handler);
    }

    public static Logger get(){
//        var myLogger = Logger.getLogger("LOGGER");
//        var handler = new ConsoleHandler();
//        myLogger.setLevel(Level.ALL);
//        handler.setLevel(Level.ALL);
//        myLogger.addHandler(handler);
//        return myLogger;
        return mLogger.get();
    }
}
