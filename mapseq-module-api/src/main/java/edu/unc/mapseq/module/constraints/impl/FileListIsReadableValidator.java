package edu.unc.mapseq.module.constraints.impl;

import java.io.File;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.unc.mapseq.module.constraints.FileListIsReadable;

/**
 * 
 * @author jdr0887
 * 
 */
public class FileListIsReadableValidator implements ConstraintValidator<FileListIsReadable, List<File>> {

    public void initialize(FileListIsReadable annotation) {
    }

    public boolean isValid(List<File> value, ConstraintValidatorContext context) {
        for (File f : value) {
            if (f == null || (f != null && !f.canRead())) {
                return false;
            }
        }
        return true;
    }
}
