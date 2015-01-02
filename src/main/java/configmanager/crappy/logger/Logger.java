package configmanager.crappy.logger;

import java.util.Date;

import nl.amc.biolab.config.manager.ConfigurationManager;

/**
 * Simple logger function
 * 
 * @author Allard van Altena
 */
public class Logger {
    public Logger log;
    public int level = 1;
    
    /**
     * Exposes the log variable to all the classes
     */
    public Logger() {
        log = this;
    }
    
    /**
     * Outputs a formatted message with the portlet name and the current datetime
     * @param message Message we want to output into the logs
     */    
    public void log(Object message, int level) {
        Date date = new Date();
        
        if (level <= this.level) {
        	System.out.println(ConfigurationManager.portlet + ", " + date.toString() + " MSG: " + message);
        }
    }
}