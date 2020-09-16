package diplomski.auto;

import java.awt.Color;
import java.util.List;

import diplomski.GKS;
import diplomski.MT2D;
import diplomski.Semafor;

public class AutoKombi extends Auto {
	
	public AutoKombi(int max, int rotacija, int simulationSpeed,
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
		gks.nacrtajTrupAuta(1.3 * velicina, 2.2 * velicina, 0.8, 0.8, 1.2, 1.2);
		
		//krov
		gks.postaviBoju(new Color(0x080808), 1);
		mat.pomakni(0, 0.2 * velicina);
		gks.trans(mat);
		gks.nacrtajTrupAuta(0.6 * velicina, 0.2 * velicina, 0.7, 0.7, 1.3, 1.3);
		
		//svjetla
		gks.postaviBoju(new Color(0xCCCCB2), 1);
		mat.pomakni(-0.8 * velicina, 2.1 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.2 * velicina, 0.2 * velicina, 1, 1, 1, 0.5);
		mat.pomakni(1.6 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.2 * velicina, 0.2 * velicina, 1, 1, 0.5, 1);
		
		//stakla
		gks.postaviBoju(new Color(0x080808), 1);
		mat.pomakni(0.15 * velicina, -2.2 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.15 * velicina, 0.45 * velicina, 1, 1, 1, 1.3);
		mat.pomakni(0, -1.4 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.15 * velicina, 0.45 * velicina, 1, 1, 1, 1.3);
		mat.pomakni(-1.9 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.15 * velicina, 0.45 * velicina, 1, 1, 1.3, 1);
		mat.pomakni(0, 1.4 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.15 * velicina, 0.45 * velicina, 1, 1, 1.3, 1);
		mat.pomakni(0.95 * velicina, 0.8 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.8 * velicina, 0.2 * velicina, 1.4, 1, 1, 1);
		gks.postaviBoju(new Color(0x191919), 1);
		mat.pomakni(0, -3 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniTrapez(0.8 * velicina, 0.2 * velicina, 1, 1.2, 1, 1);
		mat.pomakni(-0.6 * velicina, 1.1 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.1 * velicina, 0.75 * velicina);
		mat.pomakni(0.4 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.1 * velicina, 0.75 * velicina);
		mat.pomakni(0.4 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.1 * velicina, 0.75 * velicina);
		mat.pomakni(0.4 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(0.1 * velicina, 0.75 * velicina);
		
		//žmigavci
		if (smjer != 0) {
			if (zmigavac >= 0 && zmigavac < 30) {
				gks.postaviBoju(new Color(0xFF5930), 1);
				if (smjer == 1) {
					mat.pomakni(-1.8 * velicina, -1 * velicina);
				}
				else {
					mat.pomakni(0.6 * velicina, -1 * velicina);
				}
				gks.trans(mat);
				gks.nacrtajPopunjenuElipsu(velicina / 4, velicina / 4);
				mat.pomakni(0, 3.9 * velicina);
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
