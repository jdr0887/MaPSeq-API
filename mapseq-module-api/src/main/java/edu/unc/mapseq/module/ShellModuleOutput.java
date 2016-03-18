package edu.unc.mapseq.module;

import org.renci.common.exec.CommandOutput;

public class ShellModuleOutput implements ModuleOutput {

    private CommandOutput output;

    public ShellModuleOutput(CommandOutput output) {
        super();
        this.output = output;
    }

    @Override
    public StringBuilder getOutput() {
        return output.getStdout();
    }

    @Override
    public StringBuilder getError() {
        return output.getStderr();
    }

    @Override
    public int getExitCode() {
        return output.getExitCode();
    }

    @Override
    public String toString() {
        return String.format("ShellModuleOutput [getExitCode()=%s]", getExitCode());
    }

}
