package edu.unc.mapseq.module.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 
 * @author jdr0887
 */
@Target(value = { TYPE })
@Retention(RUNTIME)
public @interface Application {

    String name();

    boolean isWorkflowRunIdOptional() default false;

    long wallTime() default 1; // day

    String executable() default "";

    String description() default "";

    String version() default "";

    boolean exitImmediately() default false;

}
