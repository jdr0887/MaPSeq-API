package edu.unc.mapseq.workflow;

public class WorkflowException extends Exception {

    private static final long serialVersionUID = 9004294363074403060L;

    public WorkflowException() {
        super();
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, cause);
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Throwable cause) {
        super(cause);
    }

}
