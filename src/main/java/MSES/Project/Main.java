package MSES.Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

/**
 * The Main method to launch our program.
 */
public class Main {
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	private static PrintWriter debug = null;
	
	private static final String POPULATION_FILE_NAME = "Population.txt";
	private static final String DEATH_RATES_FILE_NAME = "Death_rates.txt";
	private static final String FERTILITY_FILE_NAME = "Fertility.txt";
	private static final String CHILD_BIRTHS_DEATH = "Child_births_death.txt";
	
	/** Consider ages groups from 0 years to MAX_AGE years old */
	private static final int MAX_AGE = 105;
	
	/** The part of girl-child births */
	private static final double DELTA = 0.475;
	
	/** year -> child births deaths */
	private static Map <Integer, Double> childBirthsDeath;

	/** In our archive we have data for years from YEAR_SINCE to YEAR_TO */
	private static final int YEAR_SINCE = 1900;
	private static final int YEAR_TO = 2010;
	
	/** We will predict population from YEAR_PREDICT_SINCE to YEAR_PREDICT_TO */
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
	
	private static double getF(boolean female, Matrix [] fFertility, Matrix [] fqx, int yearPredict, int age) {
		double F = (fFertility[yearPredict].get(age, 0) + (1 - fqx[yearPredict].get(age, 0)) * fFertility[yearPredict].get(age + 1, 0))
				* (1 - childBirthsDeath.get(yearPredict));
		if (female)
			F *= DELTA / 2.;
		else
			F *= (1 - DELTA) / 2.;
		return F; 
	}
	
	
	private static double predictFertilityForAgeLMA(Matrix [] fFertility, int age, int year) {
		final int yearSince = 1990;
		int yearCount = year - yearSince + 1;
		double y[] = new double[yearCount];
		//debug.println(age);
		for (int i = 0; i < yearCount; i++) {
			y[i] = fFertility[yearSince + i].get(age, 0);
//			debug.print(y[i] + " ");
		}		
		
		int n = 5;
		int l = year - (yearSince + n - 1) + 1;
		Matrix F = new Matrix(l, n);
		for (int j = 0; j < n; j++) {
			int currentYear = year - j;
			for (int i = 0; i < l; i++) {
				F.set(i, j, fFertility[currentYear--].get(age, 0));
				//debug.print(F.get(i, j) + " ");
			}
			//debug.println();
		}
		
		Matrix Y = new Matrix(l, 1);
		for (int i = 0; i < l; i++)
			Y.set(i, 0, fFertility[year - i].get(age, 0));
		
		Matrix W = (F.transpose().times(F)).inverse().times(F.transpose()).times(Y);
		
		double newY = 0;
		for (int i = 0; i < n; i++)
			newY += W.get(i, 0) * fFertility[year - i - 1].get(age, 0);
		debug.print("age = " + age + ": last 3 = " + fFertility[year - 3].get(age, 0) + ", " +
			 fFertility[year - 2].get(age, 0) + ", " + + fFertility[year - 1].get(age, 0) + ", ");
		debug.printf("new y = %f", newY);// y_{t+1}
		debug.print(", real y = " + fFertility[year].get(age, 0));// y_{t+1}
		debug.printf(", error = %f\n", Math.abs(newY - fFertility[year].get(age, 0)));
		return newY;
	}
	
	private static Matrix getNewFertilityLMA(Matrix [] fFertility, int year) {
		Matrix newFertility = fFertility[year].copy();
		for (int currentAge = 16; currentAge <= Math.min(MAX_AGE, 50); currentAge++)
			newFertility.set(currentAge, 0, predictFertilityForAgeLMA(fFertility, currentAge, year));
		return newFertility;
	}
	
//	private static Matrix getNewFertilityESS(Matrix [] fFertility, int year) {
//		Matrix newFertility = fFertility[year].copy();
//		for (int currentAge = 0; currentAge <= MAX_AGE; currentAge++)
//			newFertility.set(currentAge, 0, predictFertilityForAge(newFertility, currentAge));
//		return newFertility;
//	}

