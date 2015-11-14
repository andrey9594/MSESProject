package MSES.Project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class AgeDataReader extends DataReader {
	private final Scanner scanner;
	
	public AgeDataReader(String fileName) throws IOException {
		Path path = FileSystems.getDefault().getPath("resources", fileName);
		scanner = new Scanner(Files.newInputStream(path));
	}
	
	@Override
	public Data getNext() {
		String nextLine = scanner.nextLine();
		if (nextLine == null)
			return null;
		String values[] = nextLine.split(" ");
		//int year = ;
		//int age = ;
		//Data data = new Data();
		for (String s : values)
			System.out.println(s);
		return null;
	}
}
