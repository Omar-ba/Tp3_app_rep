package clientPackage;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import ope.Operation;

public class Client {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 1900;

        try (Socket socket = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner sc = new Scanner(System.in)) {

            System.out.println(" Connecté au serveur " + host + ":" + port);

            while (true) {
                System.out.print("Entrez le premier nombre : ");
                double op1 = sc.nextDouble();

                System.out.print("Entrez l’opérateur (+, -, *, /) : ");
                char operateur = sc.next().charAt(0);

                System.out.print("Entrez le deuxième nombre : ");
                double op2 = sc.nextDouble();

                Operation operation = new Operation(op1, op2, operateur);
                out.writeObject(operation);

                Operation resultat = (Operation) in.readObject();
                System.out.println(" Résultat : " + resultat.getResultat());
            }

        } catch (Exception e) {
            System.out.println(" Erreur client : " + e.getMessage());
        }
    }
}

