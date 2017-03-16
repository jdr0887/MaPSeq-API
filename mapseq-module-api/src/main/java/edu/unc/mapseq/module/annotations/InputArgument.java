package edu.unc.mapseq.module.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author jdr0887
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface InputArgument {

    String flag() default "";

    String description() default "";

    String defaultValue() default "";

    String delimiter() default " ";

    int order() default 0;

    boolean wrapValueInSingleQuotes() default false;

    boolean disabled() default false;

}
