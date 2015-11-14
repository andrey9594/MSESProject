package MSES.Project;

/**
 * Class for collection data about population from data files
 */
public class PopulationData extends Data {
	private double female;
	private double male;
	
	/**
	 * Main constructor
	 * @param year year
	 * @param age age
	 * @param female Total female
	 * @param male Total male
	 */
	public PopulationData(int year, int age, double female, double male) {
		super(year, age);
		this.female = female;
		this.male = male;
	}
	
	/**
	 * get
	 * @return female
	 */
	public double getFemale() {
		return female;
	}
	
	/**
	 * set
	 * @param female set the female new value
	 */
	public void setFemale(double female) {
		this.female = female;
	}
	
	/**
	 * get
	 * @return male
	 */
	public double getMale() {
		return male;
	}
	
	/**
	 * set
	 * @param male set the male new value
	 */
	public void setMale(double male) {
		this.male = male;
	}

}
