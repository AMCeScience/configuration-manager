package nl.amc.biolab.config.manager;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.tools.ConfigurationReader;
import nl.amc.biolab.config.tools.ConfigurationWriter;
import configmanager.crappy.logger.Logger;

public class ConfigurationManager {
	public static Logger logger = new Logger();
	
	public static ConfigurationReader read = null;
	public static ConfigurationWriter writer = null;
	
	public static String portlet = "Portlet";
	public static String config_file_path = null;
	private String config_file_path_default = "guse/apache-tomcat-6.0.36/webapps/config.json";
	
	/**
	 * Sets the configuration manager with the default configuration path
	 * 
	 * @throws ReaderException
	 */
	public ConfigurationManager() throws ReaderException {
		ConfigurationManager.config_file_path = null;
		
		_init();
	}

	/**
	 * Sets the configuration manager with a defined configuration path
	 * 
	 * @param config_file_path path to the configuration file
	 * @throws ReaderException
	 */
	public ConfigurationManager(String config_file_path) throws ReaderException {
		logger.log("Setting configuration path to: " + config_file_path, 1);

		ConfigurationManager.config_file_path = config_file_path;

		_init();
	}
	
	/**
	 * Sets the configuration manager with a defined configuration path and portlet name for logging purposes
	 * 
	 * @param config_file_path path to the configuration file
	 * @param portlet_name portlet name for logging purposes
	 * @throws ReaderException
	 */
	public ConfigurationManager(String config_file_path, String portlet_name) throws ReaderException {
		logger.log("Setting configuration path to: " + config_file_path, 1);

		ConfigurationManager.config_file_path = config_file_path;
		ConfigurationManager.portlet = portlet_name;

		_init();
	}

	private void _init() throws ReaderException {
		logger.log("##############################################################################################", 1);
		logger.log("The user directory on this machine is: " + System.getProperty("user.dir") + 
				". Provide a relative path from this folder or an absolute path to a different folder.", 1);
		logger.log("##############################################################################################", 1);
		
		if (ConfigurationManager.config_file_path == null) {
			// Set to default
			logger.log("Setting configuration path to default: " + config_file_path_default, 1);

			ConfigurationManager.config_file_path = config_file_path_default;
		}

		logger.log("Reading from: " + ConfigurationManager.config_file_path, 1);

		// Setting configuration reader and writer
		ConfigurationManager.read = new ConfigurationReader();
		ConfigurationManager.writer = new ConfigurationWriter();
		
		// Setting logging level from configuration file
		try {
			logger.level = read.getIntegerItem("logging", "cm_level");
		} catch (ReaderException e) {
			logger.level = 5;
			
			logger.log("Logging level is not set in configuration will default to lowest threshold.", 1);
		}
	}
}