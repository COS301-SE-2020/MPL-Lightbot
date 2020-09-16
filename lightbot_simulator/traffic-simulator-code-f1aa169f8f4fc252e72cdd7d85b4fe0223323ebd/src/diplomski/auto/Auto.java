package diplomski.auto;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import diplomski.GKS;
import diplomski.MT2D;
import diplomski.Main;
import diplomski.Semafor;
import diplomski.enums.Smjer;
import diplomski.enums.VrstaTrake;

public abstract class Auto {
	protected Color boja;
	protected double velicinaGraphics, velicina, x, y, rotacijaSmjer = 0;
	protected int smjer;
	protected int rotacija, simulationSpeed, zmigavac;
	protected long redniBroj;
	protected Semafor semafor;
	protected List<Auto> auti;
	protected boolean trebaObrisati = false;
	
	public Auto(int max, int rotacija, int simulationSpeed, Semafor semafor, List<Auto> auti, long redniBroj, int smjer) {
		Random random = new Random();
		velicinaGraphics = max * 2;
		velicina = velicinaGraphics/40;
		if (Main.brojIzlaznihTraka[rotacija / 90] == 1) {
			x = 0.08 * velicinaGraphics;
		}
		else {
			if (smjer == 1 || smjer == 0 && 
					Main.vrstaIzlaznihTraka[rotacija / 90] == VrstaTrake.LIJEVOGORE_DESNO.getVrstaTrake()) {
				x = 0.05 * velicinaGraphics;
			}
			else if (smjer == -1 || smjer == 0 && 
					Main.vrstaIzlaznihTraka[rotacija / 90] == VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake()) {
				x = 0.15 * velicinaGraphics;
			}
			else {//VrstaTrake.LIJEVOGORE_GOREDESNO
				switch(random.nextInt(2)) {
				case 0:
					x = 0.05 * velicinaGraphics;
					break;
				case 1:
					x = 0.15 * velicinaGraphics;
					break;
				}
			}
		}
		y = -1 * velicinaGraphics;
		this.rotacija = rotacija;
		this.simulationSpeed = simulationSpeed;
		this.semafor = semafor;
		this.auti = auti;
		this.redniBroj = redniBroj;
		
		switch(random.nextInt(6)) {
		case 0:
			boja = new Color(0xCC0000); break;//crvena
		case 1:
			boja = new Color(0x1975FF); break;//plava
		case 2:
			boja = new Color(0xA37547); break;//svijetlo zuta
		case 3:
			boja = new Color(0x2E8A5C); break;//zelena
		case 4:
			boja = new Color(0x009999); break;//tirkizna
		case 5:
			boja = new Color(0x643385); break;//ljubicasta
		}

		this.smjer = smjer;
		zmigavac = random.nextInt(60);
	}
	
	public abstract void nacrtajAuto(GKS gks, MT2D mat);
	
