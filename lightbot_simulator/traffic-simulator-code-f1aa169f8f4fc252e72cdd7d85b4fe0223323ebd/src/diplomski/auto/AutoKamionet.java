package diplomski.auto;

import java.awt.Color;
import java.util.List;

import diplomski.GKS;
import diplomski.MT2D;
import diplomski.Semafor;

public class AutoKamionet extends Auto {
	
	public AutoKamionet(int max, int rotacija, int simulationSpeed,
			Semafor semafor, List<Auto> auti, long redniBroj, int smjer) {
		super(max, rotacija, simulationSpeed, semafor, auti, redniBroj, smjer);
	}

	public void nacrtajAuto(GKS gks, MT2D mat) {
		//trup
		gks.postaviBoju(boja, 1);
		mat.identitet();
		mat.rotiraj(rotacija);
		mat.pomakni(x, y);
		mat.rotiraj(rotacijaSmjer);
		gks.trans(mat);
		gks.nacrtajTrupAuta(1.3 * velicina, 2.1 * velicina, 0.9, 1, 1.3, 1);
		
		//svjetla
		gks.postaviBoju(new Color(0x080808), 1);
		mat.pomakni(-0.75 * velicina, 2.5 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.3 * velicina, 0.15 * velicina, 0.6, 1.2, 1, 1);
		mat.pomakni(1.5 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.3 * velicina, 0.15 * velicina, 0.6, 1.2, 1, 1);
		
		//stakla
		mat.pomakni(0.26 * velicina, -1.8 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.12 * velicina, 0.5 * velicina, 1, 1, 1, 1.3);
		mat.pomakni(-2 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.12 * velicina, 0.5 * velicina, 1, 1, 1.3, 1);
		mat.pomakni(1 * velicina, 0.7 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.8 * velicina, 0.2 * velicina, 1.4, 1, 1, 1);
		
		//prtljažnik
		gks.postaviBoju(new Color(0x2A2A2A), 1);
		mat.pomakni(0, -2.4 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(1.1 * velicina, 0.9 * velicina);
		
		//žmigavci
		if (smjer != 0) {
			if (zmigavac >= 0 && zmigavac < 30) {
				gks.postaviBoju(new Color(0xFF5930), 1);
				if (smjer == 1) {
					mat.pomakni(-1.2 * velicina, -0.6 * velicina);
				}
				else {
					mat.pomakni(1.2 * velicina, -0.6 * velicina);
				}
				gks.trans(mat);
				gks.nacrtajPopunjenuElipsu(velicina / 4, velicina / 4);
				mat.pomakni(0, 3.65 * velicina);
				gks.trans(mat);
				gks.nacrtajPopunjenuElipsu(velicina / 4, velicina / 4);
			}
			if (zmigavac >= 0) {
				zmigavac += simulationSpeed;
				if (zmigavac >= 60) {
					zmigavac = 0;
				}
			}
		}
	}
}
