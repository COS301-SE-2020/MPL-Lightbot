package diplomski;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JPanel;

import diplomski.auto.Auto;
import diplomski.auto.AutoKamionet;
import diplomski.auto.AutoKombi;
import diplomski.auto.AutoMiniCooper;
import diplomski.auto.AutoObicni;
import diplomski.enums.Smjer;

public class GraphicsSimulator extends JPanel {
	private static final long serialVersionUID = 1L;
	private int velicinaGraphics;
	private double gustocaTop[];
	private GKS gks;
	private MT2D mat;
	private double min, max;
	private Raskrizje raskrizje;
	private List<Semafor> semafori;
	private List<Auto> auti;
	private Animacija animacija;
	private Random random;
	
	public GraphicsSimulator (int velicinaGraphics) {
		this.velicinaGraphics = velicinaGraphics;
		mat = new MT2D();
		min = -0.35 * velicinaGraphics;
		max = 0.35 * velicinaGraphics;
		gustocaTop = new double[4];
		random = new Random();
		
		auti = new ArrayList<>();
		
		raskrizje = new Raskrizje(velicinaGraphics, min, max, mat);
		
		animacija = new Animacija(60, 1);
		
		resetiraj();
		animacija.start();
	}
	
	public void setSimulationSpeed(int simulationSpeed) {
    	animacija.setSimulationSpeed(simulationSpeed);
    }
	
	public void resetiraj() {
		semafori = new ArrayList<>();
		int maxGoreDolje = Integer.max(Main.semafor[0], Main.semafor[2]);
		int maxLijevoDesno = Integer.max(Main.semafor[1], Main.semafor[3]);
		int maxGoreDoljeLijevo = Integer.max(Main.semaforLijevo[0], Main.semaforLijevo[2]);
		maxGoreDoljeLijevo = maxGoreDoljeLijevo > 0 ? maxGoreDoljeLijevo + 6 : 0;
		int maxLijevoDesnoLijevo = Integer.max(Main.semaforLijevo[1], Main.semaforLijevo[3]);
		maxLijevoDesnoLijevo = maxLijevoDesnoLijevo > 0 ? maxLijevoDesnoLijevo + 6 : 0;
		int ciklus = maxGoreDolje + maxLijevoDesno + maxGoreDoljeLijevo + maxLijevoDesnoLijevo + 12;
		semafori.add(new Semafor((int)max, ciklus, maxLijevoDesno + maxLijevoDesnoLijevo + 6, Main.semafor[3], false, Main.strelicaDesno[3]));
		semafori.add(new Semafor((int)max, ciklus, 0, Main.semafor[0], false, Main.strelicaDesno[0]));
		semafori.add(new Semafor((int)max, ciklus, maxLijevoDesno + maxLijevoDesnoLijevo + 6, Main.semafor[1], false, Main.strelicaDesno[1]));
		semafori.add(new Semafor((int)max, ciklus, 0, Main.semafor[2], false, Main.strelicaDesno[2]));
		semafori.add(new Semafor((int)max, ciklus, maxLijevoDesno + maxLijevoDesnoLijevo - 2, Main.semaforLijevo[3], true, 0));
		semafori.add(new Semafor((int)max, ciklus, ciklus - Main.semafor[0] - 6, Main.semaforLijevo[0], true, 0));
		semafori.add(new Semafor((int)max, ciklus, maxLijevoDesno + maxLijevoDesnoLijevo - 2, Main.semaforLijevo[1], true, 0));
		semafori.add(new Semafor((int)max, ciklus, ciklus - Main.semafor[2] - 6, Main.semaforLijevo[2], true, 0));
		
		for (int i = 0; i < 4; i ++) {
			gustocaTop[i] = izracunajGustocaTop(i);
		}
		
		auti.clear();
		animacija.resetirajBrojace();
		raskrizje.setSemafori(semafori);
		for (int i = 0; i < 4; i ++) {
			Main.brojAuta[i] = 0;
			Main.cekanje[i] = 0;
		}
		Main.brojSekundi.setText("0");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		gks=new GKS(g, min, max, velicinaGraphics);
		
		raskrizje.nacrtajRaskrizje(g, gks);
		
		for (int i = 0; i < auti.size(); i ++) {
			auti.get(i).nacrtajAuto(gks, mat);
		}
		
		raskrizje.nacrtajSemafore(gks);
		raskrizje.nacrtajBrojVozilaIzSvakogSmjera(gks, auti);
	}
	
	private double izracunajGustocaTop(int i) {
		Integer razlika = Math.abs(Main.gustoca2[i] - Main.gustoca1[i]);
		Integer manjiBroj = Main.gustoca1[i] > Main.gustoca2[i] ? Main.gustoca2[i] : Main.gustoca1[i];
		Double procijenjenBrojVozila = random.nextDouble() * razlika + manjiBroj;
		return procijenjenBrojVozila > 0 ? 60000.0 / procijenjenBrojVozila : -1;
	}
	
	private class Animacija extends Thread {
        private long pauza, brojacSekunde = 0, autoId = 0, paintStartTime, paintEndTime;
        private double fps;
        private int simulationSpeed;
    	private long brojac[] = {0, 0, 0, 0};
        
        Animacija (double fps, int simulationSpeed) {
            pauza = Math.round(1000.0/fps);
            this.fps = fps;
            this.simulationSpeed = simulationSpeed;
        }
        
