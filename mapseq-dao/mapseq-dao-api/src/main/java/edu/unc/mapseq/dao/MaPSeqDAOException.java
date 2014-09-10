package edu.unc.mapseq.dao;

public class MaPSeqDAOException extends Exception {

    private static final long serialVersionUID = -7851739902384642707L;

    public MaPSeqDAOException() {
        super();
    }

    public MaPSeqDAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaPSeqDAOException(String message) {
        super(message);
    }

    public MaPSeqDAOException(Throwable cause) {
        super(cause);
    }

}
