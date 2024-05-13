package com.ktu;

import java.util.logging.*;

public class Main {
    public static void main(String[] args) {
        Logger jadeLogger = java.util.logging.Logger.getLogger("jade");
        jadeLogger.setLevel(Level.SEVERE);

        String[] param = new String[2];
        param[0] = "-gui";
        param[1] = "L:com.ktu.Launcher()";

        jade.Boot.main(param);
    }
}