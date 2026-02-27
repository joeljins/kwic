import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class KWICObjectLoader extends ClassLoader {

	public Object loadObject(String className) {
     try {
            // Create a new ClassLoader 
            ClassLoader loader = this.getClass().getClassLoader();
		
            // Load the target class using its name
            Class aClass = loader.loadClass(className);

            // Create a new instance from the loaded class
            Constructor constructor = aClass.getConstructor();
            Object obj = constructor.newInstance();

            //return the instance
            return obj;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}