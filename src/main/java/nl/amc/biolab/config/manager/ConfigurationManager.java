package nl.amc.biolab.config.manager;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.tools.ConfigurationReader;
import nl.amc.biolab.config.tools.ConfigurationWriter;

public class ConfigurationManager {
	public ConfigurationReader read;
	public ConfigurationWriter writer;
	private String config_file_path;
	private String config_file_path_default = "guse/apache-tomcat-6.0.36/webapps/config.json";
	
	public ConfigurationManager() throws ReaderException {
		_init();
	}
	
	public ConfigurationManager(String config_file_path) throws ReaderException {
		this.config_file_path = config_file_path;
		
		_init();
	}
	
	private void _init() throws ReaderException {
		if (this.config_file_path == null) {
			// Set to default
			this.config_file_path = config_file_path_default;
		}
		
		this.read = new ConfigurationReader(this.config_file_path);
		this.writer = new ConfigurationWriter(this.read);
	}
}