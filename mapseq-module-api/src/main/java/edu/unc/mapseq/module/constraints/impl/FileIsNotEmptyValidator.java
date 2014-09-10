package edu.unc.mapseq.module.constraints.impl;

import java.io.File;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.unc.mapseq.module.constraints.FileIsNotEmpty;

/**
 * 
 * @author jdr0887
 * 
 */
public class FileIsNotEmptyValidator implements ConstraintValidator<FileIsNotEmpty, File> {

    public void initialize(FileIsNotEmpty annotation) {
    }

    public boolean isValid(File value, ConstraintValidatorContext context) {
        if (value != null && value.length() > 0) {
            return true;
        }
        return false;
    }
}
