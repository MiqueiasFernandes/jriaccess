/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jriaccess;

/**
 *
 * @author mfernandes
 */
public class JRIaccess {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        System.out.println("{\"servers\":["
                + "\"CONSOLE_WRITE\","
                + "\"CONSOLE_BUSY\","
                + "\"CONSOLE_PROMPT\","
                + "\"CONSOLE_ECHO\","
                + "\"CONSOLE_ERROR\","
                + "\"CONSOLE_SHOW\","
                + "\"CONSOLE_FILE\","
                + "\"CONSOLE_FLUSH\","
                + "\"CONSOLE_LOAD\","
                + "\"CONSOLE_SAVE\"]}");

        String path = System.getenv("R_HOME");

        if (path == null || path.isEmpty() || path.length() < 2) {

            String s = "R_HOME=/usr/lib64/R\n"
                    + "export R_HOME\n"
                    + "R_SHARE_DIR=/usr/share/R\n"
                    + "export R_SHARE_DIR\n"
                    + "R_INCLUDE_DIR=/usr/include/R\n"
                    + "export R_INCLUDE_DIR\n"
                    + "R_DOC_DIR=/usr/share/doc/R\n"
                    + "export R_DOC_DIR\n"
                    + "LD_LIBRARY_PATH=/home/mfernandes/R/x86_64-redhat-linux-gnu-library/3.3/rJava/jri\n"
                    + "export LD_LIBRARY_PATH";
            System.out.println("Não é possivel iniciar, configure as variaveis de ambiente como:\n" + s);
            System.exit(- 1 * ErrosException.VARIAVEL_NAO_CONFIGURADA.ordinal());
        }
        Rjava.init();
    }

}
