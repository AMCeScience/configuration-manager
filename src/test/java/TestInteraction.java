import static org.junit.Assert.assertEquals;
import nl.amc.biolab.config.exceptions.ReaderException;
import nl.amc.biolab.config.exceptions.WriterException;
import nl.amc.biolab.config.manager.ConfigurationManager;
import nl.amc.biolab.config.tools.FolderTreeSearcher;

import org.json.simple.JSONObject;
import org.junit.Test;


public class TestInteraction {
	@Test
	public void testSearch() {
		FolderTreeSearcher s = new FolderTreeSearcher();
		
		try {
			s.search("/", "test.json");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void testWrite() {
		try {
			ConfigurationManager config = new ConfigurationManager("src/test/resources/test.json");
			
			try {
				ConfigurationManager.writer.write("diff", new String[]{"test", "object", "test"});
				
				JSONObject test_diff = ConfigurationManager.read.getJSONObjectItem("test", "object");
				assertEquals("diff", test_diff.get("test"));
				
				ConfigurationManager.writer.write("test", new String[]{"test", "object", "test"});
				
				JSONObject test_object = ConfigurationManager.read.getJSONObjectItem("test", "object");
				assertEquals("test", test_object.get("test"));
			} catch (WriterException e) {
				e.printStackTrace();
			}
		} catch (ReaderException e) {
			e.printStackTrace();
		}
	}
	
	public void testRead() {
		try {
			ConfigurationManager config = new ConfigurationManager("src/test/resources/test.json");
			
			try {
				Object[] test_array = ConfigurationManager.read.getArrayItem("test", "array");
				assertEquals(123L, test_array[0]);
				
				Long test_long = ConfigurationManager.read.getLongItem("test", "long");
				assertEquals(new Long(321), test_long);
				
				// Direct test without the casting methods
				test_long = (Long) ConfigurationManager.read.getItem("test", "long");
				assertEquals(new Long(321), test_long);
				
				int test_integer = ConfigurationManager.read.getIntegerItem("test", "integer");
				assertEquals(321, test_integer);
				
				Double test_double = ConfigurationManager.read.getDoubleItem("test", "double");
				assertEquals(new Double(1.23), test_double);
				
				Float test_float = ConfigurationManager.read.getFloatItem("test", "float");
				assertEquals(new Float(3.21), test_float);
				
				String test_string = ConfigurationManager.read.getStringItem("test", "string");
				assertEquals("test", test_string);
				
				JSONObject test_object = ConfigurationManager.read.getJSONObjectItem("test", "object");
				assertEquals("test", test_object.get("test"));
				
				Boolean test_boolean = ConfigurationManager.read.getBooleanItem("test_bool");
				assertEquals(true, test_boolean);
				
				// Top level test
				String test_string_top = ConfigurationManager.read.getStringItem("test_2");
				assertEquals("test", test_string_top);
			} catch (ReaderException e) {
				e.printStackTrace();
			}
		} catch (ReaderException e) {
			e.printStackTrace();
		}
	}
	
	//@Test(expected=ReaderException.class)
	public void testReadArgumentsFail() throws ReaderException {
		ConfigurationManager config = new ConfigurationManager("src/test/resources/test.json");
		
		// No key is given, the method should throw an error
		ConfigurationManager.read.getItem();
	}
	
	//@Test(expected=ReaderException.class)
	public void testTooManyArgumentsFail() throws ReaderException {
		ConfigurationManager config = new ConfigurationManager("src/test/resources/test.json");
		
		// No key is given, the method should throw an error
		ConfigurationManager.read.getItem("test_2", "doesntexist");
	}
	
	//@Test(expected=ReaderException.class)
	public void testReadCastFail() throws ReaderException {
		ConfigurationManager config = new ConfigurationManager("src/test/resources/test.json");
		
		// Cast exceptions expected
		ConfigurationManager.read.getIntegerItem("test", "double");
	}
}