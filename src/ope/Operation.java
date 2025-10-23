package ope;
import java.io.Serializable;

public class Operation implements Serializable {
    private double op1, op2;
    private char operateur;
    private double resultat;

    public Operation(double op1, double op2, char operateur) {
        this.op1 = op1;
        this.op2 = op2;
        this.operateur = operateur;
    }

    public double getOp1() { return op1; }
    public double getOp2() { return op2; }
    public char getOperateur() { return operateur; }
    public double getResultat() { return resultat; }

    public void calculer() {
        switch (operateur) {
            case '+': resultat = op1 + op2; break;
            case '-': resultat = op1 - op2; break;
            case '*': resultat = op1 * op2; break;
            case '/': resultat = op2 != 0 ? op1 / op2 : 0; break;
            default: resultat = 0;
        }
    }
}
