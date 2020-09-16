package diplomski;

import java.awt.Color;


public class Semafor {

	private double radijus;
	private int ciklus, trenutno, zelenoStop, strelicaDesno;//ako je -1 onda je semafor ulijevo
	private boolean lijevi;
	
	public Semafor(int max, int ciklus, int trenutno, int zelenoStop, boolean lijevi, int strelicaDesno) {
		radijus = max / 34.125;
		this.ciklus = ciklus;
		this.trenutno = trenutno;
		this.zelenoStop = zelenoStop;
		this.strelicaDesno = strelicaDesno;
		this.lijevi = lijevi;
	}
	
	public void nacrtajSemafor(GKS gks, MT2D mat) {
		gks.postaviBoju(Color.BLACK, 0.8f);
		gks.nacrtajPopunjeniPravokutnik(2 * radijus, 4 * radijus);
		
		//zeleno svijetlo
		mat.pomakni(0, -2.5 * radijus);
		gks.trans(mat);
		gks.postaviBoju(Color.GREEN, 0.8f);
		if (trenutno < zelenoStop) {
			gks.nacrtajPopunjenuElipsu(radijus, radijus);
		}
		else {
			gks.nacrtajElipsu(radijus, radijus);
		}
		
		//žuto svijetlo
		mat.pomakni(0, 2.5 * radijus);
		gks.trans(mat);
		gks.postaviBoju(Color.YELLOW, 0.8f);
		if (trenutno >= zelenoStop && trenutno < (zelenoStop + 2) || trenutno >= (ciklus - 2)) {
			gks.nacrtajPopunjenuElipsu(radijus, radijus);
		}
		else {
			gks.nacrtajElipsu(radijus, radijus);
		}
		
		//crveno svijetlo
		mat.pomakni(0, 2.5 * radijus);
		gks.trans(mat);
		gks.postaviBoju(Color.RED, 0.8f);
		if (trenutno >= (zelenoStop + 2)) {
			gks.nacrtajPopunjenuElipsu(radijus, radijus);
		}
		else {
			gks.nacrtajElipsu(radijus, radijus);
		}
		
		if (strelicaDesnoUpaljena()) {
			mat.pomakni(3.3 * radijus, -5 * radijus);
			gks.trans(mat);
			gks.postaviBoju(Color.BLACK, 0.8f);
			gks.nacrtajPopunjeniPravokutnik(1.3 * radijus, 1.5 * radijus);
			gks.postaviBoju(Color.GREEN, 0.8f);
			mat.pomakni(-0.5 * radijus, 0);
			gks.trans(mat);
			gks.nacrtajPopunjeniPravokutnik(0.5 * radijus, 0.4 * radijus);
			mat.pomakni(radijus, 0);
			mat.rotiraj(-90);
			gks.trans(mat);
			gks.nacrtajPopunjeniTrokut(0.5 * radijus);
		}
	}
	
	public void povecajVrijemeZaSekundu() {
		trenutno++;
		if (trenutno >= ciklus)
			trenutno = 0;
	}
	
	public int getTrenutno() {
		return trenutno;
	}
	
	public int getZelenoStop() {
		return zelenoStop;
	}
	
	public boolean isLijevi() {
		return lijevi;
	}
	
	public boolean strelicaDesnoUpaljena() {
		return trenutno >= (ciklus - strelicaDesno - 2) 
				&& trenutno < (ciklus - 2);
	}
}
