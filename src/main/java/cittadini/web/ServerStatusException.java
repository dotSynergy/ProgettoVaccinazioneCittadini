package cittadini.web;

/**
 * The type Server status exception.
 */
public class ServerStatusException extends RuntimeException {

    /**
     * The Code.
     */
    protected int code;

    /**
     * Instantiates a new Server status exception.
     *
     * @param code the code
     */
    public ServerStatusException(int code) {
        super("Status code "+code);
    }

}
