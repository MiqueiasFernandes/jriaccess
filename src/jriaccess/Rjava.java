/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriaccess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.rosuda.JRI.RMainLoopCallbacks;
import org.rosuda.JRI.Rengine;

/**
 *
 * @author mfernandes
 */
class TextConsole implements RMainLoopCallbacks {

    @Override
    public void rWriteConsole(Rengine re, String text, int oType) {
        send(ENUM_CONSOLE.CONSOLE_WRITE, text);
    }

    @Override
    public void rBusy(Rengine re, int which) {
        send(ENUM_CONSOLE.CONSOLE_BUSY, "rBusy(" + which + ")");
    }

    @Override
    public String rReadConsole(Rengine re, String prompt, int addToHistory) {
        send(ENUM_CONSOLE.CONSOLE_PROMPT, prompt);
        return getLine(prompt);
    }

    @Override
    public void rShowMessage(Rengine re, String message) {
        send(ENUM_CONSOLE.CONSOLE_SHOW, message);
    }

    @Override
    public String rChooseFile(Rengine re, int newFile) {
        send(ENUM_CONSOLE.CONSOLE_FILE, (newFile == 0) ? "Select a file" : "Select a new file");
        return getLine("FILE:");
    }

    @Override
    public void rFlushConsole(Rengine re) {
        send(ENUM_CONSOLE.CONSOLE_FLUSH, "");
    }

    @Override
    public void rLoadHistory(Rengine re, String filename) {
        send(ENUM_CONSOLE.CONSOLE_LOAD, filename);
    }

    @Override
    public void rSaveHistory(Rengine re, String filename) {
        send(ENUM_CONSOLE.CONSOLE_SAVE, filename);
    }

    public String getLine(String prompt) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s = br.readLine();
            send(ENUM_CONSOLE.CONSOLE_ECHO, prompt + " " + s);
            return (s == null || s.length() == 0) ? s : s + "\n";
        } catch (IOException e) {
            send(ENUM_CONSOLE.CONSOLE_ERROR, "jriReadConsole exception: " + e.getMessage());
        }
        return null;
    }

    public static void send(ENUM_CONSOLE tipo, String text) {

        String opcao = "undefined";
        switch (tipo) {
            case CONSOLE_BUSY:
                opcao = "CONSOLE_BUSY";
                break;
            case CONSOLE_ECHO:
                opcao = "CONSOLE_ECHO";
                break;
            case CONSOLE_ERROR:
                opcao = "CONSOLE_ERROR";
                break;
            case CONSOLE_FILE:
                opcao = "CONSOLE_FILE";
                break;
            case CONSOLE_FLUSH:
                opcao = "CONSOLE_FLUSH";
                break;
            case CONSOLE_LOAD:
                opcao = "CONSOLE_LOAD";
                break;
            case CONSOLE_PROMPT:
                opcao = "CONSOLE_PROMPT";
                break;
            case CONSOLE_SAVE:
                opcao = "CONSOLE_SAVE";
                break;
            case CONSOLE_SHOW:
                opcao = "CONSOLE_SHOW";
                break;
            case CONSOLE_WRITE:
                opcao = "CONSOLE_WRITE";
                break;
        }

        int i = 0;
        boolean[] lines = new boolean[text.length()];
        for (char letra : text.toCharArray()) {
            if (letra == '\n') {
                lines[i] = true;
            } else {
                lines[i++] = false;
            }
        }

        StringBuilder op = new StringBuilder(opcao);

        i = 0;
        for (boolean newline : lines) {
            if (newline) {
                op.append("," + i++);
            }
        }

        System.out.println("[" + op.toString() + "]" + text.replace(System.lineSeparator(), ""));
    }

}

public class Rjava {

    public static void init() {
        // just making sure we have the right version of everything
        if (!Rengine.versionCheck()) {
            TextConsole.send(ENUM_CONSOLE.CONSOLE_ERROR, "** Version mismatch - Java files don't match library version.");
            System.exit(1);
        }
        TextConsole.send(ENUM_CONSOLE.CONSOLE_BUSY, "Creating Rengine (with arguments)");
        // 1) we pass the arguments from the command line
        // 2) we won't use the main loop at first, we'll start it later
        //    (that's the "false" as second argument)
        // 3) the callbacks are implemented by the TextConsole class above
        Rengine re = new Rengine(new String[]{"--vanilla"}, false, new TextConsole());
        TextConsole.send(ENUM_CONSOLE.CONSOLE_BUSY, "Rengine created, waiting for R");
        // the engine creates R is a new thread, so we should wait until it's ready
        if (!re.waitForR()) {
            TextConsole.send(ENUM_CONSOLE.CONSOLE_ERROR, "Cannot load R");
            return;
        }

        if (true) {
            TextConsole.send(ENUM_CONSOLE.CONSOLE_BUSY, "Now the console is yours ... have fun");
            re.startMainLoop();
        } else {
            re.end();
            TextConsole.send(ENUM_CONSOLE.CONSOLE_ERROR, "end...");
        }
    }
}
