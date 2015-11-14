package MSES.Project;

/**
 * Class for collection all data from data files
 */
public abstract class Data {
	private int year;
	private int age;
	
	/**
	 * Main constructor
	 * @param year year
	 * @param age age
	 */
	public Data(int year, int age) {
		this.year = year;
		this.age = age;
	}
	
	/**
	 * get
	 * @return year
	 */
	public int getYear() {
		return year;
	}
	
	/**
	 * set
	 * @param year set the year new value
	 */
	public void setYear(int year) {
		this.year = year;
	}
	
	/**
	 * get
	 * @return age
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * set
	 * @param age set the age new value
	 */
	public void setAge(int age) {
		this.age = age;
	}
}
