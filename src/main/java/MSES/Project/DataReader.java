package MSES.Project;

import Jama.Matrix;

/**
 * Parent class for all readers
 */
public abstract class DataReader {
	/**
	 * Collecting all data into nf and nm vectors
	 * @param yearSince year from
	 * @param yearTo year to
	 * @param nf female vector
	 * @param nm male vector
	 */
	abstract void getAllData(int yearSince, int yearTo, int maxAge, Matrix nf[], Matrix nm[]);
	
	abstract public void close();
}
