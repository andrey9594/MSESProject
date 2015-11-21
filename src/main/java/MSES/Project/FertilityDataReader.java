package MSES.Project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Jama.Matrix;

public class FertilityDataReader extends DataReader {
	private static final Logger log = LoggerFactory.getLogger(FertilityDataReader.class);
	
	private final Scanner scanner;
	
	public FertilityDataReader(String fileName) throws IOException {
		Path path = FileSystems.getDefault().getPath("resources", fileName);
		scanner = new Scanner(Files.newInputStream(path));
		scanner.nextLine(); // it's the unused service head information
	}

	@Override
	void getAllData(int yearSince, int yearTo, int maxAge, Matrix[] fFertility, Matrix[] empty) {
		FertilityData fertilityData = null;
		while ((fertilityData = getNext()) != null) {
			int currentYear = fertilityData.getYear();
			if (currentYear >= yearSince && currentYear <= yearTo) {
				int currentAge = fertilityData.getAge();
				if (fFertility[currentYear] == null) {
					fFertility[currentYear] = new Matrix(maxAge + 1, 1); // 0, 1, ..., MAX_AGE <=> n + 1 values
				} 
				if (currentAge <= maxAge) {
					fFertility[currentYear].set(currentAge, 0, fertilityData.getASFR());
				}
			}
		}		
	}
	
	private FertilityData getNext() {
		try {
			int year = Integer.parseInt(scanner.next().substring(0, 4)); // some year with the plus symbol at the end
			/**
			 * we have some problem with the year 110+
			 * because the plus at the end hinder our
			 * parsing process
			 */
			int age = -1;
			String nextAge = scanner.next();
			if (nextAge.length() == 3)
				age = Integer.parseInt(nextAge.substring(0, 2));
			else
				age = Integer.parseInt(nextAge);
			double ASFR = Double.parseDouble(scanner.next());
			return new FertilityData(year, age, ASFR);
		} catch(Exception e) {
			log.warn("Can't parse an information from the file, have we reached the end of the file?");
			return null;
		}
	}
	
	@Override
	public void close() {
		if (scanner != null) {
			scanner.close();
			log.info("Scanner has successfully closed");
		}
	}
}
