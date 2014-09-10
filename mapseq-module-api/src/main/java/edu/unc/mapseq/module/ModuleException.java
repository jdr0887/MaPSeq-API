package edu.unc.mapseq.module;

/**
 * 
 * @author jdr0887
 * 
 */
public class ModuleException extends Exception {

    private static final long serialVersionUID = 3651017544997863363L;

    public ModuleException() {
        super();
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(Throwable cause) {
        super(cause);
    }

}
