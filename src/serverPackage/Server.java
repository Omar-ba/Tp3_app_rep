package serverPackage;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    // Liste des clients connectés
    private static List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        int port = 1900;
        int clientCount = 0;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur démarré sur le port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                clientCount++;

                ClientHandler clientHandler = new ClientHandler(socket, clientCount);
                clients.add(clientHandler);

                System.out.println("Client n°" + clientCount + " connecté depuis : " + socket.getInetAddress().getHostAddress());
                afficherClientsConnectes();

                // Lancer un thread pour gérer ce client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Affiche tous les clients connectés
    public static void afficherClientsConnectes() {
        System.out.println("=== Clients connectés ===");
        synchronized (clients) {
            for (ClientHandler c : clients) {
                System.out.println("Client n°" + c.getClientNumber() + " - IP : " + c.getClientIP());
            }
        }
        System.out.println("=========================");
    }

    // Retirer un client de la liste lors de la déconnexion
    public static void retirerClient(ClientHandler client) {
        clients.remove(client);
        afficherClientsConnectes();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private int clientNumber;

    public ClientHandler(Socket socket, int clientNumber) {
        this.socket = socket;
        this.clientNumber = clientNumber;
    }

    public int getClientNumber() {
        return clientNumber;
    }

    public String getClientIP() {
        return socket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("Bienvenue, vous êtes le client n°" + clientNumber);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Client " + clientNumber + " dit : " + message);
                out.println("Serveur : " + message);

                if (message.equalsIgnoreCase("bye") || message.equalsIgnoreCase("stop")) {
                    out.println("Au revoir client " + clientNumber);
                    break;
                }
            }

            socket.close();
            System.out.println("Client n°" + clientNumber + " déconnecté.");
            Server.retirerClient(this);

        } catch (IOException e) {
            System.out.println("Erreur avec le client n°" + clientNumber);
            Server.retirerClient(this);
        }
    }
}
