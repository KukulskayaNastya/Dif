import java.util.Locale;

public class Main {

    static double h = 0.10;
    static double x0 = 0;
    static double y0 = 1;

    public static void main (String[] args) {

        double[] X = new double[11];
        X[0] = x0;
        double[] Y = new double[11];
        Y[0] = y0;
        for (int i = 1; i <= 4; i++) {
            X[i] = X[i-1] + h;
            Y[i] = Y[i-1] + K(X[i-1], Y[i-1]);
        }
        System.out.println("Runge-Kutt:");
        Locale l = Locale.ENGLISH;
        for (int i = 0; i <= 4; i++) {
            System.out.println(String.format(l, "%.1f", X[i]) + "     " + String.format(l, "%.10f", Y[i]));
        }

        double[][] N = new double[5][];
        for (int i = 0; i <= 4; i++) {
            N[i] = new double[11 - i];
            N[0][i] = n(X[i], Y[i]);
        }
        for (int i = 5; i <= 10; i++) {
            X[i] = X[i-1] + h;
            addN(N);
            Y[i] = Y[i-1] + N[0][i-1] + N[1][i-2] / 2 + 5 * N[2][i-3] / 12 + 3 * N[3][i-4] / 8 + 251 * N[4][i-5] / 720;
            N[0][i] = n(X[i], Y[i]);
        }
        addN(N);
        printTable(X, Y, N);

        System.out.println();
        System.out.println("Euler:");
        int[] K = {10,5,20};
        double[] H = {h, 2*h, h/2};
        for (int j=0;j<=2;j++){
            System.out.println(" xk          yk");
            double[] Y1 = new double[K[j]+1]; Y1[0]=y0;
            double[] X1 = new double[K[j]+1]; X1[0]=x0;
            for (int i=1; i<Y1.length; i++){
                X1[i] = X1[i-1] + H[j];
                Y1[i] = Y1[i-1]+H[j]*f(X1[i-1],Y1[i-1]);
            }
            for (int i=0; i<Y1.length; i++){
                System.out.println(String.format(l, "%.2f",X1[i])+"     "+String.format(l, "%.10f",Y1[i]));
            }
        }
    }

    static double f(double x, double y){
        return x*Math.pow(y,3)-y;
    }

    static double n(double x, double y){
        return h*f(x,y);
    }

    static double K(double x, double y){
        double k1 = h*f(x,y);
        double k2 = h*f(x+h/2,y+k1/2);
        double k3 = h*f(x+h/2,y+k2/2);
        double k4 = h*f(x+h,y+k3);
        return (k1 + 2*k2 + 2*k3 + k4)/6;
    }

    static void addN(double[][] N){
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < N[i].length; j++) {
                N[i][j] = N[i - 1][j + 1] - N[i - 1][j];
            }
        }
    }

    static void printTable(double[] X, double[] Y, double[][] D) {
        if (Y.length==11) {
            Locale l = Locale.ENGLISH;
            System.out.println();
            System.out.println("Adams:");
            int delta = 4;
            String[] S = new String[delta + 1];
            for (int i = 1; i <= 2 * D[0].length - 1; i++) {
                for (int j = 1; j <= delta; j++) {
                    S[j] = "";
                }
                if (i % 2 != 0) {
                    String PRINT = String.format(l, "%.1f", X[(i - 1) / 2]) + "   " + String.format(l, "%.10f", Y[(i - 1) / 2]) + "   ";
                    for (int j = 0; j <= delta; j += 2) {
                        if (i >= j + 1 && i <= 2 * D[0].length - (j + 1)) {
                            S[j] = String.format(l, "%.6f", D[j][(i - 1) / 2 - (j / 2)]);
                        }
                        PRINT = PRINT + S[j] + "            ";
                    }
                    System.out.println(PRINT);
                } else {
                    String PRINT = "                                ";
                    for (int j = 1; j <= delta; j += 2) {
                        if (i >= j + 1 && i <= 2 * D[0].length - (j + 1)) {
                            S[j] = String.format(l, "%.6f", D[j][(i - j + 1) / 2 - 1]);
                        }
                        PRINT = PRINT + S[j] + "             ";
                    }
                    System.out.println(PRINT);
                }
            }
        }
    }
}