	public static void main(String[] args) {
		try {
			debug = new PrintWriter(new File("debug.out"));
		} catch (FileNotFoundException e) {
			log.error("Can't create debug file!", e);
			e.printStackTrace();
			return;
		}
		
		Matrix nf[] = new Matrix[YEAR_TO + 1]; // the ages distribution vector for female
		Matrix nm[] = new Matrix[YEAR_TO + 1]; // the ages distribution vector for male
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

		/** deathRates is the ages distribution vector of death for each age */
		Matrix fqx[] = new Matrix[YEAR_TO + 1];
		Matrix mqx[] = new Matrix[YEAR_TO + 1];
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
				
		/** fFertality is the ages distribution vector of fertility for each age for female */
		Matrix fFertility[] = new Matrix[YEAR_TO + 1];
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
		
		/** Child births death; Map: year -> death value */
		ChildBirthsDeathReader childBirthsDeathReader = null;
		try {
			childBirthsDeathReader = new ChildBirthsDeathReader(CHILD_BIRTHS_DEATH);
			childBirthsDeath = childBirthsDeathReader.getAllData();
		} catch(IOException e) {
			log.error("Can't read from the file!", e);
			e.printStackTrace();
			return;
		} finally {
			if (childBirthsDeathReader != null)
				childBirthsDeathReader.close();
		}
		
		/** initial Lesli matrix */
		Matrix Lf = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
		Matrix Lm = new Matrix(MAX_AGE + 1, MAX_AGE + 1);
		for (int j = 0; j < MAX_AGE; j++) {
			double fF = getF(true, fFertility, fqx, YEAR_PREDICT_SINCE, j);
			Lf.set(0, j, fF);
			double mF = getF(false, fFertility, fqx, YEAR_PREDICT_SINCE, j);
			Lm.set(0, j, mF);
		}
		for (int i = 1; i <= MAX_AGE; i++) {
			Lf.set(i, i - 1, 1 - fqx[YEAR_PREDICT_SINCE].get(i - 1, 0));
			Lm.set(i, i - 1, 1 - mqx[YEAR_PREDICT_SINCE].get(i - 1, 0));
		}

		Matrix nfCurrent = nf[YEAR_PREDICT_SINCE].copy();
		Matrix nmCurrent = nm[YEAR_PREDICT_SINCE].copy();
		// const
		System.out.println("***\n Const Matrix \n***");
		for (int currentYear = YEAR_PREDICT_SINCE; currentYear < YEAR_PREDICT_TO; currentYear++) {
			nfCurrent = Lf.times(nfCurrent);
			nmCurrent = Lm.times(nmCurrent);
			String actualString = Integer.toString(getTotalPopulation(nf[currentYear], nm[currentYear]));
			actualString = actualString.substring(0, 2) + "." + actualString.substring(2, 5) + "."
					+ actualString.substring(5);
			String predictString = Integer.toString(getTotalPopulation(nfCurrent, nmCurrent));
			predictString = predictString.substring(0, 2) + "." + predictString.substring(2, 5) + "."
					+ predictString.substring(5);
			System.out.println(currentYear + ": actual = " + actualString + ", predict = " + predictString);
		}

		// predict
		// Matrix nfCurrent = nf[YEAR_PREDICT_SINCE].copy();
		//		Matrix nmCurrent = nm[YEAR_PREDICT_SINCE].copy();
		System.out.println("***\n Not const Matrix \n***");
		nfCurrent = nf[YEAR_PREDICT_SINCE].copy();
		nmCurrent = nm[YEAR_PREDICT_SINCE].copy();
		for (int currentYear = YEAR_PREDICT_SINCE; currentYear < YEAR_PREDICT_TO; currentYear++) {
			for (int i = 1; i <= MAX_AGE; i++) {
				Lf.set(i, i - 1, 1 - fqx[currentYear].get(i - 1, 0));
				Lm.set(i, i - 1, 1 - mqx[currentYear].get(i - 1, 0));
			}
			fFertility[currentYear] = getNewFertilityLMA(fFertility, currentYear);
			for (int j = 0; j < MAX_AGE; j++) {
				double fF = getF(true, fFertility, fqx, currentYear, j);
				Lf.set(0, j, fF);
				double mF = getF(false, fFertility, fqx, currentYear, j);
				Lm.set(0, j, mF);
			}
			nfCurrent = Lf.times(nfCurrent);
			nmCurrent = Lm.times(nmCurrent);
			String actualString = Integer.toString(getTotalPopulation(nf[currentYear], nm[currentYear]));
			actualString = actualString.substring(0, 2) + "." + actualString.substring(2, 5) + "."
					+ actualString.substring(5);
			String predictString = Integer.toString(getTotalPopulation(nfCurrent, nmCurrent));
			predictString = predictString.substring(0, 2) + "." + predictString.substring(2, 5) + "."
					+ predictString.substring(5);
			System.out.println(currentYear + ": actual = " + actualString + ", predict = " + predictString);
		}
		debug.flush();
		debug.close();
	}
}
