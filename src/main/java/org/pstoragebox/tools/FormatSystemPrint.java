package org.pstoragebox.tools;

import java.util.Scanner;

import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

public class FormatSystemPrint {
    private static final String HEAD = "PStorageBox > ";

    @Deprecated
    public static void printMessage(String message) {
        printInfo(message);
//        System.out.println(ansi().fg(YELLOW).a(HEAD).reset().a(message));
    }

    public static void printInfo(String message) {
        System.out.println(ansi().fg(GREEN).a(HEAD).reset().a(message));
    }

    public static void printWarn(String message) {
        System.out.println(ansi().fg(YELLOW).a(HEAD).reset().a(message));
    }

    public static void printError(String message) {
        System.out.println(ansi().fg(RED).a(HEAD).reset().a(message));
    }//.eraseLine(Erase.ALL)

    public static void printRemoteMessage(String remote, String message) {
        System.out.println(ansi().fg(CYAN).a(" [ " + remote + " ] ").reset().a(message));
    }

    public static void cls() {
        System.out.println(ansi().eraseScreen());
    }

    private static void printHead() {
//        System.out.println();
        System.out.print(ansi().fg(WHITE).a(HEAD).reset());
    }

    public static String getNextLine() {
        var scan = new Scanner(System.in);
//        printHead();
        var data = scan.nextLine();
        while (data.equals("")) {
//            printHead();
            data = scan.nextLine();
        }
        return data;
    }
}
