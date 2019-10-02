package ru.daddyrusher;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Server */
public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static {
        try {
            Handler handler = new FileHandler("log.log");
            handler.setLevel(Level.ALL);
            LOGGER.addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            LOGGER.info("Server starts...");
            ServerSocket serverSocket = new ServerSocket(8080);
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            for (int i=0; i<5; i++) {
                LOGGER.info("Thread #" + i + " started");
                executorService.submit(new Thread(() -> {
                    try {
                        while (true) {
                            //request
                            Socket clientSocket = serverSocket.accept();
                            InputStreamReader reader = new InputStreamReader(clientSocket.getInputStream());
                            Thread.sleep(10000);
                            StringBuilder sb = new StringBuilder();
                            while (reader.ready()) sb.append((char) reader.read());
                            LOGGER.info("Read request");
                            System.out.println(sb);

                            //response
                            OutputStreamWriter writer = new OutputStreamWriter(clientSocket.getOutputStream());
                            String response = "HTTP/1.1 200 OK\n" +
                                    "Cache-Control: no-cache\n" +
                                    "Connection: close:\n" +
                                    "Content-Type: application/json\n\n" +
                                    "{\"ok\": \"" + (sb.toString().length() > 30 ? "too long" : sb.toString()) + "\"}";

                            writer.write(response);
                            writer.flush();

                            LOGGER.info("Response sent");
                            reader.close();
                            writer.close();
                            clientSocket.close();
                        }
                    } catch (IOException | InterruptedException e) {
                        LOGGER.warning(e.getMessage());
                    }
                }));
            }

            Thread.sleep(10000000);
            serverSocket.close();
            LOGGER.info("Server is closed");
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
    }
}
