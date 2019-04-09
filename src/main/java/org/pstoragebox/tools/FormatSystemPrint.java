package org.pstoragebox.tools;

import java.util.Scanner;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class FormatSystemPrint {
    private static final String HEAD = "PStorageBox > ";

    public static void printMessage(String message) {
        System.out.println( ansi().eraseScreen().fg(YELLOW).a(HEAD).reset().a(message) );
    }

    public static void printInfo(String message) {
        System.out.println( ansi().eraseScreen().fg(GREEN).a(HEAD).reset().a(message) );
    }

    public static void printError(String message) {
        System.out.println( ansi().eraseScreen().fg(RED).a(HEAD).reset().a(message) );
    }

    static void printHead() {
        System.out.print( ansi().eraseScreen().fg(YELLOW).a(HEAD).reset() );
    }

    public static String getNextLine() {
        var scan = new Scanner(System.in);
        printHead();
        var data = scan.nextLine();
        while (data.equals("")) {
            printHead();
            data = scan.nextLine();
        }
        return data;
    }
}
