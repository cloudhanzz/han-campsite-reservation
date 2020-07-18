package han.jiayun.campsite.reservation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Is is used to specify the value of the annotated field should not be copied from one instance to another.
 * 
 * @author Jiayun Han
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotCopiable {
}