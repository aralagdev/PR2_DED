package uoc.ds.pr.exceptions;

public class UserProfileNotFoundException extends DSException {
    private static final long serialVersionUID = 1L;

    public UserProfileNotFoundException() {
        super();
    }

    public UserProfileNotFoundException(String msg) {
        super(msg);
    }
}
