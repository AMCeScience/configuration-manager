package nl.amc.biolab.config.manager;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.tools.ConfigurationReader;
import nl.amc.biolab.config.tools.ConfigurationWriter;

public class ConfigurationManager {
	public ConfigurationReader read;
	public ConfigurationWriter writer;
	
	public ConfigurationManager(String config_file_location) throws ReaderException {
		this.read = new ConfigurationReader(config_file_location);
		this.writer = new ConfigurationWriter(this.read);
	}
}
