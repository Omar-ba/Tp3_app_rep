package serverPackage;
import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

import ope.Operation;

public class Server {
    private static final int PORT = 1900;
    private static AtomicInteger compteurGlobal = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" Serveur en attente sur le port " + PORT + " ...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(" Nouveau client connecté : " + clientSocket.getInetAddress());
                new ClientProcess(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interne pour gérer chaque client
    static class ClientProcess extends Thread {
        private Socket socket;

        public ClientProcess(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
                while (true) {
                    Operation op = (Operation) in.readObject();
                    op.calculer();
                    out.writeObject(op);

                    int cpt = compteurGlobal.incrementAndGet();
                    System.out.println(" Opération traitée (" + cpt + " total)");
                }
            } catch (Exception e) {
                System.out.println(" Client déconnecté : " + socket.getInetAddress());
            }
        }
    }
}
