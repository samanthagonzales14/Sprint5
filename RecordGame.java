package sprint5_0.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RecordGame {
	private String filePath;
	private List<String> moves = new ArrayList<>();
	public BufferedWriter writer;
	
	public RecordGame(String file) {
		this.filePath = file;
	}
	public void recordMove(String move) {
		// Function to write to file and append subsequent data to end of file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
			writer.write(move);
			writer.newLine();	
			System.out.println("Move recorded successfully.");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error has occurred.");
		}
	}
	public List<String> readRecordedMoves() throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))){
			String line;
			while((line = reader.readLine()) != null) {
				moves.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("An error has occurred while reading recorded moves.");
			throw e;
		} 
		return moves;		
	}
	public void deleteRecordedFile() {
		// Function to delete entire file and contents
		File file = new File(filePath);
		if (file.exists()) {		
			if (file.delete()) {
				System.out.println("File deleted successfully.");
				} else {
	                System.err.println("Failed to delete the file.");
	            }
			} else {
				System.out.println("File does not exist.");
			}
	}
}
