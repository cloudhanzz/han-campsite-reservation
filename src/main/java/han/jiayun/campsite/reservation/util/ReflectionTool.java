package han.jiayun.campsite.reservation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple tool for using Java reflections.
 * 
 * @author Jiayun Han
 *
 */
public final class ReflectionTool {

	/**
	 * Disable instantiation
	 */
	private ReflectionTool() {
	}

	/**
	 * Find the names of the fields of a class that are wanted by are missing
	 * 
	 * @param object          The instance of the class to check
	 * @param wantedAttribute The annotated wanted field name
	 * @return An empty list if no missing attribute is found; otherwise the missing
	 *         attribute names as a list
	 */
	public static List<String> getMissedAttributes(Object object, Class<? extends Annotation> wantedAttribute) {

		List<String> missingAttributes = new ArrayList<>();

		Field[] fields = object.getClass().getDeclaredFields();

		for (Field field : fields) {

			if (field.getDeclaredAnnotation(wantedAttribute) == null) {
				continue;
			}

			field.setAccessible(true);

			Object obj;

			try {
				obj = field.get(object);
				if (obj == null) {
					missingAttributes.add(field.getName());
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return missingAttributes;
	}
}
