package MSES.Project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathDataReader extends DataReader {
	private static final Logger log = LoggerFactory.getLogger(DataReader.class);

	private final Scanner scanner;

	public DeathDataReader(String fileName) throws IOException {
		Path path = FileSystems.getDefault().getPath("resources", fileName);
		scanner = new Scanner(Files.newInputStream(path));
		scanner.nextLine(); // it's the unused service head information
	}
	
	@Override
	public DeathData getNext() {
		try {
			int year = Integer.parseInt(scanner.next().substring(0, 4)); // some year with the plus symbol at the end
			/**
			 * we have some problem with the year 110+
			 * because the plus at the end hinder our
			 * parsing process
			 */
			int age = -1;
			String nextAge = scanner.next();
			if (nextAge.length() == 4)
				age = Integer.parseInt(nextAge.substring(0, 3));
			else
				age = Integer.parseInt(nextAge);
			double fqx = Double.parseDouble(scanner.next());
			double mqx = Double.parseDouble(scanner.next());
			scanner.next(); // we don't need the total parameter
			return new DeathData(year, age, fqx, mqx);
		} catch(Exception e) {
			log.warn("Can't parse an information from the file, have we reached the end of the file?");
			return null;
		}
	}
	
	@Override
	public void close() {
		if (scanner != null) {
			scanner.close();
		}
	}

}
