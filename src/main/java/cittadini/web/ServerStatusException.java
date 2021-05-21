package cittadini.web;

public class ServerStatusException extends RuntimeException {

    protected int code;

    public ServerStatusException(int code) {
        super("Status code "+code);
    }

}
