package eFrame.exception;

/**
 * 声明请求分发时候出现异常
 * <br>
 * @date 2013-1-16
 * @author LiangRL
 * @alias E.E.
 */
public class DispatchRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;

    public DispatchRequestException(String message, Throwable cause) {
        super(message, cause);
    }	
    
    public DispatchRequestException(String message) {
        super(message);
    }    
}
