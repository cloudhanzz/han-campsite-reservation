package han.jiayun.campsite.reservation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import han.jiayun.campsite.reservation.exceptions.GenericServerException;

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

	/**
	 * Copy all non-null values from {@code from} instance to {@code to} instance of the same class if the fields do not
	 * fall into {@code ignoredProperties}
	 * 
	 * @param from             The instance to copy values from, which must of the
	 *                         same class as {@code from}
	 * @param to               The instance to copy values to which must of the same
	 *                         class as {@code to}
	 * @param ignoreProperties Serving as a filter: the values of the fields will
	 *                         not be copied even though the values are not null if
	 *                         the names of the fields are contained in it
	 */
	public static void copyNonNullValues(String requestId, Object from, Object to, List<String> ignoreProperties) {

		if (ignoreProperties == null) {
			ignoreProperties = new ArrayList<>();
		}

		ensureSameClassType(from, to, requestId);

		final BeanWrapper src = new BeanWrapperImpl(from);
		final BeanWrapper tgt = new BeanWrapperImpl(to);

		for (final Field property : to.getClass().getDeclaredFields()) {
			String name = property.getName();
			if (ignoreProperties.contains(name) == false) {
				Object value = src.getPropertyValue(name);
				if (value != null) {
					tgt.setPropertyValue(property.getName(), value);
				}
			}
		}
	}

	private static void ensureSameClassType(Object a, Object b, String requestId) {
		Class<? extends Object> classA = a.getClass();
		Class<? extends Object> classB = b.getClass();
		if (classA != b.getClass()) {
			String message = String.format(
					"copyNonNullValues method should not operate on two different classes: %s, %s", classA, classB);
			throw new GenericServerException(message);
		}
	}

}
