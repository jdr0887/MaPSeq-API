package edu.unc.mapseq.module.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.unc.mapseq.module.constraints.impl.FileListIsReadableValidator;

@Documented
@Constraint(validatedBy = FileListIsReadableValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface FileListIsReadable {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String message() default "{edu.unc.mapseq.module.constraints.FileListIsReadable.message}";

}
