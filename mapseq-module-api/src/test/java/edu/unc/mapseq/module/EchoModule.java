package edu.unc.mapseq.module;

import java.io.File;

import javax.validation.constraints.NotNull;

import edu.unc.mapseq.module.annotations.Application;
import edu.unc.mapseq.module.annotations.InputArgument;
import edu.unc.mapseq.module.annotations.InputValidations;
import edu.unc.mapseq.module.annotations.OutputArgument;
import edu.unc.mapseq.module.annotations.OutputValidations;
import edu.unc.mapseq.module.constraints.FileIsNotEmpty;

/**
 * 
 * @author jdr0887
 */
@Application(name = "EchoModule", executable = "/bin/echo")
public class EchoModule extends Module {

    @NotNull(message = "message is null", groups = InputValidations.class)
    @InputArgument
    private String message;

    @NotNull(message = "output is null", groups = InputValidations.class)
    @FileIsNotEmpty(message = "output is empty", groups = OutputValidations.class)
    @OutputArgument(redirect = true)
    private File output;

    public EchoModule() {
        super();
    }

    @Override
    public Class<?> getModuleClass() {
        return EchoModule.class;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }

}
