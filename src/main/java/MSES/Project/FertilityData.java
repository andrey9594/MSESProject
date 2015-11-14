package MSES.Project;

public class FertilityData extends Data {
	/**
	 * ASFR 
	 */
	private double ASFR;
	
	public FertilityData(int year, int age, double ASFR) {
		super(year, age);
		this.ASFR = ASFR;
	}
	
	/**
	 * get
	 * @return ASFR
	 */
	public double getASFR() {
		return ASFR;
	}
	
	/**
	 * set
	 * @param ASFR set the new ASFR value
	 */
	public void setASFR(double ASFR) {
		this.ASFR = ASFR;
	}
}
