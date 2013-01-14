package eFrame.exception;

/**
 * 初始化系统异常
 * <br>
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class ServiceInitException extends RuntimeException{

	private static final long serialVersionUID = 1L;

    public ServiceInitException(String message, Throwable cause) {
        super(message, cause);
    }	
    
    public ServiceInitException(String message) {
        super(message);
    }    
}
