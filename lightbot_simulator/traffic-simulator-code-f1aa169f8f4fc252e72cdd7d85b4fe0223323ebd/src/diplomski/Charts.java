package diplomski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.swing.JPanel;

public class Charts extends JPanel {
	private static final long serialVersionUID = 1L;
	private int velicinaCharts;
	private GKS gks;
	private double minX, maxX, minY, maxY;
	private Animacija animacija;
	private double granica = 0.8;
	private Color[] bojeBarova = {new Color(0x3333FF), new Color(0x0066FF), new Color(0x0099CC), new Color(0x009999)};
	
	public Charts (int velicinaCharts) {
		this.velicinaCharts = velicinaCharts;
		minX = -0.5 * velicinaCharts;
		maxX = 0.5 * velicinaCharts;
		minY = -1 * velicinaCharts;
		maxY = 1 * velicinaCharts;
		
		animacija = new Animacija(60);
		animacija.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		gks=new GKS(g, minX, maxX, minY, maxY, (int)(2.5 * velicinaCharts), velicinaCharts);
		
		nacrtajSkalu();
		nacrtajOznake();
		nacrtajLegendu();
		nacrtajMjerila();
		nacrtajStupce();
	}
	
	private void nacrtajSkalu() {
		gks.nacrtajLiniju(granica * minX, granica * minY, granica * minX, granica * maxY);
		gks.nacrtajLiniju(granica * minX, granica * maxY, (granica + 0.02) * minX, (granica - 0.05) * maxY);
		gks.nacrtajLiniju(granica * minX, granica * maxY, (granica - 0.02) * minX, (granica - 0.05) * maxY);
		gks.nacrtajLiniju(granica * minX, granica * minY, granica * maxX, granica * minY);
		gks.nacrtajLiniju(granica * maxX, granica * minY, (granica - 0.025) * maxX, (granica + 0.05) * minY);
		gks.nacrtajLiniju(granica * maxX, granica * minY, (granica - 0.025) * maxX, (granica - 0.05) * minY);
	}
	
	private void nacrtajOznake() {
		gks.nacrtajTekst("Avg. wait", 12, (granica + 0.1) * minX, (granica + 0.12) * maxY);
		gks.nacrtajTekst("in seconds", 12, (granica + 0.1) * minX, (granica + 0.03) * maxY);
		gks.nacrtajTekst("Direction", 12, (granica - 0.08) * maxX, (granica + 0.13) * minY);
	}
	
	private void nacrtajLegendu() {//mene
		List<String> legenda = Arrays.asList("Down", "Right", "Up", "Left");
		legenda.stream()
			.forEach(l -> gks.nacrtajTekst(l, 18, (granica - 0.4 * legenda.indexOf(l) - 0.14) * minX, (granica + 0.15) * minY));
	}
	
	private void nacrtajMjerila() {
		Double max = dajMaksimalnuVrijednost();
		gks.nacrtajLiniju((granica + 0.02) * minX, 0.1 * minY, (granica - 0.02) * minX, 0.1 * minY);
		gks.nacrtajTekst(Math.round(max / 20) / 100.0 + "", 18, (granica + 0.16) * minX, 0.15 * minY);
		gks.nacrtajLiniju((granica + 0.02) * minX, 0.6 * maxY, (granica - 0.02) * minX, 0.6 * maxY);
		gks.nacrtajTekst(Math.round(max / 10) / 100.0 + "", 18, (granica + 0.16) * minX, 0.55 * maxY);
	}
	
	private void nacrtajStupce() {
		gks.postaviStroke(new BasicStroke(60, 0, 0));
		List<Double> postotci = dajPostotke();
		double maxPostotak = dajMaksimalnuVrijednost();
		for (int i = 0; i < 4; i ++) {
			double postotak = 0;
			if (Main.brojAuta[i] > 0 && maxPostotak > 0) {
				postotak = postotci.get(i) / maxPostotak;
			}
			gks.postaviBoju(bojeBarova[i], 1);
			gks.nacrtajLiniju((granica - 0.4 * i - 0.2) * minX, granica * minY, 
					(granica - 0.4 * i - 0.2) * minX, (granica - postotak * 1.4) * minY);
		}
	}
	
	public List<Double> dajPostotke() {
		List<Double> postotci = new ArrayList<Double>();
		for (int i = 0; i < 4; i ++) {
			if (Main.brojAuta[i] > 0) {
				postotci.add((double)Main.cekanje[i] / Main.brojAuta[i]);
			}
			else {
				postotci.add(0.0);
			}
		}
		return postotci;
	}
	
	private Double dajMaksimalnuVrijednost() {
		List<Double> postotci = dajPostotke();
		return postotci.stream()
				.max(Comparator.comparing(v -> v)).get();
	}
	
	private class Animacija extends Thread {
        private long pauza, paintStartTime, paintEndTime;
        
        Animacija (double fps) {
            pauza = Math.round(1000.0/fps);
        }
        
        public void run() {
            while(true) {//!kraj
            	paintStartTime = System.currentTimeMillis();
        		
                repaint();
                
            	paintEndTime = System.currentTimeMillis();

                try {
                	if (pauza > (paintEndTime - paintStartTime)) {
                        sleep(pauza - (paintEndTime - paintStartTime));
                	}
                } catch(InterruptedException e) {}
            }
        }
    }
}
