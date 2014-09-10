package edu.unc.mapseq.module.constraints.impl;

import java.io.File;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.unc.mapseq.module.constraints.FileIsReadable;

/**
 * 
 * @author jdr0887
 * 
 */
public class FileIsReadableValidator implements ConstraintValidator<FileIsReadable, File> {

    public void initialize(FileIsReadable annotation) {
    }

    public boolean isValid(File value, ConstraintValidatorContext context) {
        if (value != null && value.exists() && value.canRead()) {
            return true;
        }
        return false;
    }
}
