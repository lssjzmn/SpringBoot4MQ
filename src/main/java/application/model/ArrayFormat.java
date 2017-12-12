package application.model;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by lancec on 2015/1/26.
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ArrayFormat {
    String pattern() default "\\s*[;]\\s*";
}
