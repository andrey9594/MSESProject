package MSES.Project;

/**
 * Parent class for all readers
 */
public abstract class DataReader {
	/**
	 * 
	 * @return Next object of the Data class (or it's children) 
	 */
	public Data getNext() {
		return null;
	}
	
	abstract public void close();
}
