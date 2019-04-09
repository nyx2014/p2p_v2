package org.pstoragebox.tools;

import java.util.Scanner;

public class FormatSystemPrint {
    public static void printMessage(String message){
        System.out.println("PStorageBox > " + message);
    }

    public static void printHead(){System.out.print("PStorageBox > ");}

    public static String getNextLine(){
        Scanner scan = new Scanner(System.in);
        printHead();
        String data = scan.nextLine();
        while (data.equals("")) {
            printHead();
            data = scan.nextLine();
        }
        return data;
    }
}
