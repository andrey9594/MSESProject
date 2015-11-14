package MSES.Project;

/**
 * Parent class for all readers
 */
public abstract class DataReader {
	/**
	 * 
	 * @return Next object of the Data class (or it's childs) 
	 */
	public Data getNext() {
		return null;
	}
}
