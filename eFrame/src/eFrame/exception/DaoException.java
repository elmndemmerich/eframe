package eFrame.exception;

/**
 * 数据库相关异常
 * <br>
 * @date 2012-12-27
 * @author LiangRL
 * @alias E.E.
 */
public class DaoException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DaoException(String message){
		super(message);
	}
	
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
