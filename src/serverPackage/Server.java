package serverPackage;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;
import ope.Operation;

public class Server {
    private static final int PORT = 1900;

    // Compteur global d'opérations
    private static AtomicInteger compteurGlobal = new AtomicInteger(0);
    // Compteur de clients connectés
    private static AtomicInteger compteurClients = new AtomicInteger(0);

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println(" Serveur en attente sur le port " + PORT + " ...");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Créer les flux correctement
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                out.flush();
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                // Incrémentation du numéro client
                int idClient = compteurClients.incrementAndGet();
                System.out.println(" Nouveau client #" + idClient + " connecté");
                System.out.println("→ Nombre total de clients connectés : " + compteurClients.get());

                // Envoi du numéro du client
                out.writeInt(idClient);
                out.flush();

                // Démarrage du thread pour ce client
                new ClientProcess(clientSocket, idClient, in, out).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientProcess extends Thread {
        private Socket socket;
        private int clientId;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientProcess(Socket socket, int clientId, ObjectInputStream in, ObjectOutputStream out) {
            this.socket = socket;
            this.clientId = clientId;
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Operation op = (Operation) in.readObject();
                    op.calculer();

                    out.writeObject(op);
                    out.flush();

                    int cpt = compteurGlobal.incrementAndGet();
                    System.out.println(" Client #" + clientId + " a effectué une opération (" + cpt + " total)");
                }
            } catch (Exception e) {
                System.out.println(" Client #" + clientId + " déconnecté");
                int nb = compteurClients.decrementAndGet();
                System.out.println("→ Clients restants : " + nb);
            }
        }
    }
}
