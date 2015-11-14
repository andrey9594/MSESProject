package MSES.Project;

import Jama.Matrix;

/**
 * The Main method to launch our program.
 *
 */
public class Main {
	/**
	 * Consider ages groups from 0 years to
	 * @param MAX_AGES
	 * years old
	 */
	private static final int MAX_AGES = 105;

	/**
	 * In our archive we have data for years from
	 * @param YEAR_SINCE
	 * to
	 * @param YEAR_TO
	 */
	private static final int YEAR_SINCE = 1900;
	private static final int YEAR_TO = 2010;
	private static final int YEARS_COUNT = YEAR_TO - YEAR_SINCE + 1;

	public static void main(String[] args) {
		/**
		 * n is the ages distribution vector
		 */
		Matrix n[] = new Matrix[YEARS_COUNT];
		for (int i = 0; i < YEARS_COUNT; i++) {
			
		}

	}
}
