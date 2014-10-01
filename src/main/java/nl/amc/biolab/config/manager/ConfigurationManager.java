package nl.amc.biolab.config.manager;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.tools.ConfigurationReader;
import nl.amc.biolab.config.tools.ConfigurationWriter;

public class ConfigurationManager {
	public ConfigurationReader read;
	public ConfigurationWriter writer;
	private static String config_file_path = null;
	private String config_file_path_default = "guse/apache-tomcat-6.0.36/webapps/config.json";

	public ConfigurationManager() throws ReaderException {
		_init();
	}

	public ConfigurationManager(String config_file_path) throws ReaderException {
		System.out.println("Setting configuration path to: " + config_file_path);

		ConfigurationManager.config_file_path = config_file_path;

		_init();
	}

	private void _init() throws ReaderException {
		if (ConfigurationManager.config_file_path == null) {
			// Set to default
			System.out.println("Setting configuration path to default: " + config_file_path_default);

			ConfigurationManager.config_file_path = config_file_path_default;
		}

		System.out.println("Reading from: " + config_file_path);

		this.read = new ConfigurationReader(ConfigurationManager.config_file_path);
		this.writer = new ConfigurationWriter(this.read);
	}
}