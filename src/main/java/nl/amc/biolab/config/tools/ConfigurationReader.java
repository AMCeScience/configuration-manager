package nl.amc.biolab.config.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import nl.amc.biolab.config.exceptions.ReaderException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.primitives.Ints;

import configmanager.crappy.logger.Logger;

/**
 * Configuration file reader class, handles reading the configuration file and extracting specific configuration items from it
 *
 * @author Allard van Altena
 */
public class ConfigurationReader extends Logger {
	protected String config_file_location;
	protected File config_file;
	private Object json_obj;
	private Long read_time;
    
    public ConfigurationReader(String config_file_location) throws ReaderException {
    	this.config_file_location = config_file_location;
    	
    	_parseFile();
    }
    
    public Object[] getArrayItem(String... names) throws ReaderException {
    	Object result = getItem(names);
    	
    	if (result instanceof JSONArray) {
    		return ((JSONArray) result).toArray();  
    	}
    	
    	throw new ReaderException("No key exists with this type.");
    }
    
    public int getIntegerItem(String... names) throws ReaderException {
    	try {
    		return Ints.checkedCast(getLongItem(names));
    	} catch (IllegalArgumentException e) {
    		throw new ReaderException("No key exists with this type.");
    	}
    }
    
    public Long getLongItem(String... names) throws ReaderException {
    	try {
    		return Long.parseLong(getItem(names).toString());
    	} catch (NumberFormatException e) {
    		throw new ReaderException("No key exists with this type.");
    	}
    }
    
    public Double getDoubleItem(String... names) throws ReaderException {
    	try {
    		return Double.parseDouble(getItem(names).toString());
    	} catch (NumberFormatException e) {
    		throw new ReaderException("No key exists with this type.");
    	}
    }
    
    public Float getFloatItem(String... names) throws ReaderException {
    	try {
    		return Float.parseFloat(getItem(names).toString());
    	} catch (NumberFormatException e) {
    		throw new ReaderException("No key exists with this type.");
    	}
    }
    
    public Boolean getBooleanItem(String... names) throws ReaderException {
    	if (getItem(names).toString() == "true" || getItem(names).toString() == "false") {
    		return Boolean.parseBoolean(getItem(names).toString());
    	} else {
    		throw new ReaderException("No key exists with this type.");
    	}
    }
    
    public String getStringItem(String... names) throws ReaderException {
    	return getItem(names).toString();
    }
    
    public JSONObject getJSONObjectItem(String... names) throws ReaderException {
    	Object result = getItem(names);
    	
    	if (result instanceof JSONObject) {
    		return (JSONObject) result;
    	}
    	
    	throw new ReaderException("No key exists with this type.");
    } 
    
    public Object getItem(String... names) throws ReaderException {
    	if (names.length < 1) {
    		throw new ReaderException("No key arguments were given.");
    	}
    	
    	if (_hasBeenModified() || this.json_obj == null) {
    		log("modified", 5);
    		
    		_parseFile();
    	}
    	
    	String name = "";
    	Object current_obj = null;
    	JSONObject inner_obj = null;
    	
    	if (this.json_obj instanceof JSONObject) {
    		current_obj = this.json_obj;
    	} else {
    		throw new ReaderException("JSON file is not formatted properly.");
    	}
    	
    	for (int count = 0; count <= names.length; count++) {
    		if (!(current_obj instanceof JSONObject)) {
    			// We've gone to far i.e. key does not exist on this level
    			throw new ReaderException("Key does not exist in configuration file.");
    		}
    		
    		inner_obj = (JSONObject) current_obj;
    		
    		name = names[count];
    		
    		if (inner_obj != null && inner_obj.containsKey(name)) {
    			current_obj = _getItem(inner_obj, name);
    			
    			if (count + 1 == names.length) {
    				log("returning: " + current_obj.toString(), 5);
    				
    				return current_obj;
    			}
    		} else {
        		throw new ReaderException("Key does not exist in configuration file.");
        	}
    	}
    	
    	return current_obj;
    }
    
    public JSONObject getFullFile() throws ReaderException {
    	if (this.json_obj instanceof JSONObject) {
    		return (JSONObject) this.json_obj;
    	} else {
    		throw new ReaderException("JSON file is not formatted properly.");
    	}
    }
    
    private void _parseFile() throws ReaderException {
    	log("parsing", 5);
    	
    	JSONParser parser = new JSONParser();
    	
    	try {
    		_readFile();
    		
    		this.read_time = getConfigFile().lastModified();
        	this.json_obj = parser.parse(new FileReader(getConfigFile()));
    	} catch (IOException e) {
    		log(e.toString(), 1);
		} catch (ParseException e) {
			log(e.toString(), 1);
		}
    }
    
    protected JSONObject _parseJSONObjectFromFile() throws ReaderException {
    	JSONParser parser = new JSONParser();
    	
    	try {
    		_readFile();
        	
        	return (JSONObject) parser.parse(new FileReader(getConfigFile()));
    	} catch (IOException e) {
    		log(e.toString(), 1);
		} catch (ParseException e) {
			log(e.toString(), 1);
		}
    	
    	return null;
    }
    
    protected void _readFile() throws ReaderException {
    	File config_file = new File(this.config_file_location);
    	
    	if (config_file.exists()) {
    		_setConfigFile(config_file);
    	} else {
    		throw new ReaderException("Configuration file was not found on the provided location.");
    	}
    }
    
    protected void _setConfigFile(File config_file) {
    	this.config_file = config_file;
    }
    
    public File getConfigFile() {
    	return this.config_file;
    }
    
    private boolean _hasBeenModified() {
    	File config_file = new File(this.config_file_location);
    	
    	Long this_time = config_file.lastModified();

    	return (this_time > this.read_time);
    }
    
    private Object _getItem(JSONObject obj, String name) {
    	return obj.get(name);
    }
}
