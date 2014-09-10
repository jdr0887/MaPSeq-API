package edu.unc.mapseq.module.constraints.impl;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import edu.unc.mapseq.module.constraints.Contains;

/**
 * 
 * @author jdr0887
 * 
 */
public class ContainsValidator implements ConstraintValidator<Contains, String> {

    private Contains annotation;

    public void initialize(Contains annotation) {
        this.annotation = annotation;
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        List<String> containList = Arrays.asList(this.annotation.values());
        if (StringUtils.isNotEmpty(value) && containList.contains(value)) {
            return true;
        }
        return false;
    }
}
