package uoc.ds.pr.exceptions;

public class NoHistoryFoundException extends DSException{
    private static final long serialVersionUID = 1L;

    public NoHistoryFoundException() {
        super();
    }

    public NoHistoryFoundException(String msg) {
        super(msg);
    }
}
