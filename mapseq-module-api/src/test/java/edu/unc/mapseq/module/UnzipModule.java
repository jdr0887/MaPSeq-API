package edu.unc.mapseq.module;

import java.io.File;

import javax.validation.constraints.NotNull;

import edu.unc.mapseq.module.annotations.Application;
import edu.unc.mapseq.module.annotations.InputArgument;
import edu.unc.mapseq.module.annotations.InputValidations;
import edu.unc.mapseq.module.annotations.OutputValidations;
import edu.unc.mapseq.module.constraints.FileIsNotEmpty;
import edu.unc.mapseq.module.constraints.FileIsReadable;

/**
 * 
 * @author jdr0887
 * 
 */
@Application(name = "UnzipModule", executable = "/usr/bin/unzip")
public class UnzipModule extends Module {

    @NotNull(message = "Zip is required", groups = InputValidations.class)
    @FileIsReadable(message = "Zip file does not exist or is not readable", groups = InputValidations.class)
    @InputArgument(description = "Input zip file")
    private File zip;

    @NotNull(message = "extract is required", groups = InputValidations.class)
    @FileIsNotEmpty(message = "invalid output file", groups = OutputValidations.class)
    @InputArgument(flag = "-d", description = "Extract directory")
    private File extract;

    @InputArgument(flag = "-o", description = "Overwrite files WITHOUT prompting")
    private Boolean overwrite;

    public UnzipModule() {
        super();
    }

    @Override
    public Class<?> getModuleClass() {
        return UnzipModule.class;
    }

    public File getZip() {
        return zip;
    }

    public void setZip(File zip) {
        this.zip = zip;
    }

    public File getExtract() {
        return extract;
    }

    public void setExtract(File extract) {
        this.extract = extract;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

}
