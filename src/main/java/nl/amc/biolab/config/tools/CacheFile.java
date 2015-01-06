package nl.amc.biolab.config.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import nl.amc.biolab.config.manager.ConfigurationManager;

public class CacheFile {
	private String file_name;

	public CacheFile(String file_name_in) {
		this.file_name = file_name_in;
	}

	public boolean exists() {
		File cache_file = new File(file_name);

		ConfigurationManager.logger.log("A cache was found at: " + cache_file.getAbsolutePath() + ". "
				+ "Trying to retrieve the file name from this cache and checking if the configuration file is still at this location.", 1);

		return cache_file.exists();
	}

	public String read() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file_name));

			String this_line;

			while ((this_line = reader.readLine()) != null) {
				if (this_line.contains(".json") && new File(this_line).exists()) {
					ConfigurationManager.logger.log("Configuration file was found at: " + this_line, 1);

					reader.close();

					return this_line;
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void write(String file_path) {
		try {
			ConfigurationManager.logger.log("Writing the pathname to a cache file at: " + file_name, 1);

			PrintWriter writer = new PrintWriter(file_name);

			writer.println(file_path);

			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
