package edu.unc.mapseq.module;

public class DefaultModuleOutput implements ModuleOutput {

    private int exitCode = 0;

    private StringBuilder output = new StringBuilder();

    private StringBuilder error = new StringBuilder();

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public StringBuilder getOutput() {
        return output;
    }

    public void setOutput(StringBuilder output) {
        this.output = output;
    }

    public void setError(StringBuilder error) {
        this.error = error;
    }

    public StringBuilder getError() {
        return error;
    }

}
