package nl.amc.biolab.config.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import nl.amc.biolab.config.exceptions.ReaderException;
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
	private String config_file_path_default = System.getProperty("catalina.base") + "/conf/config.json";
	
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
		ConfigurationManager.config_file_path = config_file_path;
		ConfigurationManager.portlet = portlet_name;

		_init();
	}
	
	public ConfigurationManager(String root_search_folder, String config_search_name, String portlet_name) throws ReaderException {
		ConfigurationManager.root_search_folder = root_search_folder;
		ConfigurationManager.config_search_name = config_search_name;
		ConfigurationManager.portlet = portlet_name;

		_init();
	}

	private void _init() throws ReaderException {
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
			
			File cache_file = new File(ConfigurationManager.cache_search_name);
			
			if (cache_file.exists()) {
				logger.log("A cache was found at: " + cache_file.getAbsolutePath() + ". "
						+ "Trying to retrieve the file name from this cache and checking if the configuration file is still at this location.", 1);
				
				try {
					BufferedReader reader = new BufferedReader(new FileReader(ConfigurationManager.cache_search_name));
					
					String thisLine;
					
					while ((thisLine = reader.readLine()) != null) {
						if (thisLine.contains(".json") && new File(thisLine).exists()) {
							logger.log("Configuration file was found and exists at: " + thisLine, 1);
							
							file_path = thisLine;
						}
					}
					
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
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
				try {
					logger.log("Writing the pathname to a cache file at: " + ConfigurationManager.cache_search_name, 1);
					PrintWriter writer = new PrintWriter(ConfigurationManager.cache_search_name);
					
					writer.println(file_path);
					
					writer.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
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