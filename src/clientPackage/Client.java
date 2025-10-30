package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import ope.Operation;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1900;

        try (Socket socket = new Socket(host, port)) {

            // Créer les flux correctement
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Lire le numéro du client envoyé par le serveur
            int clientId = in.readInt();
            System.out.println(" Vous êtes connecté en tant que client n°" + clientId);

            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Entrez le premier nombre : ");
                double op1 = sc.nextDouble();

                System.out.print("Entrez l’opérateur (+, -, *, /) : ");
                char operateur = sc.next().charAt(0);

                System.out.print("Entrez le deuxième nombre : ");
                double op2 = sc.nextDouble();

                Operation operation = new Operation(op1, op2, operateur);
                out.writeObject(operation);
                out.flush();

                Operation resultat = (Operation) in.readObject();
                System.out.println("Résultat : " + resultat.getResultat());
            }

        } catch (Exception e) {
            System.out.println("Erreur client : " + e.getMessage());
        }
    }
}
