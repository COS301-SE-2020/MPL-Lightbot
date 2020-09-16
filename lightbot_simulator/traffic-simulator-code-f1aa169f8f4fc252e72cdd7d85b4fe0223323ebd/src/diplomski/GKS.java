package diplomski;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GKS {
    private double xLast,yLast;
    private double sx,sy,px,py;
    private double[][] matrica={{1, 0, 0}, {0, 1, 0}, {0, 0, 1}};
    private Graphics2D g2d;

	public GKS(Graphics g, double min, double max, int size) {
        sx = size/(max-min);
        sy = size/(min-max);
        px = -sx*min;
        py = -sy*max;
        g2d = (Graphics2D) g.create();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
    }
	
	public GKS(Graphics g, double minX, double maxX, double minY, double maxY, int sizeX, int sizeY) {
        sx = sizeX/(maxX-minX);
        sy = sizeY/(minY-maxY);
        px = -sx*minX;
        py = -sy*maxY;
        g2d = (Graphics2D) g.create();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public int x_to_pix(double x) {
        return (int) (sx*x+px);
    }

    public int y_to_pix(double y) {
        return (int) (sy*y+py);
    }

    public void postaviNa(double x, double y) {
        xLast = matrica[0][0]*x+matrica[0][1]*y+matrica[0][2];
        yLast = matrica[1][0]*x+matrica[1][1]*y+matrica[1][2];
    }

    public void linijaDo(double x, double y) {
        int x1, y1, x2, y2;
        x1 = x_to_pix(xLast);
        y1 = y_to_pix(yLast);
        xLast = matrica[0][0]*x+matrica[0][1]*y+matrica[0][2];
        yLast = matrica[1][0]*x+matrica[1][1]*y+matrica[1][2];
        x2 = x_to_pix(xLast);
        y2 = y_to_pix(yLast);
        
        if (g2d != null)
        	g2d.drawLine(x1,y1,x2,y2);
        else
        	g2d.drawLine(x1,y1,x2,y2);
    }
    
    public void poligon(List<Double> x, List<Double> y) {
    	int[] xx = new int[x.size()];
    	int[] yy = new int[y.size()];
    	
        for (int i=0; i<x.size(); i++) {
            xLast = matrica[0][0]*x.get(i)+matrica[0][1]*y.get(i)+matrica[0][2];
            yLast = matrica[1][0]*x.get(i)+matrica[1][1]*y.get(i)+matrica[1][2];
            xx[i] = x_to_pix(xLast);
            yy[i] = y_to_pix(yLast);
        }
        
        g2d.fillPolygon(xx, yy, xx.length);
    }

    public void postaviBoju(Color c, float prozirnost) {
    	g2d.setColor(new Color(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, prozirnost));
    }
    
    public void postaviStroke(Stroke stroke) {
	    g2d.setStroke(stroke);
    }
    
    public void trans (MT2D m) {
        matrica=m.getMatrica();
    }
    
    public void nacrtajLiniju(double startX, double startY, double endX, double endY) {
		postaviNa(startX, startY);
		linijaDo(endX, endY);
	}
    
    public void nacrtajTekst(String tekst, int size, double x, double y) {
    	postaviNa(x, y);
    	int xx = x_to_pix(xLast);
        int yy = y_to_pix(yLast);
        g2d.setFont(new Font("Arial", 8, size));
        g2d.drawString(tekst, xx, yy);
    }
    
    public void nacrtajPravokutnik(double x, double y) {
    	postaviNa(-x, -y);
		linijaDo(-x, y);
		linijaDo(x, y);
		linijaDo(x, -y);
		linijaDo(-x, -y);
    }
    
    public void nacrtajPopunjeniPravokutnik(double x, double y) {
    	List<Double> xArray = Arrays.asList(-x, -x, x, x);
    	List<Double> yArray = Arrays.asList(-y, y, y, -y);
    	
    	poligon(xArray, yArray);
    }
    
    public void nacrtajPopunjeniTrapez(double x, double y, double difX1, double difX2, double difY1, double difY2) {
    	List<Double> xArray = Arrays.asList(-difX2 * x, -difX1 * x, difX1 * x, difX2 * x);
    	List<Double> yArray = Arrays.asList(-difY1 * y, difY1 * y, difY2 * y, -difY2 * y);
    	
    	poligon(xArray, yArray);
    }
    
    public void nacrtajTrupAuta(double x, double y, double xGore, double xDolje, double yGore, double yDolje) {
    	List<Double> xArray = Arrays.asList(-x, -x, -xGore * x, xGore * x, x, x, xDolje * x, -xDolje * x);
    	List<Double> yArray = Arrays.asList(-y, y, yGore * y, yGore * y, y, -y, -yDolje * y, -yDolje * y);
    	
    	poligon(xArray, yArray);
    }
    
    public void nacrtajPopunjeniTrokut(double x) {
    	List<Double> xArray = Arrays.asList(-2 * x, 0.0, 2 * x);
    	List<Double> yArray = Arrays.asList(-x, x, -x);
    	
    	poligon(xArray, yArray);
    }
    
    public void nacrtajElipsu(double a, double b) {
        postaviNa(a * Math.cos(0), b * Math.sin(0));
        double x;
        double y;
        for (double i=0; i <= 2*Math.PI; i += 0.01) {
            x = a * Math.cos(i);
            y = b * Math.sin(i);
            linijaDo(x,y);
        }
    }
    
    public void nacrtajPopunjenuElipsu(double a, double b) {
    	List<Double> x = new ArrayList<>();
    	List<Double> y = new ArrayList<>();
    	
        for (double i=0; i <= 2*Math.PI; i += 0.01) {
            x.add(a * Math.cos(i));
            y.add(b * Math.sin(i));
        }
        
        poligon(x, y);
    }
    
    public void nacrtajTekst(String tekst) {
    	int x = x_to_pix(matrica[0][2]);
        int y = y_to_pix(matrica[1][2]);
    	g2d.drawString(tekst, x, y);
    }
    
    public void postaviFont(Font font) {
        g2d.setFont(font);
    }
}
