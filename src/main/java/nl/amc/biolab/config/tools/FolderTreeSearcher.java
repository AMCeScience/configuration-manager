package nl.amc.biolab.config.tools;

import java.io.File;
import java.io.FilenameFilter;

import nl.amc.biolab.config.exceptions.ReaderException;
import configmanager.crappy.logger.Counter;

public class FolderTreeSearcher {
	// Run for specified amount of time before exiting
	int execution_time = 600;
	
	Counter count = new Counter();
	
	public String search(String rootFolder, String name) throws ReaderException {
		count.start("Timing the configuration file searcher.");
		
		File root = new File(rootFolder);
		
		// Check if given root folder exists
		if (!root.exists()) {
			throw new ReaderException("Given root could not be found.");
		}
		
		// Loop directory
		File result = findFile(root, name);
		
		if (result == null) {
        	throw new ReaderException("Could not find the configuration file under root: " + root.getAbsolutePath());
        }
		
    	return result.getAbsolutePath();
	}
	
	public File findFile(File directory, String fileName) throws ReaderException {
		if (count.poll() / 1000 > execution_time) {
			throw new ReaderException("Execution time was reached, exiting.");
		}
		
		// Get file in this directory ignore hidden directories/files (starting with '.')
		File[] dirFiles = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		});
		
		if (dirFiles == null || dirFiles.length < 1) {
			return null;
		}
		
		// Loop files
		for (File file : dirFiles) {
			// Test if this is the configuration file
			if (file.getName().matches(fileName) && file.exists()) {
				return file;
			}
			
			if (file.isDirectory()) {
				File testDir = findFile(file, fileName);
				
				if (testDir != null) {
					return testDir;
				}
			}
		}
		
		return null;
	}
}