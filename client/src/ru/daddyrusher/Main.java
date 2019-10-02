package ru.daddyrusher;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Client sending request from browser */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8080);
            OutputStream out = clientSocket.getOutputStream();
            out.write("Hello niggaz!".getBytes());
            out.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            reader.lines().forEach(System.out::println);
            reader.close();
            out.close();
            clientSocket.close();

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "");
        }
    }
}
