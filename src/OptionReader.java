import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class OptionReader {
	private static HashMap<String, String> userOptions = null;
	private static KWICObjectLoader kwicObjLoader = new KWICObjectLoader();
	
	private OptionReader() {
		// TODO Auto-generated constructor stub
	}
	
	public static void readOptions(String filePath) {
		userOptions = new HashMap<String, String>();
		Properties props = new Properties();
		
		try (FileInputStream fis = new FileInputStream(filePath)) {
			props.load(fis);
			for (String key : props.stringPropertyNames()) {
				userOptions.put(key, props.getProperty(key));
			}
		} catch (IOException e) {
			System.err.println("Error: Could not find or read " + filePath);
			e.printStackTrace();
		}
	}
		
	public static Object getObjectFromKey(String keyStr) { 
		Object kwicObj = null;
		if (userOptions.containsKey(keyStr)) {
			String objName;
			objName = userOptions.get(keyStr);
			kwicObj = kwicObjLoader.loadObject(objName);
		}
		return kwicObj;
	}
	
	public static Object getObjectFromStr(String objStr) {
		return kwicObjLoader.loadObject(objStr);
	}
	
	public static String getString(String keyStr) {
		String valueStr = "";
		if (userOptions.containsKey(keyStr)) {			
			valueStr = userOptions.get(keyStr);			
		}
		return valueStr;
	}
	
	
	

}