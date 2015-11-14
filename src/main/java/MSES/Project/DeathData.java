package MSES.Project;

public class DeathData extends Data {
	/**
	 * Probability of death between ages x and x+n
	 * for female
	 */
	private double fqx;
	
	/**
	 * Probability of death between ages x and x+n
	 * for male
	 */
	private double mqx;

	public DeathData(int year, int age, double fqx, double mqx) {
		super(year, age);
		this.fqx = fqx;
		this.mqx = mqx;
	}
	
	/**
	 * get
	 * @return fqx
	 */
	public double getFqx() {
		return fqx;
	}
	
	/**
	 * set
	 * @param fqx set the new fqx value
	 */
	public void setFqx(double fqx) {
		this.fqx = fqx;
	}
	
	/**
	 * get
	 * @return mqx
	 */
	public double getMqx() {
		return mqx;
	}
	
	/**
	 * set
	 * @param mqx set the new mqx value
	 */
	public void setMqx(double mqx) {
		this.mqx = mqx;
	}
}
