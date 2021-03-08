/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ambiente;

import java.util.StringTokenizer;

/**
 *
 * @author USRBET
 */
public class Parametros {

    //"-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200"
    private double L, M;
    private int N, V, S, E, H;

    public double getL() {
        return L;
    }

    public void setL(double L) {
        this.L = L;
    }

    public double getM() {
        return M;
    }

    public void setM(double M) {
        this.M = M;
    }

    public int getN() {
        return N;
    }

    public void setN(int N) {
        this.N = N;
    }

    public int getV() {
        return V;
    }

    public void setV(int V) {
        this.V = V;
    }

    public int getS() {
        return S;
    }

    public void setS(int S) {
        this.S = S;
    }

    public int getE() {
        return E;
    }

    public void setE(int E) {
        this.E = E;
    }

    public int getH() {
        return H;
    }

    public void setH(int H) {
        this.H = H;
    }

    public Parametros(double L, double M, int N, int V, int S, int E, int H) {
        this.L = L;
        this.M = M;
        this.N = N;
        this.V = V;
        this.S = S;
        this.E = E;
        this.H = H;
    }

    public Parametros(String param) {
        //-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200
        String[] aux = param.split(" ");
        this.L = Double.parseDouble(aux[1]);
        this.M = Double.parseDouble(aux[3]);
        this.N = Integer.parseInt(aux[5]);
        this.V = Integer.parseInt(aux[7]);
        this.S = Integer.parseInt(aux[9]);
        this.E = Integer.parseInt(aux[11]);
        this.H = Integer.parseInt(aux[13]);
    }

//    public static void main(String[] args) {
//        //-L 0.5 -M 0.1 -N 100 -V 0 -S 0 -E 20 -H 200
//        String aux = "-L 0.5234324 -M 0.1234234 -N 100 -V 0 -S 0 -E 20 -H 200";
//        String[] a1 = aux.split(" ");
//        for (int i = 0; i < a1.length; i++) {
//            String a11 = a1[i];
//            System.out.println(a11);
//        }
//
//        // System.out.println(aux.substring(aux.indexOf("-L"+1), aux.indexOf("-M")1));
////    for (StringTokenizer stringTokenizer = new StringTokenizer(aux); stringTokenizer.hasMoreTokens();) {
////        String token = stringTokenizer.nextToken();
////        System.out.println(token);
////        
////    }
//    }
    public Parametros() {
    }

    public void reajuste() {

    }

    @Override
    public String toString() {
        return "-L " + this.getL() + " -M " + this.getM() + " -N " + this.getN() + " -V " + this.getV()
                + " -S " + this.getS() + " -E " + this.getE() + " -H " + this.getH() + "";
    }

}
