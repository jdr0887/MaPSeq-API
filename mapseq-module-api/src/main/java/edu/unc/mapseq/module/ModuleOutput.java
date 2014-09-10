package edu.unc.mapseq.module;

public interface ModuleOutput {

    public int getExitCode();

    public StringBuilder getOutput();

    public StringBuilder getError();

}
