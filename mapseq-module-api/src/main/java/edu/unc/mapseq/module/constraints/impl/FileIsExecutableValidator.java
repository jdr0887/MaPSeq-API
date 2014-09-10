package edu.unc.mapseq.module.constraints.impl;

import java.io.File;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.unc.mapseq.module.constraints.FileIsExecutable;

/**
 * 
 * @author jdr0887
 * 
 */
public class FileIsExecutableValidator implements ConstraintValidator<FileIsExecutable, File> {

    public void initialize(FileIsExecutable annotation) {
    }

    public boolean isValid(File value, ConstraintValidatorContext context) {
        if (value != null && value.canExecute()) {
            return true;
        }
        return false;
    }
}
