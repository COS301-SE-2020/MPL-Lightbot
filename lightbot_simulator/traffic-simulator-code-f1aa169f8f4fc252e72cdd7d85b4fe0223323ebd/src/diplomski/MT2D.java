package diplomski;

public class MT2D {

private double[][] matrica;
    
    public MT2D () {
        identitet();
    }
    
    public void identitet () {
        double[][] m={{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        matrica=m;
    }
    
    public double[][] getMatrica() {
        return matrica;
    }
    
    public void pomakni (double px, double py) {
        double[][] m={{1, 0, px}, {0, 1, py}, {0, 0, 1}};
        mult(m);
    }
    
    public void skaliraj (double sx, double sy) {
        double[][] m={{sx, 0, 0}, {0, sy, 0}, {0, 0, 1}};
        mult(m);
    }
    
    public void skalirajOkoTocke (double x, double y, double f) {
        pomakni(-x, -y);
        skaliraj(f, f);
        pomakni(x, y);
    }
    
    public void zrcaliNaX () {
        double[][] m={{1, 0, 0}, {0, -1, 0}, {0, 0, 1}};
        mult(m);
    }
    
    public void zrcaliNaY () {
        double[][] m={{-1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
        mult(m);
    }
    
    public void zrcaliNa (double k, double l) {
        double kutPravca=Math.toDegrees(Math.atan(k));
        pomakni(0, l);
        rotiraj(kutPravca);
        zrcaliNaX();
        rotiraj(-kutPravca);
        pomakni(0, -l);
    }
    
    public void zrcaliNaPravcu (double x1, double y1, double x2, double y2) {
        double k=0;
        if ((x2-x1)!=0)
            k=(y2-y1)/(x2-x1);
        else
            k=10000000;
        double l=y1+k*(-x1);
        
        double kutPravca=Math.toDegrees(Math.atan(k));
        pomakni(0, l);
        rotiraj(kutPravca);
        zrcaliNaX();
        rotiraj(-kutPravca);
        pomakni(0, -l);
    }
    
    public void rotiraj (double kut) {
        double rad=Math.toRadians(kut);
        double[][] m={{Math.cos(rad), -Math.sin(rad), 0}, {Math.sin(rad), Math.cos(rad), 0}, {0, 0, 1}};
        mult(m);
    }
    
    public void rotiraj_oko_tocke (double x0, double y0, double kut) {
        pomakni(x0, y0);
        rotiraj(kut);
        pomakni(-x0, -y0);
    }
    
    public void mult(double[][] m) {
        double[][] rjesenje=new double[3][3];
        for (int c = 0 ; c < 3 ; c++ ) {
            for (int d = 0 ; d < 3 ; d++ ) {  
               for (int k = 0 ; k < 3 ; k++ ) {
                  rjesenje[c][d] += matrica[c][k]*m[k][d];
               }
            }
        }
        matrica=rjesenje;
    }
}
