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
	
	/**
	 * Consider ages groups from 0 years to MAX_AGE years old
	 */
	private static final int MAX_AGE = 105;

	/**
	 * In our archive we have data for years from YEAR_SINCE to YEAR_TO
	 */
	private static final int YEAR_SINCE = 1900;
	private static final int YEAR_TO = 2010;
	private static final int YEARS_COUNT = YEAR_TO - YEAR_SINCE + 1;
	
	/**
	 * We will predict population from YEAR_PREDICT_SINCE to YEAR_PREDICT_TO
	 */
	private static final int YEAR_PREDICT_SINCE = 2003;
	private static final int YEAR_PREDICT_TO = 2010;
	private static final int YEAR_PREDICT_COUNT = YEAR_PREDICT_SINCE - YEAR_PREDICT_TO + 1;
	
	private static final String POPULATION_FILE_NAME = "Population.txt";
	private static final String DEATH_RATES_FILE_NAME = "Death_rates.txt";
	private static final String FERTILITY_FILE_NAME = "Fertility.txt";
	
	private static int getTotalPopulation(int year, Matrix nf[], Matrix nm[]) {
		double sum = 0;
		for (int i = 0; i <= MAX_AGE; i++) {
			sum += nf[year - YEAR_SINCE].get(i, 0);
			sum += nm[year - YEAR_SINCE].get(i, 0);
		}
		return (int)sum;
	}

	public static void main(String[] args) {
		/**
		 * nf is the ages distribution vector
		 * for female
		 */
		Matrix nf[] = new Matrix[YEARS_COUNT];
		/**
		 * nm is the ages distribution vector
		 * for male
		 */
		Matrix nm[] = new Matrix[YEARS_COUNT];
		AgeDataReader ageDataReader = null;
		try {
			ageDataReader = new AgeDataReader(POPULATION_FILE_NAME);
			PopulationData currentPopulationData = null;
			while ((currentPopulationData = ageDataReader.getNext()) != null) {
				int currentYear = currentPopulationData.getYear();
				if (currentYear >= YEAR_SINCE && currentYear <= YEAR_TO) {
					int currentAge = currentPopulationData.getAge();
					if (currentAge == 0) {
						nf[currentYear - YEAR_SINCE] = new Matrix(MAX_AGE + 1, 1); // 0, 1, ..., MAX_AGE <=> n + 1 values
						nm[currentYear - YEAR_SINCE] = new Matrix(MAX_AGE + 1, 1); // -//-
					} 
					if (currentAge <= MAX_AGE) {
						nf[currentYear - YEAR_SINCE].set(currentAge, 0, currentPopulationData.getFemale());
						nm[currentYear - YEAR_SINCE].set(currentAge, 0, currentPopulationData.getMale());
					}
				}
			}
		} catch (IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (ageDataReader != null)
				ageDataReader.close();
		}

		/**
		 * deathRates is the ages distribution vector
		 * of death for each age
		 */
		Matrix fqx[] = new Matrix[YEARS_COUNT];
		Matrix mqx[] = new Matrix[YEARS_COUNT];
		DeathDataReader deathDataReader = null;
		try {
			deathDataReader = new DeathDataReader(DEATH_RATES_FILE_NAME);
			DeathData deathData = null;
			while ((deathData = deathDataReader.getNext()) != null) {
				int currentYear = deathData.getYear();
				if (currentYear >= YEAR_SINCE && currentYear <= YEAR_TO) {
					int currentAge = deathData.getAge();
					if (currentAge == 0) {
						fqx[currentYear - YEAR_SINCE] = new Matrix(MAX_AGE + 1, 1); // 0, 1, ..., MAX_AGE <=> n + 1 values
						mqx[currentYear - YEAR_SINCE] = new Matrix(MAX_AGE + 1, 1); // -//-
					} 
					if (currentAge <= MAX_AGE) {
						fqx[currentYear - YEAR_SINCE].set(currentAge, 0, deathData.getFqx());
						mqx[currentYear - YEAR_SINCE].set(currentAge, 0, deathData.getMqx());
					}
				}
			}
		} catch(IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (deathDataReader != null)
				deathDataReader.close();
		}
		
		/**
		 * fFertality is the ages distribution vector
		 * of fertility for each age
		 * for female
		 */
		Matrix fFertility[] = new Matrix[YEARS_COUNT];
		FertilityDataReader fertilityDataReader = null;
		try {
			fertilityDataReader = new FertilityDataReader(FERTILITY_FILE_NAME);
			FertilityData fertilityData = null;
			while ((fertilityData = fertilityDataReader.getNext()) != null) {
				int currentYear = fertilityData.getYear();
				if (currentYear >= YEAR_SINCE && currentYear <= YEAR_TO) {
					int currentAge = fertilityData.getAge();
					if (fFertility[currentYear - YEAR_SINCE] == null) {
						fFertility[currentYear - YEAR_SINCE] = new Matrix(MAX_AGE + 1, 1); // 0, 1, ..., MAX_AGE <=> n + 1 values
					} 
					if (currentAge <= MAX_AGE) {
						fFertility[currentYear - YEAR_SINCE].set(currentAge, 0, fertilityData.getASFR());
					}
				}
			}
		} catch(IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (fertilityDataReader != null)
				fertilityDataReader.close();
		}
		
		// constant Lesli matrix
		Matrix Lf = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
		Matrix Lm = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
		for (int j = 0; j <= MAX_AGE; j++) {
			//Lf.set(0, j, !!!); // 
			//Lm.set(0, j, !!!); //
		}
		for (int i = 1; i <= MAX_AGE; i++) {
			Lf.set(i, i - 1, 1 - fqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(i - 1, 0));
			Lm.set(i, i - 1, 1 - mqx[YEAR_PREDICT_SINCE - YEAR_SINCE].get(i - 1, 0));
		}
		
		Matrix nfCurrent = nf[YEAR_PREDICT_SINCE - YEAR_SINCE].copy();
		Matrix nmCurrent = nm[YEAR_PREDICT_SINCE - YEAR_SINCE].copy();
		for (int currentYear = YEAR_PREDICT_SINCE; currentYear < YEAR_PREDICT_TO; currentYear++) {
			
		}
		
//		for (int year = 2000; year <= 2010; year++)
//			System.out.println(year + ": " + getTotalPopulation(year, nf, nm));
	}
}
