package uoc.ds.pr.exceptions;

public class NoWebPagesFoundException extends DSException{
    private static final long serialVersionUID = 1L;

    public NoWebPagesFoundException() {
        super();
    }

    public NoWebPagesFoundException(String msg) {
        super(msg);
    }
}
