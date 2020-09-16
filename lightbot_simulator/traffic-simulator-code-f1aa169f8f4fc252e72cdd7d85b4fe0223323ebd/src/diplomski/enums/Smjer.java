package diplomski.enums;

public enum Smjer {
	DESNO(-1), 
	RAVNO(0), 
	LIJEVO(1);
	 
	private int smjer;
	 
	private Smjer(int s) {
		smjer = s;
	}
	 
	public int getSmjer() {
		return smjer;
	}
}
