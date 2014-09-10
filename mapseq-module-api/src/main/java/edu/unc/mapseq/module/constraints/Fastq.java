package edu.unc.mapseq.module.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import edu.unc.mapseq.module.constraints.impl.FastqValidator;

@Documented
@Constraint(validatedBy = FastqValidator.class)
@Target({ TYPE, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Fastq {

    String message() default "{constraints.fastq}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String files();

    String platform();

    String aligner();

    String ends();

    @Target({ TYPE, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Fastq[] value();
    }
}
