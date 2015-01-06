package nl.amc.biolab.config.manager;

import java.io.File;

import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.tools.CacheFile;
import nl.amc.biolab.config.tools.ConfigurationReader;
import nl.amc.biolab.config.tools.ConfigurationWriter;
import nl.amc.biolab.config.tools.FolderTreeSearcher;
import configmanager.crappy.logger.Logger;

public class ConfigurationManager {
	public static Logger logger = new Logger();
	
	public static ConfigurationReader read = null;
	public static ConfigurationWriter writer = null;
	
	public static String root_search_folder = "/home";
	public static String config_search_name = "config.json";
	public static String cache_search_name = "cache-config.txt";
	
	public static String portlet = "Portlet";
	public static String config_file_path = null;
	private static String config_file_path_default = System.getProperty("catalina.base") + "/conf/config.json";
	
	private ConfigurationManager() {}
	
	/**
	 * Sets the configuration manager with the default configuration path
	 * 
	 * @throws ReaderException
	 */
	public static void init() throws ReaderException {
		ConfigurationManager.config_file_path = null;
		
		_init();
	}

	/**
	 * Sets the configuration manager with a defined configuration path
	 * 
	 * @param config_file_path path to the configuration file
	 * @throws ReaderException
	 */
	public static void init(String config_file_path) throws ReaderException {
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
	public static void init(String config_file_path, String portlet_name) throws ReaderException {
		ConfigurationManager.config_file_path = config_file_path;
		ConfigurationManager.portlet = portlet_name;

		_init();
	}
	
	public static void init(String root_search_folder, String config_search_name, String portlet_name) throws ReaderException {
		ConfigurationManager.root_search_folder = root_search_folder;
		ConfigurationManager.config_search_name = config_search_name;
		ConfigurationManager.portlet = portlet_name;

		_init();
	}

	private static void _init() throws ReaderException {
		logger.log("##############################################################################################", 1);
		logger.log("The user directory on this machine is: " + System.getProperty("user.dir") + 
				". Provide a relative path from this folder or an absolute path to a different folder.", 1);
		logger.log("##############################################################################################", 1);
		
		// If no path was set
		if (ConfigurationManager.config_file_path == null) {
			// Lets do an educated guess at a default
			logger.log("Setting configuration path to default: " + config_file_path_default, 1);

			ConfigurationManager.config_file_path = config_file_path_default;
		}
		
		// Check if the configuration file exists at the currently set path
		if (!new File(ConfigurationManager.config_file_path).exists()) {
			String file_path = null;
			
			CacheFile cache = new CacheFile(ConfigurationManager.config_search_name.replace(".json", "") + "-" + ConfigurationManager.cache_search_name);
			
			// Check if configuration file path is saved in a cache file
			if (cache.exists()) {
				file_path = cache.read();
			}
			
			if (file_path == null || !new File(file_path).exists()) {
				logger.log("Defined configuration file path does not exist, starting search from: " + ConfigurationManager.root_search_folder 
						+ ". Looking for a file named: " + ConfigurationManager.config_search_name
						+ ". If these parameters are not correct please provide a correct configuration file path"
						+ " or the 'root folder', 'search name', and 'portlet name' to the ConfigurationManager constructor.", 1);
				
				// Search for file in the folder tree
				FolderTreeSearcher searcher = new FolderTreeSearcher();
				file_path = searcher.search(ConfigurationManager.root_search_folder, ConfigurationManager.config_search_name);
				
				logger.log("Found configuration file at: " + file_path, 1);

				// Write the path to a cache file
				cache.write(file_path);
			}
			
			ConfigurationManager.config_file_path = file_path;
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
		
		logger.log("Configuration manager initialisation done!", 1);
		logger.log("##############################################################################################", 1);
	}
}