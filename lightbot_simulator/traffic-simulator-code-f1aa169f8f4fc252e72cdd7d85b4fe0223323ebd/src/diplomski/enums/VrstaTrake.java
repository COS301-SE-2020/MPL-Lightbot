package diplomski.enums;

public enum VrstaTrake {
	 LIJEVO_GOREDESNO(1), 
	 LIJEVOGORE_DESNO(2), 
	 LIJEVOGORE_GOREDESNO(3);
	 
	 private int vrstaTrake;
	 
	 private VrstaTrake(int v) {
		 vrstaTrake = v;
	 }
	 
	 public int getVrstaTrake() {
	   return vrstaTrake;
	 }
}