        public void run() {
            while(true) {//!kraj
            	paintStartTime = System.currentTimeMillis();
            	
            	for (int i = 0; i < auti.size(); i ++) {
                	auti.get(i).kreciSe();
                }
            	List<Auto> autiZaBrisanje = auti.stream()
            			.filter(auto -> auto.isTrebaObrisati())
            			.collect(Collectors.toList());
            	autiZaBrisanje.stream()
            		.forEach(auto -> auti.remove(auto));
            	
                
                brojacSekunde += simulationSpeed;
                if (brojacSekunde >= fps) {//sekunda
            		semafori.stream()
            			.forEach(semafor -> semafor.povecajVrijemeZaSekundu());
            		brojacSekunde = 0;
            		Long brojSekundi = Long.parseLong(Main.brojSekundi.getText()) + 1;
            		Main.brojSekundi.setText(brojSekundi + "");
                }
                
                for (int i = 0; i < gustocaTop.length; i ++) {
                	if (brojac[i] > gustocaTop[i] && gustocaTop[i] >= 0) {
                		dodajNoviAuto(i * 90);
                		Main.brojAuta[i] ++;
                		brojac[i] = 0;
            			gustocaTop[i] = izracunajGustocaTop(i);
                	}
                	brojac[i] += pauza * simulationSpeed;
                }
        		
                repaint();
                
            	paintEndTime = System.currentTimeMillis();

                try {
                	if (pauza > (paintEndTime - paintStartTime)) {
                        sleep(pauza - (paintEndTime - paintStartTime));
                	}
                } catch(InterruptedException e) {}
            }
        }
        
        public void setSimulationSpeed(int simulationSpeed) {
        	brojacSekunde = 0;
        	this.simulationSpeed = simulationSpeed;
        	auti.stream()
        		.forEach(auto -> auto.setSimulationSpeed(simulationSpeed));
        }
        
        public void resetirajBrojace() {
        	for (int i = 0; i < brojac.length; i ++) {
        		brojac[i] = 0;
        	}
        }
        
        public void dodajNoviAuto(int rotacija) {
    		int smjer = Smjer.RAVNO.getSmjer();
			float smjerRandom = random.nextFloat(), vjLijevo = 0, vjDesno = 0;
    		Semafor semaforNaKojiAutoDolazi = null;
    		
    		switch (rotacija) {
    		case 0:
    			semaforNaKojiAutoDolazi = semafori.get(1);
    			vjLijevo = (float)Main.vjerojatnost[3] / (Main.vjerojatnost[3] + Main.vjerojatnost[1] + Main.vjerojatnost[2]);
    			vjDesno = (float)Main.vjerojatnost[1] / (Main.vjerojatnost[3] + Main.vjerojatnost[1] + Main.vjerojatnost[2]);
    			break;
    		case 90:
    			semaforNaKojiAutoDolazi = semafori.get(2);
    			vjLijevo = (float)Main.vjerojatnost[0] / (Main.vjerojatnost[3] + Main.vjerojatnost[0] + Main.vjerojatnost[2]);
    			vjDesno = (float)Main.vjerojatnost[2] / (Main.vjerojatnost[3] + Main.vjerojatnost[0] + Main.vjerojatnost[2]);
    			break;
    		case 180:
    			semaforNaKojiAutoDolazi = semafori.get(3);
    			vjLijevo = (float)Main.vjerojatnost[1] / (Main.vjerojatnost[3] + Main.vjerojatnost[1] + Main.vjerojatnost[0]);
    			vjDesno = (float)Main.vjerojatnost[3] / (Main.vjerojatnost[3] + Main.vjerojatnost[1] + Main.vjerojatnost[0]);
    			break;
    		case 270:
    			semaforNaKojiAutoDolazi = semafori.get(0);
    			vjLijevo = (float)Main.vjerojatnost[2] / (Main.vjerojatnost[0] + Main.vjerojatnost[1] + Main.vjerojatnost[2]);
    			vjDesno = (float)Main.vjerojatnost[0] / (Main.vjerojatnost[0] + Main.vjerojatnost[1] + Main.vjerojatnost[2]);
    			break;
    		}
    		
			if (smjerRandom < vjLijevo) {
				smjer = Smjer.LIJEVO.getSmjer();
				switch (rotacija) {
				case 0: 
					if (semafori.get(5).getZelenoStop() > 0) {
						semaforNaKojiAutoDolazi = semafori.get(5);
					}
					break;
				case 90: 
					if (semafori.get(6).getZelenoStop() > 0) {
						semaforNaKojiAutoDolazi = semafori.get(6);
					}
					break;
				case 180: 
					if (semafori.get(7).getZelenoStop() > 0) {
						semaforNaKojiAutoDolazi = semafori.get(7);
					}
					break;
				case 270: 
					if (semafori.get(4).getZelenoStop() > 0) {
						semaforNaKojiAutoDolazi = semafori.get(4);
					}
					break;
				}
			}
			else if (smjerRandom < vjLijevo + vjDesno) {
				smjer = Smjer.DESNO.getSmjer();
			}
    		
    		Auto auto = null;
    		switch (random.nextInt(4)) {
    		case 0:
    			auto = new AutoObicni((int)max, rotacija, simulationSpeed, semaforNaKojiAutoDolazi, auti, autoId, smjer);
    			break;
    		case 1:
    			auto = new AutoMiniCooper((int)max, rotacija, simulationSpeed, semaforNaKojiAutoDolazi, auti, autoId, smjer);
    			break;
    		case 2:
    			auto = new AutoKamionet((int)max, rotacija, simulationSpeed, semaforNaKojiAutoDolazi, auti, autoId, smjer);
    			break;
    		case 3:
    			auto = new AutoKombi((int)max, rotacija, simulationSpeed, semaforNaKojiAutoDolazi, auti, autoId, smjer);
    			break;
    		}
    		
    		auti.add(auto);
    		autoId++;
        }
    }
}
