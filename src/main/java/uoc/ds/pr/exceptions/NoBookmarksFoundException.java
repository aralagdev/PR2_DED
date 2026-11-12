package uoc.ds.pr.exceptions;

public class NoBookmarksFoundException extends DSException{
    private static final long serialVersionUID = 1L;

    public NoBookmarksFoundException() {
        super();
    }

    public NoBookmarksFoundException(String msg) {
        super(msg);
    }
}
