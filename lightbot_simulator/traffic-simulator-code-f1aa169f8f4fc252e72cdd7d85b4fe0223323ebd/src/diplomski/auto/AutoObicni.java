package diplomski.auto;

import java.awt.Color;
import java.util.List;

import diplomski.GKS;
import diplomski.MT2D;
import diplomski.Semafor;

public class AutoObicni extends Auto {
	
	public AutoObicni(int max, int rotacija, int simulationSpeed,
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
		gks.nacrtajPopunjeniPravokutnik(1.3 * velicina, 2 * velicina);
		
		//prozori
		gks.postaviBoju(new Color(0xE0E0D1), 1);
		mat.pomakni(0, 0.8 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(velicina, 0.5 * velicina);
		gks.postaviBoju(new Color(0x080808), 1);
		mat.pomakni(0, -1.8 * velicina);
		gks.trans(mat);
		gks.nacrtajPravokutnik(0.9 * velicina, 0.3 * velicina);
		
		//gume
		mat.pomakni(1.3 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(velicina / 5, 0.65 * velicina);
		mat.pomakni(-2.6 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(velicina / 5, 0.65 * velicina);
		mat.pomakni(0, 2 * velicina);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(velicina / 5, 0.65 * velicina);
		mat.pomakni(2.6 * velicina, 0);
		gks.trans(mat);
		gks.nacrtajPopunjeniPravokutnik(velicina / 5, 0.65 * velicina);
		
		//žmigavci
		if (smjer != 0) {//-0.1, -1.6
			if (zmigavac >= 0 && zmigavac < 30) {
				gks.postaviBoju(new Color(0xFF5930), 1);
				if (smjer == 1) {
					mat.pomakni(-2.6 * velicina, -2.7 * velicina);
				}
				else {
					mat.pomakni(-0.2 * velicina, -2.7 * velicina);
				}
				gks.trans(mat);
				gks.nacrtajPopunjenuElipsu(velicina / 4, velicina / 4);
				mat.pomakni(0, 3.3 * velicina);
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
