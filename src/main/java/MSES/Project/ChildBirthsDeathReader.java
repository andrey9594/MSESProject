package MSES.Project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChildBirthsDeathReader {
	private static final Logger log = LoggerFactory.getLogger(ChildBirthsDeathReader.class);
	
	private final Scanner scanner;
	
	public ChildBirthsDeathReader(String fileName) throws IOException {
		Path path = FileSystems.getDefault().getPath("resources", fileName);
		scanner = new Scanner(Files.newInputStream(path));
		scanner.nextLine(); // it's the unused service head information
	}
	
	public HashMap<Integer, Double> getAllData() {
		HashMap <Integer, Double> result = new HashMap<>();
		while (scanner.hasNext()) {
			int age = Integer.parseInt(scanner.next());
			if (!scanner.hasNext())
				break;
			double death = Double.parseDouble(scanner.next());
			result.put(age, death);
		}
		return result;
	}
	
	public void close() {
		if (scanner != null) {
			scanner.close();
			log.info("Scanner has successfully closed");
		}
	}
}