	public void kreciSe() {//kad su 2 izlazne trake, a jedna ulazna (ili obrnuto), ne idu na sredinu, al logika je dobra TODO
		for (Auto auto: auti) {
			if (auto.isClose(x, y, rotacija, redniBroj)) {
				Main.cekanje[rotacija / 90] += Math.round(1000.0 / 60) * simulationSpeed;
				return;
			}
		}
		
		if (y > -0.35 * velicinaGraphics - velicina/8 * simulationSpeed &&
				y < -0.35 * velicinaGraphics + velicina/8 * simulationSpeed &&
				semafor.getTrenutno() >= semafor.getZelenoStop()) {
			if (smjer == Smjer.DESNO.getSmjer() && semafor.strelicaDesnoUpaljena()) {
				for (Auto auto: auti) {
					if (!auto.canTurnRight(rotacija)) {
						Main.cekanje[rotacija / 90] += Math.round(1000.0 / 60) * simulationSpeed;
						return;
					}
				}
			}
			else {
				Main.cekanje[rotacija / 90] += Math.round(1000.0 / 60) * simulationSpeed;
				return;
			}
		}
		
		if (smjer == Smjer.LIJEVO.getSmjer() && y > -0.2 * velicinaGraphics - velicina/8 * simulationSpeed &&
				y < -0.2 * velicinaGraphics + velicina/8 * simulationSpeed) {
			for (Auto auto: auti) {
				if (auto.cantTurnLeft(rotacija)) {
					Main.cekanje[rotacija / 90] += Math.round(1000.0 / 60) * simulationSpeed;
					return;
				}
			}
		}
		
		if (smjer == Smjer.LIJEVO.getSmjer() && rotacijaSmjer == 0 && y > -0.3 * velicinaGraphics) {
			rotacijaSmjer = smjer;
			y += velicina/8 * simulationSpeed;
		}
		else if (smjer == Smjer.DESNO.getSmjer() && rotacijaSmjer == 0 && y > -0.25 * velicinaGraphics) {
			rotacijaSmjer = smjer * simulationSpeed;
			y += velicina/8 * simulationSpeed;
		}
		else if (rotacijaSmjer != 0) {
			if (Math.abs(rotacijaSmjer) < 90) {
				if (smjer == Smjer.DESNO.getSmjer()) {
					rotacijaSmjer += smjer * 1.2 * simulationSpeed;
					if (Main.brojIzlaznihTraka[rotacija / 90] == 1) {
						x += velicina/7 * -smjer * Math.sin(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
						y += velicina/10 * Math.cos(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
					}
					else {
						x += velicina/7 * -smjer * Math.sin(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
						y += velicina/12 * Math.cos(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
					}
				}
				else {
					if (Main.brojIzlaznihTraka[rotacija / 90] == 1) {
						rotacijaSmjer += smjer * 0.75 * simulationSpeed;
						x += velicina/6 * -smjer * Math.sin(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
						y += velicina/6 * Math.cos(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
					}
					else {
						rotacijaSmjer += smjer * 0.6 * simulationSpeed;
						x += velicina/6.8 * -smjer * Math.sin(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
						y += velicina/6.8 * Math.cos(Math.toRadians(Math.abs(rotacijaSmjer))) * simulationSpeed;
					}
				}
				
				if (Math.abs(rotacijaSmjer) >= 90) {
					zmigavac = -1;
				}
			}
			else {
				x += velicina/8 * -smjer * simulationSpeed;
			}
		}
		else {
			y += velicina/8 * simulationSpeed;
		}
		
		if (y > 0.6 * velicinaGraphics || Math.abs(x) > 0.6 * velicinaGraphics) {
			trebaObrisati = true;
		}
	}
	
	public boolean isTrebaObrisati() {
		return trebaObrisati;
	}
	
	public boolean jeLiPrijeSemafora(int rotacija) {
		return rotacija == this.rotacija
				&& y <= -0.1 * velicinaGraphics;
	}
	
	public void setSimulationSpeed(int simulationSpeed) {
		this.simulationSpeed = simulationSpeed;
	}
	
	private boolean isClose(double x, double y, double rotacija, long redniBroj) {
		if (Math.abs(this.x - x) < 3.5 * velicina && Math.abs(this.y - y) < 6 * velicina &&
				this.rotacija == rotacija && this.redniBroj < redniBroj) {
			return true;
		}
		
		return false;
	}
	
	private boolean cantTurnLeft(int rotacija) {
		if (smjer == Smjer.DESNO.getSmjer() && Main.brojUlaznihTraka[4 % (this.rotacija / 90 + 1)] == 2) {
			return false;
		}
		if (Math.abs(this.rotacija - rotacija) == 180 && smjer != Smjer.LIJEVO.getSmjer() && 
				y > -0.7 * velicinaGraphics &&
				y < 0.13 * velicinaGraphics && Math.abs(x) < 0.34 * velicinaGraphics) {
			
			if (y < -0.35 * velicinaGraphics + velicina/8 * simulationSpeed &&
					semafor.getTrenutno() >= semafor.getZelenoStop()) {
				return false;
			}
			
			if (Main.brojIzlaznihTraka[this.rotacija / 90] == 1 || 
					smjer == Smjer.RAVNO.getSmjer() && x == 0.05 * velicinaGraphics) {
				long najmanjiRedniBroj = redniBroj;
				for (Auto auto: auti) {
					najmanjiRedniBroj = auto.najmanjiRedniBrojKojiCeka(najmanjiRedniBroj, this.rotacija);
				}
				if (redniBroj == najmanjiRedniBroj) {
					return true;
				}
			}
			else {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Vezano za strelicu udesno; auto provjerava jel ide netko s lijeve strane
	 * @param rotacija
	 * @return
	 */
	private boolean canTurnRight(int rotacija) {
		if (smjer == Smjer.RAVNO.getSmjer() && ((this.rotacija - rotacija) == -90 || 
				(this.rotacija - rotacija) == 270) && y > -0.28 * velicinaGraphics &&
				(x != 0.05 * velicinaGraphics || Main.brojUlaznihTraka[4 % (rotacija / 90 + 1)] == 1)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Ako dva auta iz suprotnih smjerova �ele skrenuti ulijevo
	 * @param redniBroj
	 * @param rotacija
	 * @return
	 */
	private long najmanjiRedniBrojKojiCeka(long redniBroj, double rotacija) {
		if (this.rotacija == rotacija && smjer == Smjer.LIJEVO.getSmjer() &&
				this.redniBroj < redniBroj && y <= -0.15 * velicinaGraphics && y > -0.4 * velicinaGraphics) {
			return this.redniBroj;
		}
		
		return redniBroj;
	}
}
