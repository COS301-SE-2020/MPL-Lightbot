package diplomski;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;

import diplomski.auto.Auto;
import diplomski.enums.VrstaTrake;


public class Raskrizje extends JPanel {
	private static final long serialVersionUID = 1L;
	private int velicina;
	private MT2D mat;
	private double min, max;
	private List<Semafor> semafori;
	
	public Raskrizje (int velicinaGraphics, double min, double max, MT2D mat) {
		velicina = velicinaGraphics;
		this.min = min;
		this.max = max;
		this.mat = mat;
	}

	public void nacrtajRaskrizje(Graphics g, GKS gks) {
		nacrtajTeren(g, gks);
		nacrtajCestovneLinije(gks);
		nacrtajIsprekidaneLinije(gks);
		nacrtajLinijePoPotrebi(gks);
		nacrtajStrelice(gks);
		nacrtajPjesackePrijelaze(gks);
		nacrtajTravnatuPovrsinu(gks);
	}
	
	private void nacrtajTeren(Graphics g, GKS gks) {
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, velicina, velicina);
	}
	
	private void nacrtajCestovneLinije(GKS gks) {
		gks.postaviBoju(Color.BLACK, 1);
		for (int i=-1; i<3; i++) {
			mat.identitet();
			mat.rotiraj(i * 90);
			gks.trans(mat);
			gks.nacrtajLiniju(0.4 * min, min, 0.4 * min, 0.4 * min);
			gks.nacrtajLiniju(0.4 * max, min, 0.4 * max, 0.4 * min);
		}
	}
	
	private void nacrtajIsprekidaneLinije(GKS gks) {
		gks.postaviBoju(Color.WHITE, 1);
		mat.identitet();
		gks.trans(mat);
		gks.postaviStroke(new BasicStroke((int)(0.02 * max), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{(float)max / 22.75f}, 0));
		for (int i=0; i<4; i++) {
			mat.rotiraj(90);
			gks.trans(mat);
			gks.nacrtajLiniju(0, min, 0, 0.6 * min);
		}
		gks.postaviStroke(new BasicStroke());
	}
	
	private void nacrtajLinijePoPotrebi(GKS gks) {
		gks.postaviBoju(Color.WHITE, 1);
		mat.identitet();
		gks.trans(mat);
		gks.postaviStroke(new BasicStroke((int)(0.015 * max)));
		for (int i=0; i<4; i++) {
			if (Main.brojIzlaznihTraka[i] == 2) {
				gks.nacrtajLiniju(0.2 * max, min, 0.2 * max, 0.6 * min);
			}
			if (Main.brojUlaznihTraka[i] == 2) {
				gks.nacrtajLiniju(0.2 * min, min, 0.2 * min, 0.6 * min);
			}
			mat.rotiraj(90);
			gks.trans(mat);
		}
		gks.postaviStroke(new BasicStroke());
	}
	
	private void nacrtajStrelice(GKS gks) {
		gks.postaviBoju(Color.WHITE, 1);
		for (int i = 0; i < 4; i ++) {
			if (Main.brojIzlaznihTraka[i] == 2) {
				for (int j = 0; j < 2; j ++) {
					mat.identitet();
					mat.rotiraj(i * 90);
					mat.pomakni((0.03 + j * 0.07) * velicina, -0.26 * velicina);
					gks.trans(mat);
					
					if (Main.vrstaIzlaznihTraka[i] == VrstaTrake.LIJEVO_GOREDESNO.getVrstaTrake()) {
						if (j == 0) {
							nacrtajStrelicuLijevo(gks);
						}
						else {
							nacrtajStrelicuDesnoGore(gks);
						}
					}
					else if (Main.vrstaIzlaznihTraka[i] == VrstaTrake.LIJEVOGORE_DESNO.getVrstaTrake()) {
						if (j == 0) {
							nacrtajStrelicuLijevoGore(gks);
						}
						else {
							nacrtajStrelicuDesno(gks);
						}
					}
					else {//VrstaTrake.LIJEVOGORE_GOREDESNO
						if (j == 0) {
							nacrtajStrelicuLijevoGore(gks);
						}
						else {
							nacrtajStrelicuDesnoGore(gks);
						}
					}
				}
			}
		}
	}
	
	private void nacrtajStrelicuDesno(GKS gks) {
		gks.nacrtajPopunjeniPravokutnik(0.015 * max, 0.06 * max);
		mat.pomakni(0.03 * max, 0.045 * max);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.035 * max, 0.015 * max);
		mat.pomakni(0.045 * max, 0);
		mat.rotiraj(-90);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrokut(0.02 * max);
	}
	
	private void nacrtajStrelicuDesnoGore(GKS gks) {
		nacrtajStrelicuDesno(gks);
		mat.pomakni(-0.035 * max, -0.075 * max);
		mat.rotiraj(90);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.015 * max, 0.02 * max);
		mat.pomakni(-0.0005 * max, 0.04 * max);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrokut(0.02 * max);
	}
	
	private void nacrtajStrelicuLijevo(GKS gks) {
		mat.pomakni(0.04 * max, 0);
		mat.zrcaliNaY();
		gks.trans(mat);
		nacrtajStrelicuDesno(gks);
	}
	
	private void nacrtajStrelicuLijevoGore(GKS gks) {
		mat.pomakni(0.04 * max, 0);
		mat.zrcaliNaY();
		gks.trans(mat);
		nacrtajStrelicuDesnoGore(gks);
	}
	
	private void nacrtajPjesackePrijelaze(GKS gks) {
		gks.postaviBoju(Color.WHITE, 1);
		mat.identitet();
		gks.trans(mat);
		gks.postaviStroke(new BasicStroke((int)(0.2 * max), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{(float)max / 22.75f}, 0));
		for (int i=0; i<4; i++) {
			mat.rotiraj(90);
			gks.trans(mat);
			gks.nacrtajLiniju(0.4 * min + max / 20, 0.5 * max, 0.4 * max + min / 20, 0.5 * max);
		}
		gks.postaviStroke(new BasicStroke());
	}
	
	private void nacrtajTravnatuPovrsinu(GKS gks) {
		gks.postaviBoju(new Color(0x5CE62E), 1);
		for (int i=0; i<4; i++) {
			mat.identitet();
			mat.rotiraj(i * 90);
			mat.pomakni(0.8 * min, 0.8 * max);
			gks.trans(mat);
			gks.nacrtajPopunjeniPravokutnik(0.07 * velicina, 0.07 * velicina);
		}
	}
	
	public void nacrtajBrojVozilaIzSvakogSmjera(GKS gks, List<Auto> auti) {
		gks.postaviFont(new Font("Dialog", Font.PLAIN, 45));
		for (int i=0; i<4; i++) {
			mat.identitet();
			mat.rotiraj(i * 90);
			mat.pomakni((0.45 + i/2 * 0.12) * max -1, (0.8 + (i == 0 || i == 3 ? 0.1 : 0)) * min -1);
			gks.trans(mat);
			gks.postaviBoju(Color.BLACK, 1);
			
			final int rotacija = i * 90;
			Long count = auti.stream()
				.filter(auto -> auto.jeLiPrijeSemafora(rotacija))
				.count();
			
			gks.nacrtajTekst(count.toString());
			
			mat.pomakni(0, 2);
			gks.trans(mat);
			gks.nacrtajTekst(count.toString());
			
			mat.pomakni(2, 0);
			gks.trans(mat);
			gks.nacrtajTekst(count.toString());
			
			mat.pomakni(0, -2);
			gks.trans(mat);
			gks.nacrtajTekst(count.toString());
			
			mat.pomakni(-1, 1);
			gks.trans(mat);
			gks.postaviBoju(Color.WHITE, 0.95f);
			gks.nacrtajTekst(count.toString());
		}
		gks.postaviFont(new Font("Dialog", Font.PLAIN, 10));
	}
	
	public void nacrtajSemafore(GKS gks) {
		for (int i =- 1; i < semafori.size() - 1; i ++) {
			if (semafori.get(i+1).getZelenoStop() > 0) {
				mat.identitet();
				mat.rotiraj(i % 4 * 90);
				mat.pomakni(0.3 * max, 0.24 * min);
				if (semafori.get(i+1).isLijevi()) {
					mat.pomakni(0.2 * min, 0.06 * min);
					mat.rotiraj(90);
				}
				gks.trans(mat);
				semafori.get(i+1).nacrtajSemafor(gks, mat);
			}
		}
	}
	
	public void setSemafori(List<Semafor> semafori) {
		this.semafori = semafori;
	}
}
