package edu.unc.mapseq.module.constraints.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.unc.mapseq.module.constraints.EndsWith;

/**
 * 
 * @author jdr0887
 * 
 */
public class EndsWithValidator implements ConstraintValidator<EndsWith, String> {

    private EndsWith annotation;

    public void initialize(EndsWith annotation) {
        this.annotation = annotation;
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.endsWith(this.annotation.value())) {
            return true;
        }
        return false;
    }
}
