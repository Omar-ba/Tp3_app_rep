package clientPackage;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String host = "127.0.0.1"; // localhost
        int port = 1900;

        try (Socket socket = new Socket(host, port)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Connecté au serveur !");
            System.out.println(in.readLine()); // Message de bienvenue

            String message;
            while (true) {
                System.out.print("Vous : ");
                message = clavier.readLine();
                out.println(message);

                if (message.equalsIgnoreCase("stop")) break;

                String reponse = in.readLine();
                System.out.println(reponse);
            }

            socket.close();
            System.out.println("Déconnexion du serveur.");

        } catch (IOException e) {
            System.out.println("Erreur de connexion au serveur : " + e.getMessage());
        }
    }
}
