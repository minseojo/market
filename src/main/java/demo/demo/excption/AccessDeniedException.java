package demo.demo.excption;

/**
 * 상태코드 401
 */
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String message) {
        super(message);
    }
}