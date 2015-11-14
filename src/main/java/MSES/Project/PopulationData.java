package MSES.Project;

/**
 * Class for collection data about population from data files
 */
public class PopulationData extends Data {
	private int female;
	private int male;
	
	/**
	 * Main constructor
	 * @param year year
	 * @param age age
	 * @param female Total female
	 * @param male Total male
	 */
	public PopulationData(int year, int age, int female, int male) {
		super(year, age);
		this.female = female;
		this.male = male;
	}
	
	/**
	 * get
	 * @return female
	 */
	public int getFemale() {
		return female;
	}
	
	/**
	 * set
	 * @param female set the female new value
	 */
	public void setFemale(int female) {
		this.female = female;
	}
	
	/**
	 * get
	 * @return male
	 */
	public int getMale() {
		return male;
	}
	
	/**
	 * set
	 * @param male set the male new value
	 */
	public void setMale(int male) {
		this.male = male;
	}

}
