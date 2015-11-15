package MSES.Project;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

/**
 * The Main method to launch our program.
 */
public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private static final String POPULATION_FILE_NAME = "Population.txt";
	private static final String DEATH_RATES_FILE_NAME = "Death_rates.txt";
	private static final String FERTILITY_FILE_NAME = "Fertility.txt";
	
	/**
	 * Consider ages groups from 0 years to MAX_AGE years old
	 */
	private static final int MAX_AGE = 105;
	
	/**
	 * The part of girl-child birthing
	 */
	private static final double DELTA = 0.475;

	/**
	 * In our archive we have data for years from YEAR_SINCE to YEAR_TO
	 */
	private static final int YEAR_SINCE = 1900;
	private static final int YEAR_TO = 2010;
	private static final int YEARS_COUNT = YEAR_TO - YEAR_SINCE + 1;
	
	/**
	 * We will predict population from YEAR_PREDICT_SINCE to YEAR_PREDICT_TO
	 */
	private static final int YEAR_PREDICT_SINCE = 2000;
	private static final int YEAR_PREDICT_TO = 2010;
	
	private static int getTotalPopulation(Matrix nf, Matrix nm) {
		double sum = 0;
		for (int i = 0; i <= MAX_AGE; i++) {
			sum += nf.get(i, 0);
			sum += nm.get(i, 0);
		}
		return (int)sum;
	}

	public static void main(String[] args) {
		/** nf is the ages distribution vector for female */
		Matrix nf[] = new Matrix[YEARS_COUNT];
		/** nm is the ages distribution vector for male */
		Matrix nm[] = new Matrix[YEARS_COUNT];
		AgeDataReader ageDataReader = null;
		try {
			ageDataReader = new AgeDataReader(POPULATION_FILE_NAME);
			ageDataReader.getAllData(YEAR_SINCE, YEAR_TO, MAX_AGE, nf, nm);
		} catch (IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (ageDataReader != null)
				ageDataReader.close();
		}

		/**
		 * deathRates is the ages distribution vector of death for each age
		 */
		Matrix fqx[] = new Matrix[YEARS_COUNT];
		Matrix mqx[] = new Matrix[YEARS_COUNT];
		DeathDataReader deathDataReader = null;
		try {
			deathDataReader = new DeathDataReader(DEATH_RATES_FILE_NAME);
			deathDataReader.getAllData(YEAR_SINCE, YEAR_TO, MAX_AGE, fqx, mqx);
		} catch(IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (deathDataReader != null)
				deathDataReader.close();
		}
				
		/** fFertality is the ages distribution vector of fertility for each age for female*/
		Matrix fFertility[] = new Matrix[YEARS_COUNT];
		FertilityDataReader fertilityDataReader = null;
		try {
			fertilityDataReader = new FertilityDataReader(FERTILITY_FILE_NAME);
			fertilityDataReader.getAllData(YEAR_SINCE, YEAR_TO, MAX_AGE, fFertility, null);
		} catch(IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (fertilityDataReader != null)
				fertilityDataReader.close();
		}
		
		for (int i = 0; i < MAX_AGE; i++)
			System.out.println("2007: " + i + " age = " + fFertility[2007 - YEAR_SINCE].get(i, 0));
			
		
		// constant Lesli matrix
//		Matrix Lf = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
//		Matrix Lm = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
//		for (int j = 0; j < MAX_AGE; j++) {
//			/**
//			 * fFertility[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j, 0) <-> mx
//			 * fqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(0, 0) = child mortality fro female
//			 */
//			double fF = (DELTA / 2.) * (fFertility[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j, 0)
//										+ (1 - fqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j, 0)) * fFertility[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j + 1, 0)
//										) * (1 - 0.006); 
//			Lf.set(0, j, fF);
//			double mF = ((1 - DELTA) / 2.) * (fFertility[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j, 0)
//					+ (1 - fqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j, 0)) * fFertility[YEAR_PREDICT_SINCE - YEAR_SINCE].get(j + 1, 0)
//					) * (1 - 0.006); 
//			Lm.set(0, j, mF); 
//		}
//		for (int i = 1; i <= MAX_AGE; i++) {
//			Lf.set(i, i - 1, 1 - fqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(i - 1, 0));
//			Lm.set(i, i - 1, 1 - mqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(i - 1, 0));
//		}
//		
////		Lf.print(3, 10);
//		
//		Matrix nfCurrent = nf[YEAR_PREDICT_SINCE - YEAR_SINCE].copy();
//		Matrix nmCurrent = nm[YEAR_PREDICT_SINCE - YEAR_SINCE].copy();
//		for (int currentYear = YEAR_PREDICT_SINCE; currentYear < YEAR_PREDICT_TO; currentYear++) {
//			nfCurrent = Lf.times(nfCurrent);
//			nmCurrent = Lm.times(nmCurrent);
//			System.out.println(currentYear
//					+ ": actual = " + getTotalPopulation(nf[currentYear - YEAR_SINCE], nm[currentYear - YEAR_SINCE]) 
//					+ ", predict = " + getTotalPopulation(nfCurrent, nmCurrent));
//		}
		
//		for (int year = 2000; year <= 2010; year++)
//			System.out.println(year + ": " + getTotalPopulation(nf[year - YEAR_SINCE], nm[year - YEAR_SINCE]));
	}
}
