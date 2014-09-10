package edu.unc.mapseq.module.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import edu.unc.mapseq.dao.model.MimeType;

/**
 * 
 * @author jdr0887
 */
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
public @interface OutputArgument {

    String flag() default "";

    String description() default "";

    boolean redirect() default false;

    String delimiter() default " ";

    MimeType mimeType() default MimeType.TEXT_PLAIN;

    boolean persistFileData() default false;

}
