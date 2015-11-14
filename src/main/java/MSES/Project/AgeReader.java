package MSES.Project;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class AgeReader extends DataReader {
	private final Scanner scanner;
	
	public AgeReader(String fileName) throws IOException {
		Path path = FileSystems.getDefault().getPath("resources", fileName);
		scanner = new Scanner(Files.newInputStream(path));
	}
	
	@Override
	public Data getNext() {
		return null;
	}
}
